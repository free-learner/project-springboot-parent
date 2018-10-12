package com.personal.springboot.start;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.personal.springboot.gataway.dao.entity.ApiApp;
import com.personal.springboot.gataway.dao.entity.ApiInterface;
import com.personal.springboot.gataway.dao.entity.ApiSubInterface;
import com.personal.springboot.gataway.netty.DispatcherServletChannelInitializer;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;


public class StartServer {
	private static final Logger logger = LoggerFactory.getLogger(StartServer.class);
	
	public static Map<String, ApiApp> apiAppMap =new HashMap<String, ApiApp>();
	public static Map<String, ApiInterface> apiInterfaceMap =new HashMap<String, ApiInterface>();
	public static Map<String, ApiSubInterface> apiSubInterface =new HashMap<String, ApiSubInterface>();
	
	public static boolean data_init_false=false;

	private final int port;

	public StartServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		ServerBootstrap server = new ServerBootstrap();
		try {
			DispatcherServletChannelInitializer dsChannelInitializer=new DispatcherServletChannelInitializer();
			server.group(new NioEventLoopGroup(), new NioEventLoopGroup())
					.channel(NioServerSocketChannel.class)					
					.localAddress(port)					
					.childHandler(dsChannelInitializer);	
			
			AppContex.init(dsChannelInitializer.getDispatcherServlet());
			
			// redis 自动读取进程启动
//			LoadObjectTask LoadObjectTask=(LoadObjectTask)AppContex.getBean("loadObjectTask");
//			LoadObjectTask.load();
			
			
			logger.info("服务启动完成~~~~监听端口："+port);
			server.bind().sync().channel().closeFuture().sync();	
						
		}catch(Throwable t){
			logger.error("",t);
		}finally {
			server.shutdown();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8080;
		}
		new StartServer(port).run();
	}
	
}
