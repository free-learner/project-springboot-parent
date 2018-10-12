//package com.personal.springboot.user.controller.conf;
//
//import static springfox.documentation.builders.PathSelectors.regex;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import springfox.documentation.builders.ApiInfoBuilder;
//import springfox.documentation.builders.RequestHandlerSelectors;
//import springfox.documentation.service.ApiInfo;
//import springfox.documentation.service.Contact;
//import springfox.documentation.spi.DocumentationType;
//import springfox.documentation.spring.web.plugins.Docket;
//import springfox.documentation.swagger2.annotations.EnableSwagger2;
//
///**
// * SwaggerConfig
// */
//@Configuration
//@EnableSwagger2
//public class Swagger2Configuration {
//
//	@Bean
//	public Docket accessToken() {
//		return new Docket(DocumentationType.SWAGGER_2).groupName("userApi")// 定义组
//				.select() // 选择那些路径和api会生成document
//				.apis(RequestHandlerSelectors.basePackage("com.personal.springboot.user.controller.user")) // 拦截的包路径
//				.paths(regex("/loanUser/.*"))
//				.paths(regex("/history/.*"))// 拦截的接口路径
//				.build() // 创建
//				.apiInfo(apiInfo()); // 配置说明
//	}
//
//	private ApiInfo apiInfo() {
//		return new ApiInfoBuilder()//
//				.title("永辉Swagger2标题")// 标题
//				.description("永辉Swagger2描述")// 描述
//				.termsOfServiceUrl("https://www.baidu.com")//
//				.contact(new Contact("LiuBao", "https://www.baidu.com", "18621668604@163.com"))// 联系
//				.license("Apache License Version 2.0")// 开源协议
//				.licenseUrl("https://github.com/springfox/springfox/blob/master/LICENSE")// 地址
//				.version("1.0")// 版本
//				.build();
//	}
//	
//}
