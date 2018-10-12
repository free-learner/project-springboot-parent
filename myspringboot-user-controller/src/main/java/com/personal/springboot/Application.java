package com.personal.springboot;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.web.servlet.ErrorPage;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.support.SpringBootServletInitializer;
//import org.springframework.boot.context.embedded.ErrorPage;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;

import com.personal.springboot.controller.base.MyGlobalController;
import com.personal.springboot.controller.vcode.MyKaptchaServlet;


/**
 * SpringBoot启动类
 * 
 * 打包:mvn install -DskipTests -pl myspringboot-user-controller -am
 * mvn clean install -Ptest -DskipTests -pl myspringboot-user-controller -am
 * 
 * clean install -Ptest mvn clean package -Ptest
 * 
 * 当启动脚本中包含参数--spring.profiles.active=test时以启动脚本为主
 * nohup java -Xms2048M -Xmx2048M -Xmn512M -Xss256K -jar yhloan-front-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=test > /dev/null 2>&1 &
 * 简化:
 * java -jar yhloan-front-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
 * 
 * @SpringBootApplication // same as @Configuration @EnableAutoConfiguration @ComponentScan
 * 
 * @author LiuBao
 * @version 2.0 2017年3月27日
 *
 */
@SpringBootApplication
@EnableAutoConfiguration(exclude={  
        DataSourceAutoConfiguration.class,  
            HibernateJpaAutoConfiguration.class, 
            DataSourceTransactionManagerAutoConfiguration.class,  
            })  
//@ComponentScan("com.personal.springboot")
//添加servlet扫描配置
@ServletComponentScan(basePackageClasses={MyKaptchaServlet.class})
public class Application extends SpringBootServletInitializer implements CommandLineRunner,EmbeddedServletContainerCustomizer {
    private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);
    
    @Value("${spring.profiles.active:DEV}")
    private String profiles;

    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        LOGGER.info("Application [ {} ] finished start...",profiles);
    }
    
    /**
     * 设置异常错误处理信息
     */
    @Bean
    public EmbeddedServletContainerCustomizer containerCustomizer() {
        return new EmbeddedServletContainerCustomizer() {
            @Override
            public void customize(ConfigurableEmbeddedServletContainer container) {
                ErrorPage error400Page = new ErrorPage(HttpStatus.BAD_REQUEST, MyGlobalController.ERROR_400);
                ErrorPage error401Page = new ErrorPage(HttpStatus.UNAUTHORIZED, MyGlobalController.ERROR_401);
                ErrorPage error404Page = new ErrorPage(HttpStatus.NOT_FOUND, MyGlobalController.ERROR_404);
                ErrorPage error405Page = new ErrorPage(HttpStatus.METHOD_NOT_ALLOWED, MyGlobalController.ERROR_405);
                ErrorPage error500Page = new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, MyGlobalController.ERROR_500);
                container.addErrorPages(error400Page, error401Page, error404Page, error405Page, error500Page);
                LOGGER.info("设置异常错误处理信息结束...");
            }
        };
    }

    @Override
    public void customize(ConfigurableEmbeddedServletContainer container) {
        LOGGER.info("当前启动环境profiles为:{}", profiles);
    }
    
    /*
    //Tomcat配置 
    @Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory factory = new TomcatEmbeddedServletContainerFactory();
        return factory;
    }*/
    
    /*@Override
    protected final SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
        return application.sources(Application.class);
    }
    
    //Jetty配置
    @Bean
    public EmbeddedServletContainerFactory servletContainer(){
        JettyEmbeddedServletContainerFactory factory = new JettyEmbeddedServletContainerFactory();
        factory.setPort(9999);
        factory.setContextPath("/test");
        return factory;
    }
    
    //Undertow配置
    @Bean
    public EmbeddedServletContainerFactory servletContainer(){
        UndertowEmbeddedServletContainerFactory factory = new UndertowEmbeddedServletContainerFactory();
        factory.setPort(9999);
        factory.setContextPath("/test");
        return factory;
    }
    
    //多容器配置
    @Bean  
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {  
        return new EmbeddedServletContainerCustomizer() {  
            @Override  
            public void customize(ConfigurableEmbeddedServletContainer container) {  
      
                // Port Number  
                if (TomcatEmbeddedServletContainerFactory.class.isAssignableFrom(container.getClass())) {  
                    TomcatEmbeddedServletContainerFactory tomcat =  
                            TomcatEmbeddedServletContainerFactory.class.cast(container);  
                    tomcat.setPort(8081);  
                }  
                if (JettyEmbeddedServletContainerFactory.class.isAssignableFrom(container.getClass())) {  
                    JettyEmbeddedServletContainerFactory jetty =  
                            JettyEmbeddedServletContainerFactory.class.cast(container);  
                    jetty.setPort(8082);  
                }  
                if (UndertowEmbeddedServletContainerFactory.class.isAssignableFrom(container.getClass())) {  
                    UndertowEmbeddedServletContainerFactory undertow =  
                            UndertowEmbeddedServletContainerFactory.class.cast(container);  
                    undertow.setPort(8083);  
                }  
                // Context Path  
                container.setContextPath("/springboot");  
                // Session Timeout  
                container.setSessionTimeout(30, TimeUnit.MINUTES);  
            }  
        };  
    }  
    
    */
    
}
