package com.personal.springboot.gataway.netty;

import org.springframework.mock.web.MockHttpServletResponse;

import com.personal.springboot.gataway.core.OpenApiAcceptHandler;

import io.netty.channel.ChannelHandlerContext;

public class OpenapiHttpServletResponse  extends MockHttpServletResponse{
	
	private ChannelHandlerContext ctx;
	private OpenApiAcceptHandler acceptHandler;

	public OpenapiHttpServletResponse(ChannelHandlerContext ctx) {
		super();
		this.ctx = ctx;
	}

	
	
	
	public OpenApiAcceptHandler getAcceptHandler() {
		return acceptHandler;
	}




	public void setAcceptHandler(OpenApiAcceptHandler acceptHandler) {
		this.acceptHandler = acceptHandler;
	}




	public ChannelHandlerContext getCtx() {
		return ctx;
	}

	public void setCtx(ChannelHandlerContext ctx) {
		this.ctx = ctx;
	}
	
	

}
