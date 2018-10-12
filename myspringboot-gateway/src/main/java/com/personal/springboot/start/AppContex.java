package com.personal.springboot.start;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

public class AppContex {
	
	private static WebApplicationContext wac=null  ;	
	public static void init(DispatcherServlet dispatcherServlet ){
		wac =dispatcherServlet.getWebApplicationContext();
    }
	
	public static Object getBean(String beanName){
		return wac.getBean(beanName);
	}
	

}
