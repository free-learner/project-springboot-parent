package com.yh.loan.front.test.base;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.alibaba.fastjson.JSON;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.personal.springboot.Application;
import com.personal.springboot.controller.vo.AppAuthHeader;

/**
 * 基础Controller测试类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
@WebAppConfiguration
@SpringBootTest(classes=Application.class)
@ContextConfiguration(classes = Application.class) 
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class BaseControllerFileTest {

    @Autowired
    protected MockMvc mvc;

    @Autowired
    private WebApplicationContext webApplicationConnect;

    @Before
    public void setUp() throws JsonProcessingException {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationConnect).build();
    }
    
    /**
     * 文件下载测试
     */
    @Test
    public void testDownload() throws Exception {
        String uri = "/file/download/0";
        AppAuthHeader header=new AppAuthHeader();
        header.setMobilePhone("18611478781");
        header.setUserToken("TOKEN_2ad54d9019bd4cd1b7f09b4ac1593cd2");
        
        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .header("Auth-Header", JSON.toJSONString(header))
                .characterEncoding("utf-8")
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .accept(MediaType.TEXT_EVENT_STREAM)).andReturn();
        /*String code = "1ea39089948348349bc676a1872a4619";
        MvcResult mvcResult = mvc
                .perform(MockMvcRequestBuilders.post(uri).header("Auth-Header", JSON.toJSONString(header))
                        .accept(MediaType.TEXT_EVENT_STREAM)
                        .characterEncoding("utf-8")
                        .contentType(MediaType.MULTIPART_FORM_DATA)
                        .param("code", code))
                .andReturn();*/
        int status = mvcResult.getResponse().getStatus();
        String content = mvcResult.getResponse().getContentAsString();
        Assert.assertNotNull("返回值信息为空!",content);
        Assert.assertTrue("错误，正确的返回值为200", status == 200);
    }

    /**
     * 文件上传测试
     */
    @Test
    public void testUpload1() throws Exception {
        String uri = "/file/upload/1";
        AppAuthHeader header=new AppAuthHeader();
        header.setMobilePhone("18611478781");
        header.setUserToken("TOKEN_2ad54d9019bd4cd1b7f09b4ac1593cd2");
        //{"userToken":"TOKEN_2ad54d9019bd4cd1b7f09b4ac1593cd2","mobilePhone":"18611478781"}
        File localFile=new File("D:\\temp\\yhloan-front-api-0.0.1-SNAPSHOT.jar");
        byte[] byteArray = FileUtils.readFileToByteArray(localFile);
        String mimeType = ContentType.MULTIPART_FORM_DATA.getMimeType();
        MockMultipartFile file = new MockMultipartFile("file", "orig1.jar", mimeType, byteArray);  
        
        //MockMultipartFile file = new MockMultipartFile("file", "orig1.txt", null, "bar file content111".getBytes());  
        ResultActions mvcResult = mvc.perform(MockMvcRequestBuilders.fileUpload(uri).file(file)
                .contentType(MediaType.MULTIPART_FORM_DATA)
                .header("Auth-Header", JSON.toJSONString(header)))
                .andExpect(status().isOk()); 
        Assert.assertNotNull("返回值信息为空!",mvcResult);
    }
    
    @Test
    public void testUploadFileController() throws Exception {
        String str="http://jh-dev.j1health.net/jh-statistics/record/upload_txt_file/0";
        String filePath="D:\\temp\\record.txt";
        String fileName="record.txt";
        try {
            URL url=new URL(str);
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.addRequestProperty("FileName", fileName);
            //connection.setRequestProperty("content-type", "text/html");
            //connection.setRequestProperty("Content-Type", "multipart/form-data");
            connection.setRequestProperty("Content-Type", "text/plain");
            connection.setRequestProperty("Content-Disposition", "form-data; name=\"txtFile\";filename=\"record.txt\"");
            BufferedOutputStream  out=new BufferedOutputStream(connection.getOutputStream());
            //Content-Type: text/plain
            //Content-Disposition: form-data; name="txtFile"; filename="record.txt"

            //读取文件上传到服务器
            File file=new File(filePath);
            FileInputStream fileInputStream=new FileInputStream(file);
            byte[]bytes=new byte[1024];
            int numReadByte=0;
            while ((numReadByte = fileInputStream.read(bytes, 0, 1024)) > 0) {
                out.write(bytes, 0, numReadByte);
            }

            out.flush();
            fileInputStream.close();
            //读取URLConnection的响应
            DataInputStream in=new DataInputStream(connection.getInputStream());
            byte[] b = new byte[1024];
            int len = 0;
            while((len = in.read(b)) != -1){
                System.out.println(new String(b,0,len));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        Assert.assertTrue(true);
    }
    
    
    @Test
    public void testUploadFileController2() throws Exception {
        //String fileField="txtFile";
        String fileField="file";
        String fileName="record.txt";
        String fileType="text/plain";
        String filePath="D:\\temp\\record.txt";
        String key="userName";
        String value="liubao";
        String urlStr="http://jh-dev.j1health.net/jh-statistics/record/upload_txt_file/0";
        //strParams 1:key 2:value
        List<String[]> strParams=new ArrayList<String[]>();
        strParams.add(new String[]{key,value});
        //fileParams 1:fileField, 2.fileName, 3.fileType, 4.filePath 
        List<String[]> fileParams=new ArrayList<String[]>();;
        fileParams.add(new String[]{fileField,fileName,fileType,filePath});
        HttpMultipartRequest request=new HttpMultipartRequest(urlStr, strParams, fileParams);
        byte[] responseResult = request.sendPost();
        System.out.println(new String(responseResult,"UTF-8"));
        Assert.assertTrue(true);
    }
    
}