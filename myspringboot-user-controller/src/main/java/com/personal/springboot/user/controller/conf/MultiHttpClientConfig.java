package com.personal.springboot.user.controller.conf;

import java.security.SecureRandom;

import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.httpclient.params.HttpClientParams;
import org.apache.commons.httpclient.params.HttpConnectionManagerParams;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.ConnectionConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.web.client.RestTemplate;

import yhjr.common.rsa.exception.AbsErrorCodeConstant;
import yhjr.common.rsa.exception.HttpServiceException;
import yhjr.common.rsa.utils.http.CommHttpConstants.Protocol;
import yhjr.common.rsa.utils.http.MutiHttpClientUtil;
import yhjr.common.rsa.utils.http.MutiHttpClientUtil.TrustAnyTrustManager;

/**
 * MultiThreadedHttpConnectionManager配置文件
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月24日
 */
@SuppressWarnings("deprecation")
@Configuration
public class MultiHttpClientConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(MultiHttpClientConfig.class);

    @Value("${http.client.maxTotalConnections}")
    private int maxTotalConnections;
    @Value("${http.client.defaultMaxConnectionsPerHost}")
    private int maxConnectionsPerHost;
    @Value("${http.client.soTimeout}")
    private int soTimeout;
    @Value("${http.client.connectionTimeout}")
    private int connectionTimeout;
    @Value("${http.client.staleCheckingEnabled}")
    private boolean staleCheckingEnabled;
    @Value("${http.client.connectionRequestTimeout:500}")
    private int connectionRequestTimeout;
    
    @Bean
//    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    @Bean(name="mutiHttpClientUtil")
    public MutiHttpClientUtil initMutiHttpClientUtil() {
        HttpClientParams httpClientParams = new HttpClientParams();
        httpClientParams.setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler(0, false));
        httpClientParams.setContentCharset("UTF-8");
        httpClientParams.setSoTimeout(soTimeout);
        
        HttpClient client=new HttpClient();
        client.setParams(httpClientParams);
        client.setHttpConnectionManager(initMultiThreadedHttpConnectionManager());
        
         MutiHttpClientUtil mutiHttpClientUtil=new MutiHttpClientUtil();
         mutiHttpClientUtil.setClient(client);
         mutiHttpClientUtil.addCloseableHttpClient(Protocol.HTTP, initCloseableHttpClient());
         mutiHttpClientUtil.addCloseableHttpClient(Protocol.HTTPS, initCloseableHttpsClient());
         LOGGER.info("MutiHttpClientUtil初始化结束了。。。");
         return mutiHttpClientUtil;
    }
    
    @Bean(name="multiThreadedHttpConnectionManager")
    public MultiThreadedHttpConnectionManager initMultiThreadedHttpConnectionManager() {
        HttpConnectionManagerParams params=new HttpConnectionManagerParams();
        params.setDefaultMaxConnectionsPerHost(maxConnectionsPerHost);
        params.setMaxTotalConnections(maxTotalConnections);
        params.setStaleCheckingEnabled(staleCheckingEnabled);
        params.setConnectionTimeout(connectionTimeout);
        params.setSoTimeout(soTimeout);
        MultiThreadedHttpConnectionManager manager = new MultiThreadedHttpConnectionManager();
        manager.setParams(params);
        LOGGER.info("MultiThreadedHttpConnectionManager初始化结束了。。。");
        return manager;
    }
    
    @Scope("prototype")
    @Bean(name = "closeableHttpClient")
    public CloseableHttpClient initCloseableHttpClient() {
        final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(maxTotalConnections);
        connectionManager.setDefaultMaxPerRoute(maxConnectionsPerHost);
        connectionManager.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);
        connectionManager.setDefaultSocketConfig(SocketConfig.DEFAULT);

        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.setMaxConnTotal(maxTotalConnections);
        httpClientBuilder.setMaxConnPerRoute(maxConnectionsPerHost);

        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setStaleConnectionCheckEnabled(staleCheckingEnabled)
                .build();
        
        CloseableHttpClient closeableHttpClient = httpClientBuilder.setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager).build();
        new MutiHttpClientUtil.IdleConnectionEvictor(connectionManager);
        LOGGER.info("initCloseableHttpClient初始化结束了。。。");
        return closeableHttpClient;
    }
    
    @Scope("prototype")
    @Bean(name = "closeableHttpsClient")
    public CloseableHttpClient initCloseableHttpsClient() {
        final RequestConfig requestConfig = RequestConfig.custom()
                .setConnectionRequestTimeout(connectionRequestTimeout)
                .setSocketTimeout(soTimeout)
                .setConnectTimeout(connectionTimeout)
                .setStaleConnectionCheckEnabled(staleCheckingEnabled)
                .build();
        
        SSLContext ctx = null;
        TrustManager[] allTrustCerts = new TrustManager[] { new TrustAnyTrustManager()};
        try {
            ctx = SSLContext.getInstance("SSL");
            ctx.init(new KeyManager[0], allTrustCerts, new SecureRandom());
        } catch (Exception e) {
            throw new HttpServiceException(AbsErrorCodeConstant.ERROR_CODE_91006, e);
        }
        final SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(ctx,
                SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
      final  ConnectionSocketFactory plainSocketFactory = PlainConnectionSocketFactory.getSocketFactory();
      final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
              .register(Protocol.HTTP.getKey(), plainSocketFactory)
              .register(Protocol.HTTPS.getKey(), sslConnectionSocketFactory).build();
      final PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager(registry);
      connectionManager.setMaxTotal(maxTotalConnections);
      connectionManager.setDefaultMaxPerRoute(maxConnectionsPerHost);
      connectionManager.setDefaultConnectionConfig(ConnectionConfig.DEFAULT);
      connectionManager.setDefaultSocketConfig(SocketConfig.DEFAULT);
      
        final HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(connectionManager);
        httpClientBuilder.setMaxConnTotal(maxTotalConnections);
        httpClientBuilder.setMaxConnPerRoute(maxConnectionsPerHost);
        CloseableHttpClient closeableHttpsClient = httpClientBuilder
                .setDefaultRequestConfig(requestConfig)
                .setConnectionManager(connectionManager)
                .setSSLSocketFactory(sslConnectionSocketFactory)
                .setHostnameVerifier(SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER)
                .build();
         new MutiHttpClientUtil.IdleConnectionEvictor(connectionManager);
        LOGGER.info("initCloseableHttpsClient初始化结束了。。。");
        return closeableHttpsClient;
    }
    
}