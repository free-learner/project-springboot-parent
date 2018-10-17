package com.personal.springboot.cryption.http;

import java.util.HashSet;
import java.util.Set;

/**
 * HTTP请求常量配置类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年8月27日
 */
public class CommHttpConstants {

    private static final String HTTP = "http";
    private static final String HTTPS = "https";
    public static final String UTF_8 = "UTF-8";
    public static final String HEADER_COOKIE_KEY = "cookie";
    public static final String HEADER_HOST_KEY = "host";

    public static final String HEADER_X_REAL_IP_KEY = "X-Real-IP";
    public static final String AUTH_HEADER = "Auth-Header";
    public static final String REQ_BEAN_LOG_KEY = "REQUEST_BEAN_LOG";

    public static Set<String> respSet = new HashSet<String>();
    static {
        respSet.add(HEADER_COOKIE_KEY);
        respSet.add(HEADER_HOST_KEY);
    }

    private CommHttpConstants() {
    }
    
    public static enum  Protocol{
        ALL("all", "HTTP+HTTPS协议请求" ),                     
        HTTP(CommHttpConstants.HTTP, "HTTP协议请求" ),                     
        HTTPS(CommHttpConstants.HTTPS, "HTTPS协议请求" );    
        
        private String key;
        private String value;
        
        private Protocol(String key, String value) {
            this.key = key;
            this.value = value;
        }
        public String getKey() {
            return key;
        }
        public void setKey(String key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        
        public static Protocol getByKey(String key) {
            Protocol[] os = Protocol.values();
            for (int i = 0; i < os.length; i++) {
                if (os[i].getKey() == key) {
                    return os[i];
                }
            }
            return null;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }
    
    public static  enum  RequestMethod{
        GET,  POST
    }

}
