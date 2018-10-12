//package com.personal.springboot.controller.aop;
//import java.io.ByteArrayOutputStream;
//import java.io.DataInputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.ServletInputStream;
//import javax.servlet.http.HttpServletRequest;
//
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.AfterReturning;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.core.annotation.Order;
//import org.springframework.http.HttpMethod;
//import org.springframework.stereotype.Component;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import com.alibaba.fastjson.JSON;
// 
///**
// * Controller层日志记录Advice
// */
//@Aspect
//@Order(5)
//@Component
//public class LogAspect {
//    private static final Logger LOGGER = LoggerFactory.getLogger(LogAspect.class);
//    
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) && @annotation(com.personal.springboot.controller.aop.Log)")    
//     public  void requestMappingAndLogAspect() {    
//    }    
//    
//    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping)")    
//    public  void requestMappingAspect() {    
//    }    
//    
//    @Pointcut("@annotation(com.personal.springboot.controller.aop.Log)")
//     public  void logAspect() {    
//    } 
//    
//    @Before("@annotation(log)")
//    public void logBefore(JoinPoint joinPoint, Log log) throws Throwable {
//        LOGGER.info("=====前置通知开始=====");    
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
//        String ip = request.getRemoteAddr();    
//        String url =  request.getRequestURI();
//        String method = request.getMethod();
//         try {    
//             LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//            LOGGER.info("@@@@@@@@logBefore[{}]请求url:{}" ,method, url);    
//            LOGGER.info("logBefore请求IP:" + ip);    
//            LOGGER.info("logBefore请求参数:" +JSON.toJSONString(getMapFromRequest(request),true));    
//            LOGGER.info("logBefore方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
//            LOGGER.info("方法描述:" + log.operation()+":"+log.module());    
//            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
//            //*========数据库日志記錄=========*//    
//        }  catch (Exception e) {    
//            LOGGER.error("==前置通知异常==，异常信息:{}",e);    
//        }  
//         LOGGER.info("=====前置通知结束=====");    
//    }
//   
////    @After("@annotation(log)")
//    //@AfterReturning("@annotation(log)")
//    @AfterReturning(value="logAspect()",argNames="joinPoint,retValue", returning="retValue")  
//    public void logAfterReturning(JoinPoint joinPoint,Object retValue) {
//        LOGGER.info("=====AfterReturning通知开始=====");    
//       HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
//       String url =  request.getRequestURI();
//       String method = request.getMethod();
//       String ip = request.getRemoteAddr();    
//       String jsonString = JSON.toJSONString(retValue,true);
//       /*if(jsonString.length()>1000){
//           jsonString="jsonString.length:"+jsonString.length();
//       }*/
//       if(url!=null&&LOGGER.isInfoEnabled()){
//           LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//           LOGGER.info("@@@@@@@@logAfterReturning[{}]请求url:{}" ,method, url);    
//           LOGGER.info("请求IP:" + ip);    
//           LOGGER.info("logAfterReturning方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
//           LOGGER.info("@@@@@@@@ response value: " + jsonString);
//           LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
//       }
//        LOGGER.info("=====AfterReturning通知结束=====");    
//    }
//    
//    /**
//     * 異常信息記錄
//     */
//    @AfterThrowing(pointcut = "logAspect()", throwing = "e")    
//    public  void logAfterThrowing(JoinPoint joinPoint, Throwable e) {    
//        LOGGER.info("=====异常通知开始=====");    
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();    
//        String ip = request.getRemoteAddr();    
//        String params = "";    
//         if (joinPoint.getArgs() !=  null && joinPoint.getArgs().length > 0) {    
//             for ( int i = 0; i < joinPoint.getArgs().length; i++) {    
//                params += JSON.toJSONString(joinPoint.getArgs()[i]);    
//            }    
//        }    
//         try {    
//             LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//             LOGGER.info("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));    
//            LOGGER.info("异常代码:" + e.getClass().getName());    
//            LOGGER.info("异常信息:" + e.getMessage());    
//            LOGGER.info("方法描述:" + getLogMthodDescription(joinPoint));    
//            LOGGER.info("请求IP:" + ip);    
//            LOGGER.info("请求参数:" + params);    
//            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
//        }  catch (Exception ex) {    
//            LOGGER.error("异常通知信息:{}", ex.getMessage());    
//        }    
//         LOGGER.error("异常方法:{}异常代码:{}异常信息:{}参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);    
//    }
//    
//    /**  
//     * 获取注解中对方法的描述信息
//     */    
//     public  static String getLogMthodDescription(JoinPoint joinPoint) throws Exception {    
//        String targetName = joinPoint.getTarget().getClass().getName();    
//        String methodName = joinPoint.getSignature().getName();    
//        Object[] arguments = joinPoint.getArgs();    
//        Class<?> targetClass = Class.forName(targetName);    
//        Method[] methods = targetClass.getMethods();    
//        String description = "";    
//         for (Method method : methods) {    
//             if (method.getName().equals(methodName)) {    
//                Class<?>[] clazzs = method.getParameterTypes();    
//                 if (clazzs.length == arguments.length) {    
//                    description = method.getAnnotation(Log.class).operation();    
//                     break;    
//                }    
//            }    
//        }    
//         return description;    
//    }  
//     
//     /*private Map<String ,Object> getLogRequestParam(JoinPoint joinPoint,HttpServletRequest request) throws Exception {    
//         Object[] arguments = joinPoint.getArgs();    
//         Class<?> targetClass= joinPoint.getTarget().getClass();    
//         Log logClass = targetClass.getAnnotation(Log.class);
//         String methodName = joinPoint.getSignature().getName();    
//         Method[] methods = targetClass.getMethods();
//         for (Method method : methods) {    
//             if (method.getName().equals(methodName)) {    
//                 Class<?>[] clazzs = method.getParameterTypes();    
//                 if (clazzs.length == arguments.length) {    
//                     Map<String, Object> mapFromRequest = getMapFromRequest(request);
//                     mapFromRequest.put("log", method.getAnnotation(Log.class));
//                     return mapFromRequest; 
//                 }    
//             }    
//         }    
//         return null;    
//     }*/  
//     
//     /**
//      * 解析request对象请求参数信息
//      */
//     @SuppressWarnings("unchecked")
//    private Map<String ,Object> getMapFromRequest(HttpServletRequest request) throws IOException{
//         Map<String ,Object> resultMap = new HashMap<String ,Object>();
//         String method = request.getMethod();
//         if(HttpMethod.GET.matches(method)){
//             Map<String, String[]> requestMap = request.getParameterMap();
//             if (requestMap != null) {
//                 for (String key : requestMap.keySet()) {
//                     String[] values = requestMap.get(key);
//                     resultMap.put(key, values.length == 1 ? values[0].trim() : values);
//                 }
//             }
//             /*if(LOGGER.isDebugEnabled()){
//                 LOGGER.debug("GET请求参数为:{}", ( JSON.toJSONString(resultMap)));
//             }*/
//         } else if (HttpMethod.POST.matches(method)) {
//             ServletInputStream sis = request.getInputStream();
//             DataInputStream dataInStream = new DataInputStream(sis);
//             ByteArrayOutputStream os = new ByteArrayOutputStream();
//             try {
//                 byte[] buf = new byte[1024];
//                 int n = dataInStream.read(buf);
//                 while (n != -1) {
//                     os.write(buf, 0, n);
//                     n = dataInStream.read(buf);
//                 }
//                 byte[] res = os.toByteArray();
//                /* if(res.length>1000){
//                     res=Arrays.copyOf(res, 1000);
//                 }*/
//                 String paramAsString=null;
//                 if(res.length>0){
//                     paramAsString =new String(res,"utf-8");
//                     resultMap=JSON.parseObject(paramAsString,Map.class);
//                 }
//                 /*if (LOGGER.isDebugEnabled()) {
//                     LOGGER.debug("POST请求参数为:{}" ,paramAsString);
//                 }*/
//             } catch (Exception e) {
//                 e.printStackTrace();
//             }finally{
//                 if(os!=null){
//                     os.close();
//                 }
//                 if(dataInStream!=null){
//                     dataInStream.close();
//                 }
//             }
//         }
//         return resultMap;
//     }
//     
//}