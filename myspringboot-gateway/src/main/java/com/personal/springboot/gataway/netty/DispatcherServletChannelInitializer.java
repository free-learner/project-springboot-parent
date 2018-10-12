package com.personal.springboot.gataway.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpChunkAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

import javax.servlet.ServletException;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

public class DispatcherServletChannelInitializer extends
		ChannelInitializer<SocketChannel> {

	private final DispatcherServlet dispatcherServlet;

	public DispatcherServletChannelInitializer() throws ServletException {

		MockServletContext servletContext = new MockServletContext();
		MockServletConfig servletConfig = new MockServletConfig(servletContext);

		XmlWebApplicationContext wac = new XmlWebApplicationContext();
		wac.setServletContext(servletContext);
		wac.setServletConfig(servletConfig);
		wac.setConfigLocation("classpath:/spring-context-master.xml");

		this.dispatcherServlet = new DispatcherServlet(wac);
		this.dispatcherServlet.init(servletConfig);

	}

	@Override
	public void initChannel(SocketChannel channel) throws Exception {

		ChannelPipeline pipeline = channel.pipeline();
		
		pipeline.addLast("decoder", new HttpRequestDecoder( 4096, 4096, 1024 * 1200));
		pipeline.addLast("aggregator", new HttpChunkAggregator(1024 * 1200));
		
		
		
		pipeline.addLast("encoder", new HttpResponseEncoder());
		pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());
		
		
		pipeline.addLast("handler", new ServletNettyHandler(
				this.dispatcherServlet));

	}

	@Configuration
	@EnableWebMvc
	@ComponentScan(basePackages = "org.springframework.sandbox.mvc")
	static class WebConfig extends WebMvcConfigurerAdapter {
	}

	public DispatcherServlet getDispatcherServlet() {
		return dispatcherServlet;
	}
	
	
	
	
	

}
