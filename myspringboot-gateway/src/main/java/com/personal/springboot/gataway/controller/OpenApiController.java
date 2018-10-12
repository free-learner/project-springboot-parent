package com.personal.springboot.gataway.controller;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.personal.springboot.gataway.core.OpenApiAcceptHandler;
import com.personal.springboot.gataway.netty.OpenapiHttpServletResponse;


@Controller
public class OpenApiController {
    @Inject
    OpenApiAcceptHandler acceptHandler;
    
    
    @RequestMapping(value = "/rs-third-part/")
    public void accessThirdPartOpenApi(HttpServletRequest request, OpenapiHttpServletResponse response) {
    			response.setAcceptHandler(acceptHandler);
    	    	this.acceptHandler.acceptRequest(request, response);
    }
    
    
    @RequestMapping(value = "/rs-service/")
    public void accessRsOpenApi(HttpServletRequest request, OpenapiHttpServletResponse response) {
    		response.setAcceptHandler(acceptHandler);
    	   	this.acceptHandler.acceptRequest(request, response);
    }

    @RequestMapping(value = "/ws-service/")
    public void accessWsOpenApi(HttpServletRequest request, OpenapiHttpServletResponse response) {
    			response.setAcceptHandler(acceptHandler);
    	    	this.acceptHandler.acceptRequest(request, response);
    }    
    
   
    
}