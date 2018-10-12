//package com.personal.springboot.controller.aop;
//
//import java.io.PrintWriter;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.servlet.ServletRequest;
//import javax.servlet.ServletResponse;
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.stereotype.Component;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.servlet.ModelAndView;
//
//import com.alibaba.fastjson.JSON;
//
//
///**
// * 日志Controller拦截类
// * @AutoConfigureAfter({ .class })
// * 
// * @Author  LiuBao
// * @Version 2.0
// *   2017年4月1日
// */
//@Component
//@Aspect
//@Order(1)
//public class LogRecordAspect {
//	
//	private static final Logger LOGGER = LoggerFactory.getLogger(LogRecordAspect.class);
//
//	@SuppressWarnings({ "unchecked", "rawtypes" })
//    //@Before(value="(execution(* com.personal.springboot.controller..*.*(..))) ")  
//    @Before("@annotation(org.springframework.web.bind.annotation.RequestMapping)")    
//	public void before(JoinPoint joinPoint) {
//		String url = null;
//		try {
//			url = generateUrl(joinPoint);
//		} catch (Exception e) {
//		    LOGGER.error("解析请求url信息异常!",e);
//		}
//		if(url!=null){
//			List<Object> objs = new ArrayList<Object>();
//			for (Object obj : joinPoint.getArgs()) {
//				if (obj instanceof MultipartFile) {
//				    MultipartFile commFile = (MultipartFile) obj;
//					Map map = new HashMap();
//					//上传form的name属性
//					map.put("name", commFile.getName());
//					map.put("contentType", commFile.getContentType());
//					map.put("originalFilename", commFile.getOriginalFilename());
//					map.put("size", commFile.getSize());
//					objs.add(map);
//				} else if(obj instanceof ServletResponse
//				        ||obj instanceof ModelAndView
//				        ||obj instanceof Model
//				        ||obj instanceof PrintWriter
//				        ){
//				    continue;
//				}else if(obj instanceof ServletRequest
//                        ){
//				    try {
//                        Map<String, Object> map = getMapFromRequest((HttpServletRequest)obj);
//                        objs.add(map);
//                        //url=((HttpServletRequest)obj).getRequestURI();
//                    } catch (Exception e) {
//                        LOGGER.error("解析HttpServletRequest信息异常!",e);
//                    }
//                }else{
//					objs.add(obj);
//				}
//			}
//	
//			String jsonString="{}";
//			try {
//				jsonString = JSON.toJSONString(objs,true);
//			} catch (Exception e) {
//			    LOGGER.error("JSON数据格式化异常!",e);
//			}
//			if(LOGGER.isInfoEnabled()){
//				LOGGER.info("##########################################################");
//				LOGGER.info("######## request url: " + url);
//				LOGGER.info("######## request param: " + jsonString);
//				LOGGER.info("##########################################################\n");
//			}
//		}
//	}
//	
//	//@AfterReturning(value="(execution(* com.personal.springboot..controller..*.*(..)))",argNames="joinPoint,retValue", returning="retValue")  
//    //@AfterReturning(value = "@annotation(org.springframework.web.bind.annotation.ResponseBody)", argNames = "joinPoint,retValue", returning = "retValue")
//	@AfterReturning(value="@annotation(org.springframework.web.bind.annotation.RequestMapping)",argNames="joinPoint,retValue", returning="retValue")  
//	public void after(JoinPoint joinPoint,Object retValue) {
//		String url =  null;
//		try {
//		   url = generateUrl(joinPoint);
//		} catch (Exception e) {
//		    LOGGER.error("解析请求url信息异常!",e);
//		}
//		String jsonString = JSON.toJSONString(retValue,true);
//		if(jsonString.length()>1000){
//			jsonString="jsonString.length:"+jsonString.length();
//		}
//		if(url!=null&&LOGGER.isInfoEnabled()){
//			LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//			LOGGER.info("@@@@@@@@ response (request url): " + url);
//			LOGGER.info("@@@@@@@@ response value: " + jsonString);
//			LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
//		}
//	}
//
//	private String generateUrl(JoinPoint joinPoint) {
//		String url = "";
//		RequestMapping requestMappingMethod = null;
//		RequestMapping requestMappingClass = null;
//
//		Class<?> cls = joinPoint.getTarget().getClass();
//		requestMappingClass = cls.getAnnotation(RequestMapping.class);
//		Method[] methods = cls.getMethods();
//		String methodName = joinPoint.getSignature().getName();
//		for (Method m : methods) {
//			// 获取执行方法前的注解信息。
//			if (m.getName().equals(methodName)) {
//				requestMappingMethod = m.getAnnotation(RequestMapping.class);
//				break;
//			}
//		}
//		
//		if(requestMappingClass!=null){
//		    String[] valueClasses = requestMappingClass.value();
//		    String[] valueMethods = requestMappingMethod.value();
//		    if (valueClasses.length > 0) {
//		        url = url.concat(valueClasses[0]);
//		    }
//		    if (valueMethods.length > 0) {
//		        url = url.concat(valueMethods[0]);
//		    }
//		}
//		return url;
//	}
//
//    /**
//     * 解析request对象请求参数信息
//     */
//    private Map<String ,Object> getMapFromRequest(HttpServletRequest request) {
//        Map<String, String[]> requestMap = request.getParameterMap();
//        Map<String ,Object> resultMap = new HashMap<String ,Object>();
//        if (requestMap != null) {
//            for (String key : requestMap.keySet()) {
//                String[] values = requestMap.get(key);
//                resultMap.put(key, values.length == 1 ? values[0].trim() : values);
//            }
//        }
//        return resultMap;
//    }
//	
//
//}
