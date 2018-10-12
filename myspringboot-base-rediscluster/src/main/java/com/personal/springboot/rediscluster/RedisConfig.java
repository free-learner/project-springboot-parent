//package com.personal.springboot.rediscluster;
//
//import java.lang.reflect.InvocationTargetException;
//import java.lang.reflect.Method;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.cache.CacheManager;
//import org.springframework.cache.annotation.CachingConfigurerSupport;
//import org.springframework.cache.annotation.EnableCaching;
//import org.springframework.cache.interceptor.KeyGenerator;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.StringRedisTemplate;
//import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
//
//import com.fasterxml.jackson.annotation.JsonAutoDetect;
//import com.fasterxml.jackson.annotation.PropertyAccessor;
//import com.fasterxml.jackson.databind.ObjectMapper;
//
//
///**
// * 缓存模版注解类
// * 
// * @Author  LiuBao
// * @Version 2.0
// *   2017年6月12日
// */
////@ConfigurationProperties("application.properties")
//@ConfigurationProperties(prefix = "redis.cache")
//@Configuration
//@EnableCaching
//public class RedisConfig extends CachingConfigurerSupport {
//
//     @Value("${spring.redis.hostName}")
//        private String hostName;
//        @Value("${spring.redis.port}")
//        private Integer port;
//        
//        @Bean
//        public RedisConnectionFactory redisConnectionFactory() {
//            JedisConnectionFactory cf = new JedisConnectionFactory();  
//            cf.setHostName(hostName);  
//            cf.setPort(port); 
//            cf.afterPropertiesSet();  
//            return cf;  
//        }
//    //配置key的生成
//    @Bean
//    public KeyGenerator simpleKey(){
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params) {
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName()+":");
//                for (Object obj : params) {
//                    sb.append(obj.toString());
//                }
//                return sb.toString();
//            }
//        };
//    }
//  //配置key的生成
//    @Bean
//    public KeyGenerator objectId(){
//        return new KeyGenerator() {
//            @Override
//            public Object generate(Object target, Method method, Object... params){
//                StringBuilder sb = new StringBuilder();
//                sb.append(target.getClass().getName()+":");
//                try {
//                    sb.append(params[0].getClass().getMethod("getId", null).invoke(params[0], null).toString());
//                }catch (NoSuchMethodException no){
//                    no.printStackTrace();
//                }catch(IllegalAccessException il){
//                    il.printStackTrace();
//                }catch(InvocationTargetException iv){
//                    iv.printStackTrace();
//                }
//                return sb.toString();
//            }
//        };
//    }
//    //配置注解
//    @Bean
//    public CacheManager cacheManager(@SuppressWarnings("rawtypes") RedisTemplate redisTemplate) {
//        RedisCacheManager manager = new RedisCacheManager(redisTemplate);
//        manager.setDefaultExpiration(43200);//12小时
//        return manager;
//    }
//    //对于复杂的属性仍然使用RedisTemplate
//    @Bean
//    public RedisTemplate<String, String> redisTemplate(
//            RedisConnectionFactory factory) {
//        StringRedisTemplate template = new StringRedisTemplate(factory);
//        Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
//        return template;
//    }
//
//}