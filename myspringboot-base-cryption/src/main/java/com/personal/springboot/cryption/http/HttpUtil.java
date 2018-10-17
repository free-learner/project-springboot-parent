package com.personal.springboot.cryption.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

public class HttpUtil
{
  private static Logger logger = Logger.getLogger(HttpUtil.class);

  public static String doGet(String url)
  {
    try
    {
      HttpClient client = new DefaultHttpClient();

      HttpGet request = new HttpGet(url);
      HttpResponse response = client.execute(request);

      if (response.getStatusLine().getStatusCode() == 200)
      {
        return EntityUtils.toString(response.getEntity());
      }

    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return null;
  }

  public static String doPost(String url, Map params)
  {
    BufferedReader in = null;
    try
    {
      HttpClient client = new DefaultHttpClient();

      HttpPost request = new HttpPost();
      request.setURI(new URI(url));

      List nvps = new ArrayList();
      for (Iterator iter = params.keySet().iterator(); iter.hasNext(); ) {
        String name = (String)iter.next();
        String value = String.valueOf(params.get(name));
        nvps.add(new BasicNameValuePair(name, value));
      }

      request.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));

      HttpResponse response = client.execute(request);
      int code = response.getStatusLine().getStatusCode();
      if (code == 200)
      {
        in = new BufferedReader(new InputStreamReader(response.getEntity()
          .getContent(), "utf-8"));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        String NL = System.getProperty("line.separator");
        while ((line = in.readLine()) != null) {
          sb.append(line + NL);
        }
        in.close();
        return sb.toString();
      }

      System.out.println("状态码：" + code);
      return null;
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return null;
  }

  public static String doPost(String url, String params)
    throws Exception
  {
    CloseableHttpClient httpclient = HttpClients.createDefault();
    HttpPost httpPost = new HttpPost(url);
    httpPost.setHeader("Accept", "application/json");
    httpPost.setHeader("Content-Type", "application/json");
    String charSet = "UTF-8";
    StringEntity entity = new StringEntity(params, charSet);
    httpPost.setEntity(entity);
    CloseableHttpResponse response = null;
    try
    {
      response = httpclient.execute(httpPost);
      StatusLine status = response.getStatusLine();
      int state = status.getStatusCode();
      if (state == 200) {
        HttpEntity responseEntity = response.getEntity();
        String jsonString = EntityUtils.toString(responseEntity);
        return jsonString;
      }

      logger.error("请求返回:" + state + "(" + url + ")");
    }
    finally
    {
      if (response != null)
        try {
          response.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      try
      {
        httpclient.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
    return null;
  }
}