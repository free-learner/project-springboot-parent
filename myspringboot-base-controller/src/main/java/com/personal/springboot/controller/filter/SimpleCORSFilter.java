//package com.personal.springboot.controller.filter;
//
//import java.io.IOException;
//
//import javax.servlet.Filter;
//import javax.servlet.FilterChain;
//import javax.servlet.FilterConfig;
//import javax.servlet.ServletException;
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.annotation.WebFilter;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//
//import com.personal.springboot.common.utils.IpUtils;
//
///**
// * 跨域访问
// * 
// * @Author  LiuBao
// * @Version 2.0
// *   2017年6月16日
// */
//@Component
//@Order(1)
//@WebFilter(filterName = "simpleCORSFilter", urlPatterns = "/*")
//public class SimpleCORSFilter implements Filter {
//    
//    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleCORSFilter.class);
//    
//    @Value("${ajax.cors.ip:*}")
//    private String ajaxCorsIp;
//    
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        LOGGER.warn("init执行了...");
//    }
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        LOGGER.warn("doFilter执行开始...");
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        String origin = request.getHeader("Origin");
//        if(IpUtils.isAjaxCorsIp(ajaxCorsIp, origin)){
//            //接受cookie请使用http://127.0.0.1:8080
//            response.setHeader("Access-Control-Allow-Origin", origin);
//        }
//        //支持post get options delete
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//        //缓存时间
//        response.setHeader("Access-Control-Max-Age", "3600");
//        //接受浏览器Cookie
//        response.setHeader("Access-Control-Allow-Credentials", "true");
//        response.setHeader("Access-Control-Allow-Headers", "userToken,Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With");
//        filterChain.doFilter(servletRequest, servletResponse);
//        LOGGER.warn("doFilter执行结束了...");
//    }
//
//    @Override
//    public void destroy() {
//        LOGGER.warn("destroy执行了...");
//    }
//    
//}