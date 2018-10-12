package com.personal.springboot.controller.conf;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.personal.springboot.controller.interceptor.TimeInterceptor;
import com.personal.springboot.controller.utils.NullValueFilter;

/**
 * WebMVC配置定义信息加载
 * 
 * @author LiuBao
 * @version 2.0 
 * 2017年3月27日
 */
@Configuration
public class MyBaseWebConfiguration extends WebMvcConfigurationSupport {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyBaseWebConfiguration.class);
    
    @Value("${include.path.patterns}")
    private String  includePathPatterns ;
    
    @Value("${exclude.path.patterns}")
    private String  excludePathPatterns ;
	
	@Override
    public void addInterceptors(InterceptorRegistry registry) {
	    String[]  includeUrlArray = new String[]{};
	    if(StringUtils.isNoneBlank(includePathPatterns)){
	        includeUrlArray = includePathPatterns.split(",");
	    }
	    String[]  excludeUrlArray = new String[]{};
	    if(StringUtils.isNoneBlank(excludePathPatterns)){
	        excludeUrlArray = excludePathPatterns.split(",");
	    }
	    InterceptorRegistration timeInteceptor = registry.addInterceptor(initTimeInteceptor());
	    if(ArrayUtils.isNotEmpty(includeUrlArray)){
	        timeInteceptor.addPathPatterns(includeUrlArray);
	    }
	    if(ArrayUtils.isNotEmpty(excludeUrlArray)){
	        timeInteceptor.excludePathPatterns(excludeUrlArray);
	    }
//	    InterceptorRegistration loginAuthInterceptor = registry.addInterceptor(initLoginAuthInterceptor());
//	    if(ArrayUtils.isNotEmpty(includeUrlArray)){
//	        loginAuthInterceptor.addPathPatterns(includeUrlArray);
//	    }
//	    if(ArrayUtils.isNotEmpty(excludeUrlArray)){
//	        loginAuthInterceptor.excludePathPatterns(excludeUrlArray);
//	    }
        LOGGER.warn("MyBaseWebConfiguration初始化执行了...");
        super.addInterceptors(registry);
    }

    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        converters.add(customFastJsonHttpMessageConverter());
        //converters.add(customMappingJackson2HttpMessageConverter());
        super.addDefaultHttpMessageConverters(converters);
    }
    
	@Bean
    public MappingJackson2HttpMessageConverter customMappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter jsonConverter=new MappingJackson2HttpMessageConverter();
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, false);
        objectMapper.configure(SerializationFeature.WRITE_NULL_MAP_VALUES, true);
        jsonConverter.setObjectMapper(objectMapper);
        List<MediaType> supportedMediaTypes=new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        return jsonConverter;
    }
	
    @Bean
    @SuppressWarnings("deprecation")
    public FastJsonHttpMessageConverter customFastJsonHttpMessageConverter() {
        FastJsonHttpMessageConverter jsonConverter = new FastJsonHttpMessageConverter() {
            @Override
            protected void writeInternal(Object obj, HttpOutputMessage outputMessage) 
                    throws IOException, HttpMessageNotWritableException {
                //super.writeInternal(obj,outputMessage);
                OutputStream out = outputMessage.getBody();
                String text = JSON.toJSONString(obj,new NullValueFilter(), super.getFeatures());
                byte[] bytes = text.getBytes(super.getCharset());
                out.write(bytes);
            }
        };
        SerializerFeature[] features = new SerializerFeature[] { /*SerializerFeature.WriteMapNullValue,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero, SerializerFeature.WriteNullBooleanAsFalse,*/
                SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat };
        jsonConverter.setFeatures(features);
        jsonConverter.setCharset(Charset.forName("UTF-8"));
        List<MediaType> supportedMediaTypes=new ArrayList<MediaType>();
        supportedMediaTypes.add(MediaType.APPLICATION_JSON);
        supportedMediaTypes.add(MediaType.APPLICATION_FORM_URLENCODED);
        //supportedMediaTypes.add(MediaType.APPLICATION_XML);
        //supportedMediaTypes.add(MediaType.APPLICATION_XHTML_XML);
        //supportedMediaTypes.add(MediaType.MULTIPART_FORM_DATA);
        //supportedMediaTypes.add(MediaType.APPLICATION_OCTET_STREAM);
        jsonConverter.setSupportedMediaTypes(supportedMediaTypes);
        return jsonConverter;
    }
	
//	@Bean
//    public LoginAuthInterceptor initLoginAuthInterceptor() {
//        return new LoginAuthInterceptor();
//    }
	
	@Bean
	public TimeInterceptor initTimeInteceptor() {
	    return new TimeInterceptor();
	}
	
}
