package com.personal.springboot.controller.vcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.imageio.ImageIO;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import com.alibaba.fastjson.JSON;
import com.google.code.kaptcha.Producer;
import com.google.code.kaptcha.servlet.KaptchaServlet;
import com.google.code.kaptcha.util.Config;
import com.personal.springboot.common.ApplicationContextUtils;

/**
 * 覆写 KaptchaServlet
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年6月26日
 */
@SuppressWarnings({"serial"})
@WebServlet(urlPatterns="/images/kaptcha.jpg", description="验证码servlet",initParams = {  
        @WebInitParam(name= "kaptcha.border", value="no"),  
        @WebInitParam(name= "user.name", value= "liubao"),  
        })
public class MyKaptchaServlet extends KaptchaServlet{
    private static final Logger LOGGER = LoggerFactory.getLogger(MyKaptchaServlet.class);
	private Properties props = new Properties();
	private Producer kaptchaProducer = null;
    private String sessionKeyValue = null;
	private String sessionKeyDateValue = null;
	
	@Lazy
	@Autowired(required=false)
	private KaptchaProperties kaptchaProperties;

	@Override
	public void init(ServletConfig conf) throws ServletException
	{
		super.init(conf);
		ImageIO.setUseCache(false);
		Enumeration<?> initParams = conf.getInitParameterNames();
		while (initParams.hasMoreElements())
		{
			String key = (String) initParams.nextElement();
			String value = conf.getInitParameter(key);
			this.props.put(key, value);
			LOGGER.info("key===>{},value===>{}",key,value);
		}
		
		if(kaptchaProperties==null){
		    kaptchaProperties=ApplicationContextUtils.getBean(KaptchaProperties.class);
		}
		Map<String, String> initMap=new TreeMap<>();
		//initMap.put("kaptcha.urlMappings",kaptchaProperties.getUrlMappings());
		initMap.put("kaptcha.border", kaptchaProperties.getBorder());
	    initMap.put("kaptcha.border.color",kaptchaProperties.getBorderColor());
	    initMap.put("kaptcha.textproducer.font.color",kaptchaProperties.getTextproducerFontColor());
	    initMap.put("kaptcha.image.width",kaptchaProperties.getImageWidth());
	    initMap.put("kaptcha.image.height",kaptchaProperties.getImageHeight());
	    initMap.put("kaptcha.background.clear.from",kaptchaProperties.getBackgroundClearFrom());
	    initMap.put("kaptcha.background.clear.to",kaptchaProperties.getBackgroundClearTo());
	    initMap.put("kaptcha.textproducer.font.size",kaptchaProperties.getTextproducerFontSize());
	    initMap.put("kaptcha.textproducer.char.length",kaptchaProperties.getTextproducerCharLength());
	    initMap.put("kaptcha.textproducer.char.space",kaptchaProperties.getTextproducerCharSpace());
	    initMap.put("kaptcha.textproducer.font.names",kaptchaProperties.getTextproducerFontNames());
	    initMap.put("kaptcha.noise.impl",kaptchaProperties.getNoiseImpl());
	    initMap.put("kaptcha.obscurificator.impl",kaptchaProperties.getObscurificatorImpl());
	    initMap.put("kaptcha.border.thickness",kaptchaProperties.getBorderThickness());
	    initMap.put("kaptcha.noise.color",kaptchaProperties.getNoiseColor());
		props.putAll(initMap);
		LOGGER.info("当前init参数信息为:{}",JSON.toJSONString(initMap));
		
		Config config = new Config(this.props);
		this.kaptchaProducer = config.getProducerImpl();
		this.sessionKeyValue = config.getSessionKey();
		this.sessionKeyDateValue = config.getSessionDate();
	}

	/** */
	@Override
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException
	{
		// Set standard HTTP/1.1 no-cache headers.
		resp.setHeader("Cache-Control", "no-store, no-cache");
		// return a jpeg
		resp.setContentType("image/jpeg");
		// create the text for the image
		String capText = this.kaptchaProducer.createText();
		// create the image with the text
		BufferedImage bi = this.kaptchaProducer.createImage(capText);
		ServletOutputStream out = resp.getOutputStream();
		
		// fixes issue #69: set the attributes after we write the image in case the image writing fails.
		// store the text in the session
		req.getSession().setAttribute(this.sessionKeyValue, capText);
		// store the date in the session so that it can be compared
		// against to make sure someone hasn't taken too long to enter
		// their kaptcha
		req.getSession().setAttribute(this.sessionKeyDateValue, new Date());
		
		/**
		 * 在最后再进行数据输出,否则提前输出的话,会抛异常信息
		 * Cannot create a session after the response has been committed
		 * 之所以会出现此类问题是因为我们在Response输出响应后才创建Session的。
		 * 因为那时候服务器已经将数据发送到客户端了，即：就无法发送Session ID 了
		 */
		// write the data out
		ImageIO.write(bi, "jpg", out);
		
	}
}
