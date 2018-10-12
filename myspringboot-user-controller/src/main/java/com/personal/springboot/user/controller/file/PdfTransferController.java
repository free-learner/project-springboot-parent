package com.personal.springboot.user.controller.file;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;  
//import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;  
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSON;
import com.mysql.jdbc.util.Base64Decoder;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.entity.BaseEntity;
import com.personal.springboot.common.utils.CommonUtils;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.common.utils.FileUploadUtils;
import com.personal.springboot.common.utils.FtpUtils;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.controller.vo.ResultInfo;
import com.personal.springboot.user.controller.LoggerUtils;
import com.personal.springboot.user.controller.vcode.Captcha;
import com.personal.springboot.user.controller.vcode.GifCaptcha;
import com.personal.springboot.user.controller.vcode.SpecCaptcha;
import com.personal.springboot.user.controller.vcode.VerifyCodeUtils;

import sun.misc.BASE64Encoder;

/**
 * BannerController 控制层
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
@Controller
@RequestMapping("/**/file/**")
public class PdfTransferController extends BaseController<BaseEntity,PdfTransferController> {
    
    @Value("${pdf.request.url}")
    private String pdfRequestUrl;
    
    @Value("${file.upload.path}")
    private String uploadPath;
    
    @Autowired
    private FtpUtils ftpUtils;
    
    @Value("${ftp.conf.remoteFtpPath}")
    private String remoteFtpPath;
    
    private final ResourceLoader resourceLoader;  
    @Autowired  
    public PdfTransferController(ResourceLoader resourceLoader) {  
        this.resourceLoader = resourceLoader;  
    } 
    
    /*@RequestMapping(method = RequestMethod.GET, value = "/")  
    public String provideUploadInfo(Model model) throws IOException {  
        model.addAttribute("files", Files.walk(Paths.get(ROOT))  
                .filter(path -> !path.equals(Paths.get(ROOT)))  
                .map(path -> Paths.get(ROOT).relativize(path))  
                .map(path -> linkTo(methodOn(FileUploadController.class).getFile(path.toString())).withRel(path.toString()))  
                .collect(Collectors.toList()));  
  
        return "uploadForm";  
    }*/
    
    /**
     *推荐使用的文件上传方式
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.POST, value = "/upload/2")  
    public Object handleFileUpload(@RequestHeader Map<String,Object> headerMap,@RequestParam/*("file")*/ MultipartFile file, RedirectAttributes redirectAttributes,HttpServletRequest request,HttpServletResponse response) {
        getLogger().info("header:{}",JSON.toJSONString(headerMap));
        if (!file.isEmpty()) {
            try {
                String filename=file.getOriginalFilename();
                Path path = Paths.get(uploadPath, filename);
                String realLocalPath = path.toString();
                getLogger().info("读取文件信息realLocalPath:{}",realLocalPath);
                Files.copy(file.getInputStream(), path);
                redirectAttributes.addFlashAttribute("message",  "You successfully uploaded " + file.getOriginalFilename() + "!");
            } catch (IOException | RuntimeException e) {
                redirectAttributes.addFlashAttribute("message", "Failued to upload " + file.getOriginalFilename() + " => " + e.getMessage());
            }
        } else {
            redirectAttributes.addFlashAttribute("message", "Failed to upload " + file.getOriginalFilename() + " because it was empty");
        }

        response.reset();
        response.setCharacterEncoding("UTF-8");
        ContentType.APPLICATION_JSON.getMimeType();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return  super.getSuccessResultInfo("上传成功");
    }
    
    /**
     *推荐使用的上传图片访问方式
     *http://localhost:8080/service/file/read/002.jpg
     */
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/read/{filename:.+}")  
    public ResponseEntity<?> getFile(@PathVariable String filename) {  
        ResponseEntity<?> result = null;
        try {  
            String realLocalPath = Paths.get(uploadPath, filename).toString();
            getLogger().info("读取文件信息realLocalPath:{}",realLocalPath);
            result = ResponseEntity.ok(resourceLoader.getResource("file:" + realLocalPath));  
        } catch (Exception e) {
            getLogger().error("读取文件信息异常!",e);
            result= ResponseEntity.notFound().build();  
        }  
        return result;
    } 

    
    
    /**
     * pdf文件流下载请求转发
     */
    @RequestMapping(value="/download/0",method = { RequestMethod.GET,RequestMethod.POST})
    public void download(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
        String requestURL = request.getRequestURL().toString();
        String requestURI = request.getRequestURI();
        String contextPath = request.getContextPath();
        //requestURI=/LoanFrontService/pdf,requestURL=http://10.0.12.26/LoanFrontService/pdf,contextPath=/LoanFrontService
        getLogger().info("请求地址信息:requestURI={},requestURL={},contextPath={}",requestURI,requestURL,contextPath);
        String redirectUrl=pdfRequestUrl+requestURI.replace(contextPath, "");
        redirectUrl=requestURI.replace("/file/", "/");
        getLogger().info("请求地址信息:redirectUrl={}",redirectUrl);
        //response.sendRedirect("http://qcloudoss.xunjiepdf.com/xunjiepdf/temp/20170502/shortcut.pdf");
        //response.sendRedirect("https://pdfobject.com/pdf/sample-3pp.pdf");
        //response.sendRedirect("http://10.0.66.235/jr//yhloan/download/D909EE961046D49B1725FB0B37D00474.pdf");
        //request.getRequestDispatcher("http://10.0.66.235/jr//yhloan/download/D909EE961046D49B1725FB0B37D00474.pdf").forward(request, response);
        response.sendRedirect(redirectUrl);
        //request.getRequestDispatcher(redirectUrl).forward(request, response);
    }
    
    /**
     * 单文件上传具体实现方法;
     */
    @ResponseBody
    @RequestMapping("/upload/0")
    public Object handleFileUpload(@RequestParam("file") MultipartFile file) {
        if (!file.isEmpty()) {
            try {
                /*
                 * 这段代码执行完毕之后，图片上传到了工程的跟路径； 大家自己扩散下思维，如果我们想把图片上传到
                 * d:/files大家是否能实现呢？ 等等;
                 * 这里只是简单一个例子,请自行参考，融入到实际中可能需要大家自己做一些思考，比如： 1、文件路径； 2、文件名；
                 * 3、文件格式; 4、文件大小的限制;
                 */

                BufferedOutputStream out = new BufferedOutputStream(
                        new FileOutputStream(new File(file.getOriginalFilename())));
                out.write(file.getBytes());
                out.flush();
                out.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            } catch (IOException e) {
                e.printStackTrace();
                return "上传失败," + e.getMessage();
            }
            return super.getSuccessResultInfo("上传成功");
        } else {
            return "上传失败，因为文件是空的.";
        }
    }

    /**
     * 多文件具体上传时间，主要是使用了MultipartHttpServletRequest和MultipartFile
     */
    @ResponseBody
    @RequestMapping(value = "/batch/upload/0", method = RequestMethod.POST)
    public Object handleFileUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        MultipartFile file = null;
        BufferedOutputStream stream = null;
        for (int i = 0; i < files.size(); ++i) {
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    stream = new BufferedOutputStream(new FileOutputStream(new File(file.getOriginalFilename())));
                    stream.write(bytes);
                    stream.close();
                } catch (Exception e) {
                    stream = null;
                    return "You failed to upload " + i + " => " + e.getMessage();

                }
            } else {
                return "You failed to upload " + i + " because the file was empty.";
            }
        }
        return super.getSuccessResultInfo("上传成功");
    }
    
    @RequestMapping("/page")
    public String greeting() {
        return "file";
    }
    
    /**
     * 根据用户上传的文件信息
     * 1.上传文件File
     * 2.解析数据
     */
    @ResponseBody
    @RequestMapping(value = "/upload/1", method = RequestMethod.POST)
    public Object /*void*/ upload(@RequestHeader Map<String,Object> headerMap ,@RequestParam("file") MultipartFile file,PrintWriter printWriter,HttpServletRequest request,HttpServletResponse response) throws IOException {
        getLogger().info("根据用户上传的文件信息:@RequestHeader>>>"+ JSON.toJSONString(headerMap));
        request.setCharacterEncoding("UTF-8");
        
        Object resultInfo = null;
        if (file == null || file.isEmpty()) {
            resultInfo = super.getFailureResultInfo("");
            getLogger().error("用户上传文件信息,上传文件为空!" );
        }else{
            //1.文件上传
            /**调用上传图片组件**/
            List<String> filePathList = FileUploadUtils.uploadMultipartFileList(new MultipartFile[] {file}, uploadPath);
            if (CollectionUtils.isEmpty(filePathList)) {
                //上传失败
                getLogger().error("解析文件信息为空!");
                resultInfo = super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_DEFAULT);
            }else{
                //上传成功
                resultInfo= super.getSuccessResultInfo("上传成功");
            }
        }
        
        //输出上传结果
        getLogger().warn("上传结果:" + JSON.toJSONString(resultInfo));
//        String jsonString = JSON.toJSONString(resultInfo, SerializerFeature.PrettyFormat);
//        printWriter.write(jsonString);
//        printWriter.flush();
//        printWriter.close();
        response.reset();
        response.setCharacterEncoding("UTF-8");
        ContentType.APPLICATION_JSON.getMimeType();
        response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
        return resultInfo;
    }
    
    /**
     * 上传用户借据凭证信息<批量文件上传>
     * @RequestParam("files") MultipartFile[] files
     * @RequestParam("files[]") List<MultipartFile> files
     * List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
     */
    @RequestMapping(value = "/upload/3", method = RequestMethod.POST)
    public void upload1(@RequestHeader Map<String,Object> headerMap,@RequestParam("files") MultipartFile[] files,@RequestParam(value="cino") String cino,
           @RequestParam("payAmouns") String[] payAmouns,@RequestParam("payAccounts") List<String> payAccounts,
            HttpServletRequest request,HttpServletResponse response) throws Exception {
        request.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        if(getLogger().isInfoEnabled()){
            getLogger().info("上传用户借据凭证信息:@RequestHeader>>>{}", JSON.toJSONString(headerMap));
            getLogger().info("上传用户借据凭证信息,cino={},payAmouns:{},payAccounts:{}",cino,payAmouns,payAccounts);
        }
        
        Assert.notNull(ftpUtils, "ftpUtils注入为空!");
        ResultInfo<?> resultInfo = null;
        if (files == null || ArrayUtils.isEmpty(files)) {
            getLogger().error("上传文件信息为空!");
            resultInfo = super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_DEFAULT);
        } else {
            /** 调用上传图片组件 **/
            List<String> filePathList = FileUploadUtils.uploadMultipartFileList(files, uploadPath + File.separator + "loanImage");
            if (CollectionUtils.isEmpty(filePathList)) {
                getLogger().error("上传文件信息返回路径为空!");
                resultInfo = super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_DEFAULT);
            } else {
                try {
                    List<Map<String,Object>> paramList=new ArrayList<>();
                    ftpUtils.connectFTPServer();
                    for (int i = 0; i < files.length; i++) {
                        Map<String,Object> paramMap=new HashMap<>();
                        MultipartFile multipartFile = files[i];
                        String originalName= multipartFile.getOriginalFilename();
                        String newName="_"+cino+"_"+originalName;
                        String payAmoun = payAmouns[i];
                        String payAccount = payAccounts.get(i);
                        paramMap.put("cino", cino);
                        paramMap.put("payAmoun", payAmoun);
                        paramMap.put("payAccount", payAccount);
                        paramMap.put("imagePath", remoteFtpPath+File.separator+newName);
                        paramList.add(paramMap);
                        Boolean result = ftpUtils.uploadFile(multipartFile.getInputStream(), newName, remoteFtpPath);
                        getLogger().info("ftp上传文件信息结果为{}",result);
                    }
                    getLogger().info("ftp上传文件信息请求参数paramList为{}",JSON.toJSONString(paramList));
                    // 異步调用信贷接口,传递数据，同时保存值到数据库中
                    
                    /*for (String filePath : filePathList) {
                        String newName= filePath.substring(filePath.lastIndexOf(File.separator)+1);
                        newName=custcd+"_"+cino+"_"+newName;
                        getLogger().info("ftp上传文件信息newName为{}",newName);
                        ftpUtils.uploadFile(filePath, newName, remoteFtpPath);
                    }*/
                    resultInfo = super.getSuccessResultInfo(null);
                    resultInfo.setMessage(super.getMessage(ErrorCodeConstant.ERROR_CODE_DEFAULT));
                } catch (Exception e) {
                    getLogger().error("上传文件信息返异常!",e);
                    resultInfo = super.getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_DEFAULT);
                }finally {
                    if(ftpUtils!=null){
                        ftpUtils.closeFTPClient();
                    }
                }
            }
        }
        
        //输出上传结果
        getLogger().warn("上传结果:" + JSON.toJSONString(resultInfo));
        String jsonString = JSON.toJSONString(resultInfo);
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }
    
    /**
     * 图片验证码编码返回
     */
    @ResponseBody
    @RequestMapping(value="/img1",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json")
    public Object img1(HttpServletRequest request, HttpServletResponse response) {
        Font mFont = new Font("Default", Font.BOLD, 20);
        int IMG_WIDTH = 60;
        int IMG_HEIGHT = 25;
        int FONT_SIZE = 12;
        String sRand = "";
        FileOutputStream fo=null;
        Map<String,String> map = new HashMap<>();
        try {
            BufferedImage image = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics g = image.getGraphics();
            Random random = new Random();
            g.setColor(CommonUtils.getRandColor(250, 255));
            // 填充背景色
            g.fillRect(1, 1, IMG_WIDTH - 1,IMG_HEIGHT - 1);
            // 为图形验证码绘制边框
            g.setColor(new Color(102, 102, 102));
            g.drawRect(0, 0, IMG_WIDTH - 1,
                    IMG_HEIGHT - 1);
            g.setColor(CommonUtils.getRandColor(160, 200));
            // 生成随机干扰线
            for (int i = 0; i < 80; i++) {
                int x = random.nextInt(IMG_WIDTH - 1);
                int y = random.nextInt(IMG_HEIGHT - 1);
                int xl = random.nextInt(6) + 1;
                int yl = random.nextInt(12) + 1;
                g.drawLine(x, y, x + xl, y + yl);
            }
            g.setColor(CommonUtils.getRandColor(160, 200));
            // 设置绘制字符的字体
            g.setFont(mFont);
            // 用于保存系统生成的随机字符串 
            for (int i = 0; i < 4; i++) {
                String tmp = CommonUtils.getRandomChar();
                sRand += tmp;
                // 获取随机颜色 80.
                g.setColor(new Color(20 + random.nextInt(110), 20 + random
                        .nextInt(110), 20 + random.nextInt(110)));
                // 在图片上绘制系统生成的随机字符
                g.drawString(tmp, FONT_SIZE * i + 10, (IMG_HEIGHT+FONT_SIZE)/2);
            }
            g.dispose();
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            ImageIO.write(image, "JPEG", out);
            byte[] data = out.toByteArray();  
            // 对字节数组Base64编码
            BASE64Encoder encoder = new BASE64Encoder();
            String base64Str = encoder.encode(data);
            String imgId = DateTimeUtil.dateToNumber(new Date()) + UUIDGenerator.generate();
            map.put("cacheVerifyCode", imgId);
            map.put("image", base64Str);
        } catch (Exception e) {
           e.printStackTrace();
        } finally{
            if(fo!=null){
                try {
                    fo.close();
                } catch (IOException e) { 
                    e.printStackTrace();
                    
                }
            }
        }
        getLogger().info("图片验证码:"+sRand);
        return getSuccessResultInfo(map);
    }
    
    @RequestMapping(value="/img2",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json")
    public void img2(HttpServletRequest request, HttpServletResponse response) {
        Font mFont = new Font("Default", Font.BOLD, 20);
        int IMG_WIDTH = 60;
        int IMG_HEIGHT = 25;
        int FONT_SIZE = 12;
        String sRand = "";
        FileOutputStream fo=null;
        try {
            BufferedImage bufferedImage = new BufferedImage(IMG_WIDTH, IMG_HEIGHT, BufferedImage.TYPE_INT_RGB);
            Graphics graphics = bufferedImage.getGraphics();
            Random random = new Random();
            graphics.setColor(CommonUtils.getRandColor(250, 255));
            // 填充背景色
            graphics.fillRect(1, 1, IMG_WIDTH - 1,IMG_HEIGHT - 1);
            // 为图形验证码绘制边框
            graphics.setColor(new Color(102, 102, 102));
            graphics.drawRect(0, 0, IMG_WIDTH - 1,
                    IMG_HEIGHT - 1);
            graphics.setColor(CommonUtils.getRandColor(160, 200));
            // 生成随机干扰线
            for (int i = 0; i < 80; i++) {
                int x = random.nextInt(IMG_WIDTH - 1);
                int y = random.nextInt(IMG_HEIGHT - 1);
                int xl = random.nextInt(6) + 1;
                int yl = random.nextInt(12) + 1;
                graphics.drawLine(x, y, x + xl, y + yl);
            }
            graphics.setColor(CommonUtils.getRandColor(160, 200));
            // 设置绘制字符的字体
            graphics.setFont(mFont);
            // 用于保存系统生成的随机字符串 
            for (int i = 0; i < 4; i++) {
                String tmp = CommonUtils.getRandomChar();
                sRand += tmp;
                // 获取随机颜色 80.
                graphics.setColor(new Color(20 + random.nextInt(110), 20 + random
                        .nextInt(110), 20 + random.nextInt(110)));
                // 在图片上绘制系统生成的随机字符
                graphics.drawString(tmp, FONT_SIZE * i + 10, (IMG_HEIGHT+FONT_SIZE)/2);
            }
            response.setContentType("image/*");
            graphics.dispose();
            ImageIO.write(bufferedImage, "JPEG", response.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            if(fo!=null){
                try {
                    fo.close();
                } catch (IOException e) { 
                    e.printStackTrace();
                    
                }
            }
        }
        getLogger().info("图片验证码:"+sRand);
    }
    
    @ResponseBody
    @RequestMapping(value="/img3",method = {RequestMethod.GET,RequestMethod.POST},produces = "application/json")
    public void img3(HttpServletRequest request, HttpServletResponse response) {
        Map<String,String> map = new HashMap<>();
        String filePath="D:\\mytemp\\9642683.jpg";
        map.put("image", getImageStr(filePath));
        ResultInfo<Map<String,String>> resultInfo = getSuccessResultInfo(map);
        String jsonString = JSON.toJSONString(resultInfo);
        try {
            response.getWriter().write(jsonString);
            response.getWriter().flush();
            response.getWriter().close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 图片转化成base64字符串
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     */
    public static String getImageStr(String filePath) {
        //String imgFile = "D:/timg.jpg";// 待处理的图片
        InputStream in = null;
        byte[] data = null;
        // 读取图片字节数组
        try {
            in = new FileInputStream(filePath);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 对字节数组Base64编码
        BASE64Encoder encoder = new BASE64Encoder();
        // 返回Base64编码过的字节数组字符串
        return new String(encoder.encode(data));
    }
    
    /**
     *  base64字符串转化成图片
     *  对字节数组字符串进行Base64解码并生成图片
     */
    @SuppressWarnings("static-access")
    public static String generateImage(String base64ImgStr) { 
        String filePath="";
        // 图像数据为空
        if (base64ImgStr == null){
            filePath=null;
        }
        try {
            // Base64解码
            Base64Decoder decoder = new Base64Decoder();
            byte[] bs = decoder.decode(base64ImgStr.getBytes(),0,base64ImgStr.length());
            for (int i = 0; i < bs.length; ++i) {
                if (bs[i] < 0) {// 调整异常数据
                    bs[i] += 256;
                }
            }

            InputStream sbs = new ByteArrayInputStream(bs);
            File localFile=new File(filePath);
            org.apache.commons.io.FileUtils.copyInputStreamToFile(sbs, localFile);
        } catch (Exception e) {
            filePath=null;
        }
        return filePath;
    }
    
    /**
     * http://localhost:8089/service/file/imageIndex
     */
    @RequestMapping("/imageIndex")
    public String imageIndex() {
        return "image";
    }
    
    /**
     * http://localhost:8089/service/file/i18nIndex
     */
    @RequestMapping("/i18nIndex")
    public String i18nIndex() {
        return "/i18n";
    }
    
    @RequestMapping("/i18nIndex2")
    public String i18nIndex2() {
        return "/i18n2";
    }
    
    /**
     * 获取验证码（jpg版本）
     * @param response
     */
    @RequestMapping(value="getJPGCode",method=RequestMethod.GET)
    public void getJPGCode(HttpServletResponse response,HttpServletRequest request){
        try {
            response.setHeader("Pragma", "No-cache");  
            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
            response.setContentType("image/jpg");  
            /**
             * jgp格式验证码
             * 宽，高，位数。
             */
            Captcha captcha = new SpecCaptcha(146,33,4);
            //输出
            captcha.out(response.getOutputStream());
            //存入Session
            //HttpSession session = request.getSession(true);  
            //session.setAttribute("_code",captcha.text().toLowerCase());  
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(),e, "获取验证码异常：%s",e.getMessage());
        }
    }
    
    /**
     * 获取验证码（Gif版本）
     */
    @RequestMapping(value="getGifCode",method=RequestMethod.GET)
    public void getGifCode(HttpServletResponse response,HttpServletRequest request){
        try {
            response.setHeader("Pragma", "No-cache");  
            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
            response.setContentType("image/gif");  
            /**
             * gif格式动画验证码
             * 宽，高，位数。
             */
            Captcha captcha = new GifCaptcha(146,42,4);
            //输出
            ServletOutputStream out = response.getOutputStream();
            captcha.out(out);
            out.flush();
           //存入Shiro会话session  
            getLogger().info( captcha.text().toLowerCase());
            //TokenManager.setVal2Session(VerifyCodeUtils.V_CODE, captcha.text().toLowerCase());  
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(),e, "获取验证码异常：%s",e.getMessage());
        }
    }
    
    /**
     * 获取验证码
     */
    @RequestMapping(value="getVCode",method=RequestMethod.GET)
    public void getVCode(HttpServletResponse response,HttpServletRequest request){
        try {
            response.setHeader("Pragma", "No-cache");  
            response.setHeader("Cache-Control", "no-cache");  
            response.setDateHeader("Expires", 0);  
            response.setContentType("image/jpg");  
            
            //生成随机字串  
            String verifyCode = VerifyCodeUtils.generateVerifyCode(4);  
            //存入Shiro会话session  
            //TokenManager.setVal2Session(VerifyCodeUtils.V_CODE, verifyCode.toLowerCase());  
            //生成图片  
            int w = 146, h = 33;  
            VerifyCodeUtils.outputImage(w, h, response.getOutputStream(), verifyCode); 
        } catch (Exception e) {
            LoggerUtils.fmtError(getClass(),e, "获取验证码异常：%s",e.getMessage());
        }
    }
    
}