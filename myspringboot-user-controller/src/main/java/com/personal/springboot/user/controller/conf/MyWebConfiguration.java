package com.personal.springboot.user.controller.conf;

import java.io.IOException;

import javax.servlet.MultipartConfigElement;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import com.alibaba.druid.support.http.StatViewServlet;
import com.alibaba.druid.support.http.WebStatFilter;
import com.personal.springboot.controller.conf.MyBaseWebConfiguration;
import com.personal.springboot.controller.vcode.KaptchaProperties;
import com.personal.springboot.controller.vcode.MyKaptchaServlet;
import com.personal.springboot.user.controller.interceptor.LogInterceptor;
import com.personal.springboot.user.controller.interceptor.LoginAuthInterceptor;

import freemarker.template.TemplateException;

/**
 * WebMVC配置定义信息加载
 * 
 * @author LiuBao
 * @version 2.0 
 * 2017年3月27日
 */
@Configuration
public class MyWebConfiguration extends MyBaseWebConfiguration /*WebMvcConfigurerAdapter*/ {
    private static final Logger LOGGER = LoggerFactory.getLogger(MyWebConfiguration.class);
    
    @Value("${include.path.patterns}")
    private String  includePathPatterns ;
    
    @Value("${exclude.path.patterns}")
    private String  excludePathPatterns ;
    
    @Autowired(required=false)
    private KaptchaProperties kaptchaProperties;
    
    @Value("${druid.login.username}")
    private String loginUsername;
    
    @Value("${druid.login.password}")
    private String loginPassword;
    
    @Value("${druid.parameter.exclusions}")
    private String parameterExclusions;
	
	@SuppressWarnings("unused")
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
	    /*InterceptorRegistration loginAuthInterceptor = registry.addInterceptor(initLoginAuthInterceptor());
	    if(ArrayUtils.isNotEmpty(includeUrlArray)){
	        loginAuthInterceptor.addPathPatterns(includeUrlArray);
	    }
	    if(ArrayUtils.isNotEmpty(excludeUrlArray)){
	        loginAuthInterceptor.excludePathPatterns(excludeUrlArray);
	    }
	    */
	    /*InterceptorRegistration logInterceptor = registry.addInterceptor(initLogInterceptor());
	    if(ArrayUtils.isNotEmpty(includeUrlArray)){
	        logInterceptor.addPathPatterns(includeUrlArray);
        }
        if(ArrayUtils.isNotEmpty(excludeUrlArray)){
            logInterceptor.excludePathPatterns(excludeUrlArray);
        }
        LOGGER.warn("MyWebAppConfigurer初始化执行了...");
        super.addInterceptors(registry);*/
    }

	@Bean
    public LoginAuthInterceptor initLoginAuthInterceptor() {
        return new LoginAuthInterceptor();
    }
	
	@Bean
	public LogInterceptor initLogInterceptor() {
	    return new LogInterceptor();
	}
	
	/**
	 * setLocation可以设置其默认上传文件时候的临时文件目录,否则会在/tmp目录下面生成
	 * @return
	 */
	@Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(1024L * 1024L*20);
        factory.setLocation("/usr/local/tmp");
        return factory.createMultipartConfig();
    }
	
	@Bean(name="jspViewResolver")
    public ViewResolver getJspViewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setViewClass(JstlView.class);
        //resolver.setPrefix("classpath:templates/jsp");
        resolver.setPrefix("/jsp/");
        resolver.setSuffix(".jsp");
        resolver.setOrder(1);
        return resolver;
    }
	
	@Bean
	public FreeMarkerViewResolver getFreeMarkerViewResolver() {
	    FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
	    resolver.setCache(true);
	    //resolver.setPrefix("classpath:templates/");
	    resolver.setPrefix("");
	    resolver.setSuffix(".ftl");
	    resolver.setContentType("text/html;charset=UTF-8");
	    resolver.setOrder(0);
	    return resolver;
	}
	
	@Bean
	public FreeMarkerConfigurer getFreeMarkerConfigurer() throws IOException, TemplateException {
	    FreeMarkerConfigurationFactory factory = new FreeMarkerConfigurationFactory();
	    factory.setTemplateLoaderPath("classpath:templates");
	    factory.setDefaultEncoding("UTF-8");
	    FreeMarkerConfigurer result=new FreeMarkerConfigurer();
	    result.setConfiguration(factory.createConfiguration());
	    return result;
	}
	
	/**
	 * servlet初始化操作
	 */
	@Bean
    public ServletRegistrationBean initMyKaptchaServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new MyKaptchaServlet());
        reg.addUrlMappings(kaptchaProperties.getUrlMappings2());
        //设置是否有边框 -->
        reg.addInitParameter("kaptcha.border", kaptchaProperties.getBorder());
        //设置边框颜色
        reg.addInitParameter("kaptcha.border.color", kaptchaProperties.getBorderColor());
        //设置验证码宽度
        reg.addInitParameter("kaptcha.image.width", kaptchaProperties.getImageWidth());
        //设置验证码高度
        reg.addInitParameter("kaptcha.image.height", kaptchaProperties.getImageHeight());
        //设置字体颜色
        reg.addInitParameter("kaptcha.textproducer.font.color", kaptchaProperties.getTextproducerFontColor());
        //图片的背景颜色渐变
        reg.addInitParameter("kaptcha.background.clear.from", kaptchaProperties.getBackgroundClearFrom());
        reg.addInitParameter("kaptcha.background.clear.to", kaptchaProperties.getBackgroundClearTo());
        //设置字体大小
        reg.addInitParameter("kaptcha.textproducer.font.size", kaptchaProperties.getTextproducerFontSize());
        //设置字体个数
        reg.addInitParameter("kaptcha.textproducer.char.length", kaptchaProperties.getTextproducerCharLength());
        //文本之间的间距
        reg.addInitParameter("kaptcha.textproducer.char.space", kaptchaProperties.getTextproducerCharSpace());
        //设置字体样式
        reg.addInitParameter("kaptcha.textproducer.font.names", kaptchaProperties.getTextproducerFontNames());
        //设置干扰线样式
        reg.addInitParameter("kaptcha.noise.impl", kaptchaProperties.getNoiseImpl());
        //设置图片样式
        reg.addInitParameter("kaptcha.obscurificator.impl", kaptchaProperties.getObscurificatorImpl());
        reg.addInitParameter("kaptcha.border.thickness", kaptchaProperties.getBorderThickness());
        reg.addInitParameter("kaptcha.noise.color", kaptchaProperties.getNoiseColor());
        LOGGER.warn("图形验证码servlet初始化完成了...");
        return reg;
    }
	
	@Bean
    public ServletRegistrationBean initDruidServlet() {
        ServletRegistrationBean reg = new ServletRegistrationBean();
        reg.setServlet(new StatViewServlet());
        reg.addUrlMappings("/druid/*");
        reg.addInitParameter("loginUsername", loginUsername);
        reg.addInitParameter("loginPassword", loginPassword);
        //reg.addInitParameter("allow", "127.0.0.1");
        //reg.addInitParameter("deny", "10.67.26.112");
        return reg;
    }

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        filterRegistrationBean.setFilter(new WebStatFilter());
        filterRegistrationBean.addUrlPatterns("/*");
        filterRegistrationBean.addInitParameter("exclusions", parameterExclusions);
        filterRegistrationBean.addInitParameter("profileEnable", "true");
        filterRegistrationBean.addInitParameter("principalCookieName", "USER_COOKIE");
        filterRegistrationBean.addInitParameter("principalSessionName", "USER_SESSION");
        return filterRegistrationBean;
    }
	
}
