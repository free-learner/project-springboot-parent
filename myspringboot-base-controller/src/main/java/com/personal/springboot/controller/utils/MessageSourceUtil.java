package com.personal.springboot.controller.utils;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.RequestContextUtils;

/**
 * 提示文案信息封装类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年3月29日
 */
@Component
public class MessageSourceUtil {

	//@Resource
    @Autowired(required=false)
	private MessageSource messageSource;

	/**
	 * 获取code对应的message
	 */
	public String getMessage(String code) {
		return getMessage(code, null);
	}

	/**
	 *
	 * 获取code对应的message
	 *
	 * @param code  ：对应messages配置的key.
	 * @param args  : 参数.
	 */
	public String getMessage(String code, Object[] args) {
		return getMessage(code, args, "");
	}

	/**
	 * 这里使用比较方便的方法，不依赖HttpServletRequest.
	 *
	 * @param code ：对应messages配置的key.
	 * @param args : 参数.
	 * @param defaultMessage  : 没有设置key的时候的默认值.
	 */
	public String getMessage(String code, Object[] args, String defaultMessage) {
		Locale locale = LocaleContextHolder.getLocale();
		//locale=Locale.US;
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}

	public String getMessage(String code, Object[] args, String defaultMessage, HttpServletRequest request) {
		Locale locale = RequestContextUtils.getLocale(request);
		return messageSource.getMessage(code, args, defaultMessage, locale);
	}

}
