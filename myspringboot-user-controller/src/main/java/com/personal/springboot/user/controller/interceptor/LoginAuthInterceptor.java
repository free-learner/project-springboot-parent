package com.personal.springboot.user.controller.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.Assert;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.personal.springboot.common.cons.Constants;
import com.personal.springboot.common.entity.BaseEntity;
import com.personal.springboot.common.utils.FastJsonUtil;
import com.personal.springboot.controller.aop.OnceTokenGenerator;
import com.personal.springboot.controller.base.BaseController;
import com.personal.springboot.controller.vo.AppAuthHeader;
import com.personal.springboot.service.RedisCacheService;

/**
 * 登录状态及访问权限验证
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月1日
 */
public class LoginAuthInterceptor extends BaseController<BaseEntity,LoginAuthInterceptor>  implements HandlerInterceptor {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(LoginAuthInterceptor.class);
    
    @Autowired
    private RedisCacheService redisCacheService;
    
    @Value("${noauth.redirect.path}")
    private String noauthRedirectPath ;
    
    @Value("${tokenexpired.redirect.path}")
    private String tokenExpiredPath ;
    
    @Value("${noheader.redirect.path}")
    private String noheaderRedirectPath ;
    
    @Value("${oncetoken.redirect.path}")
    private String tokenRedirectPath ;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        boolean resultFlag=true;
        String requestURI = request.getRequestURI();
        String header = request.getHeader(Constants.AUTH_HEADER);
        if(LOGGER.isInfoEnabled()){
            LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
            LOGGER.info("请求头校验信息header为:{}",header);
            LOGGER.info("结果requestURI:{},redirectPath:{},tokenExpiredPath:{}",requestURI,noauthRedirectPath,tokenExpiredPath);
        }
        try {
            if(StringUtils.isBlank(header)){
                resultFlag= false;
                //response.sendRedirect(contextPath+noheaderRedirectPath);
                request.getRequestDispatcher(noheaderRedirectPath).forward(request,response);
                return resultFlag;
            }
            //获取用户token
            AppAuthHeader appAuthHeader = FastJsonUtil.parseObject(header, AppAuthHeader.class);
            String userToken=null,mobilePhone=null;
            if(appAuthHeader!=null){
                userToken=appAuthHeader.getUserToken();
                mobilePhone = appAuthHeader.getMobilePhone();
            }
            
            if(StringUtils.isBlank(userToken)){
                //token不存在,未授权
                resultFlag= false;
                //response.sendRedirect(contextPath+noauthRedirectPath);
                request.getRequestDispatcher(noauthRedirectPath).forward(request,response);
                return resultFlag;
            }else if(!redisCacheService.existsByKey(userToken)){
                //token失效位置
                resultFlag= false;
                //response.sendRedirect(contextPath+tokenExpiredPath);
                request.getRequestDispatcher(tokenExpiredPath).forward(request,response);
                return resultFlag;
            }
            
            // mobilePhone校验
            if(StringUtils.isBlank(mobilePhone)){
                //mobilePhone不存在,未授权
                resultFlag= false;
                request.getRequestDispatcher(noheaderRedirectPath).forward(request,response);
                return resultFlag;
            }/*else if(!StringUtils.equalsIgnoreCase(mobilePhone, super.userMobilePhoneGetFromCache(request))){
                resultFlag= false;
                request.getRequestDispatcher(noauthRedirectPath).forward(request,response);
                return resultFlag;
            }*/
            
            //重复提交校验  
            if (!HandlerMethod.class.isAssignableFrom(handler.getClass())) {
                return resultFlag;
            }
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            OnceTokenGenerator onceToken = handlerMethod.getMethodAnnotation(OnceTokenGenerator.class);
            if (onceToken != null&&!onceToken.value()) {
                Assert.notNull(redisCacheService,"缓存数据源redisCacheService为空!");
                String onceTokenStr="onceTokenStr";
                try {
                    if(redisCacheService.existsByKey(onceTokenStr)){
                        redisCacheService.deleteByKey(onceTokenStr);
                    }else{
                        //重定向或抛出指定重复提交异常即可 
                        LOGGER.warn("ResponseBody中OnceToken信息[onceToken={}]重复提交." ,onceTokenStr);    
                        resultFlag= false;
                        request.getRequestDispatcher(tokenRedirectPath).forward(request,response);
                    }
                } catch (Exception e) {
                    LOGGER.error("ResponseBody中OnceToken信息[onceToken={}]校验异常:" ,onceTokenStr, e);    
                    resultFlag= false;
                    request.getRequestDispatcher(tokenRedirectPath).forward(request,response);
                }
            }
        }finally{
            if(LOGGER.isInfoEnabled()){
                LOGGER.info("登录状态及访问权限验证结果resultFlag为:{}",resultFlag);
                LOGGER.info("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@\n");
            }
        }
        return resultFlag;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        /*if(ex instanceof NoAuthException){
            response.setContentType(ContentType.APPLICATION_JSON.getMimeType());
            ResultInfo<String> buildFailure = new ResultInfo<String>().buildFailure(ErrorCodeConstant.REQUIRED_NOAUTH_EXCEPTION, ex.getMessage());
            response.getWriter().write(JSON.toJSONString(buildFailure));
        }*/
    }
    
}