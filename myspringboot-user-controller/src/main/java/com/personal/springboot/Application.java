package com.personal.springboot;

import java.math.BigDecimal;

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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.personal.springboot.common.utils.DateTimeUtil;
import com.personal.springboot.controller.base.MyGlobalController;
import com.personal.springboot.controller.vcode.MyKaptchaServlet;
import com.personal.springboot.qkl.QKLTestMain;
import com.yonghui.supplychain.model.Contract;
import com.yonghui.supplychain.model.Credit;
import com.yonghui.supplychain.model.Expenditure;
import com.yonghui.supplychain.model.Payback;
import com.yonghui.supplychain.model.Register;


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
 * -Xms512m -Xmx1024m -Xmn512m -Djava.awt.headless=true -XX:MaxPermSize=128m
 * java -jar yhloan-front-api-0.0.1-SNAPSHOT.jar --spring.profiles.active=test
 * 
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
        
        String custcd="G0003010";
        
        Register register =new Register();
        register.setCustcd(custcd);
        register.setTaxpayerNo("9135010011115156XY");
        register.setCustName("福建超创**有限公司");
        register.setAddress("福州市台江区**庆里7号3层D172室");
        register.setCorpScale("1");
        register.setOrgtype("1");
        register.setDom("福州市台江区**庆里7号3层D172室");
        register.setDivisioncode("350100");
        register.setBizType("f5124");
        register.setFlag("1");
        
        Credit credit=new Credit();
        credit.setCustcd(custcd);
        credit.setAuditAmt(new BigDecimal("45000.00"));
        credit.setBalAmt(BigDecimal.ZERO);
        credit.setBrcode("1002");
        credit.setBreedCreditNo("100218SX0335");
        credit.setBreedSmallClass("PD0007");
        credit.setCreditType("1");
        credit.setAuditDate(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        credit.setDueDate(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        credit.setUseType("1");
        credit.setFlag("2");
        
        Contract contract=new Contract();
        contract.setCustcd(custcd);
        contract.setContractno("G100D0002017091155");
        contract.setIsdate(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        contract.setTedate(DateTimeUtil.parseDatetime("2019-10-19",DateTimeUtil.DATE_PATTON_1));
        contract.setTerm("0010000000");
        contract.setShortLoanDays(5);
        contract.setTotamt(new BigDecimal(20000000.00));
        contract.setFlag("3");
        
        Expenditure expenditure=new Expenditure();
        expenditure.setCustno(custcd);
        expenditure.setLncino("QYD.201806031605222190");
        expenditure.setClrClass("1");
        expenditure.setGperidays(3);
        expenditure.setLnamt(new BigDecimal(20000000.00));
        expenditure.setLnbal(new BigDecimal(20000000.00));
        expenditure.setLnco("G100D0002017091155");
        expenditure.setLnid("PD0007");
        expenditure.setLnrtnmod("1");
        expenditure.setLnstat("01");
        expenditure.setRtndt("21");
        expenditure.setSidt(DateTimeUtil.parseDatetime("2017-10-19",DateTimeUtil.DATE_PATTON_1));
        expenditure.setEidt(DateTimeUtil.parseDatetime("2019-10-19",DateTimeUtil.DATE_PATTON_1));
        expenditure.setUsdintrate(new BigDecimal(10.00));
        expenditure.setUsdpintrate(new BigDecimal(15.00));
        expenditure.setFlag("4");
        
        Payback payback=new Payback();
        payback.setCustcd(custcd);
        payback.setLncino("ANO.201806031605222190");
        payback.setRtnamt(new BigDecimal("45000.00"));
        //payback.setCreateDate(DateTimeUtil.parseDatetime("2018-10-19",DateTimeUtil.DATE_PATTON_1));
        payback.setCreateDate(DateTimeUtil.currentDate());
        payback.setRtnint(BigDecimal.ZERO);
        payback.setRtnpint(BigDecimal.ZERO);
        payback.setFeeAmt(BigDecimal.ZERO);
        payback.setLoanAmt(payback.getRtnamt().add(payback.getRtnint()).add(payback.getRtnpint()));
        payback.setTransDate(DateTimeUtil.formatDate2Str());
        payback.setFlag("5");
        
        
        QKLTestMain test = new QKLTestMain();
        Gson gson = new GsonBuilder().setDateFormat(DateTimeUtil.DATE_TIME_PATTON_1).create();
        try {
            test.testInsert(gson.toJson(expenditure));
            LOGGER.info("expenditure:{}",gson.toJson(expenditure));
            Thread.sleep(2000);
            test.testInsert(gson.toJson(payback));
            LOGGER.info("payback:{}",gson.toJson(payback));
            Thread.sleep(2000);
            test.testInsert(gson.toJson(register));
            LOGGER.info("register:{}",gson.toJson(register));
            Thread.sleep(2000);
            test.testInsert(gson.toJson(credit));
            LOGGER.info("credit:{}",gson.toJson(credit));
            Thread.sleep(2000);
            test.testInsert(gson.toJson(contract));
            LOGGER.info("contract:{}",gson.toJson(contract));
            Thread.sleep(2000);
            test.testInsert(gson.toJson(payback));
            LOGGER.info("payback:{}",gson.toJson(payback));
//            Thread.sleep(2000);
//            test.testQueryById(); 
//            LOGGER.info("testQueryById执行结束");
            Thread.sleep(2000);
            test.testQueryByDate();
            LOGGER.info("testQueryByDate执行结束");
            Thread.sleep(2000);
        } catch (Exception e) {
            LOGGER.error("数据操作异常!",e);
        }finally {
            System.exit(200);
        }
        
        
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
