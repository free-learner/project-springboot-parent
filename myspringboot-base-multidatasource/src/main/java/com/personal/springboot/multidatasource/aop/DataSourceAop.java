package com.personal.springboot.multidatasource.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.personal.springboot.common.aop.TargetDataSource;
import com.personal.springboot.multidatasource.DataSourceContextHolder;

/**
 * 该切面需要设置到Service层,通过@Order区分@Transactional注解优先级
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月9日
 */
@SuppressWarnings("unused")
@Aspect
@Order(10)
@Component
public class DataSourceAop {
    private static final Logger logger = Logger.getLogger(DataSourceAop.class);
    
    @Pointcut("execution(* com.personal.springboot..service..*ServiceImpl.select*(..)) || execution(* com.personal.springboot..service..*ServiceImpl.find*(..)) || execution(* com.personal.springboot..service..*ServiceImpl.get*(..))")
    public void readPointcut(){}
    
    @Pointcut("execution(* com.personal.springboot..service..*ServiceImpl.insert*(..)) || execution(* com.personal.springboot..service..*ServiceImpl.update*(..)) || execution(* com.personal.springboot..service..*ServiceImpl.delete*(..))")
    public void writePointcut(){}

    @Before("readPointcut()")
    public void setReadDataSourceType() {
        DataSourceContextHolder.read();
        logger.info("DataSource切换到：READ模式!");
    }

    @Before("writePointcut()")
    public void setWriteDataSourceType() {
        DataSourceContextHolder.write();
        logger.info("DataSource切换到：WRITE模式!");
    }
    
    @After("readPointcut() || writePointcut()")
    public void restoreDataSource(JoinPoint point) {
        logger.info("DataSource清除 : {} > {}"+point.getSignature());
       DataSourceContextHolder.remove();
    }
    
    /*@Around("@annotation(org.springframework.transaction.annotation.Transactional)")
    public Object setWriteDataSourceType(ProceedingJoinPoint joinPoint) throws Throwable {
        Class<?> classTarget=joinPoint.getTarget().getClass();  
        String methodName=joinPoint.getSignature().getName();  
        Class<?>[] paramTypes=((MethodSignature) joinPoint.getSignature()).getParameterTypes();  
        Method objMethod=classTarget.getMethod(methodName, paramTypes); 
        Transactional transactional =objMethod.getAnnotation(Transactional.class); 
        if(transactional!=null){
            if(transactional.readOnly()){
                DataSourceContextHolder.read();
                logger.info("dataSource切换到：read模式.");
            }else{
                DataSourceContextHolder.write();
                logger.info("dataSource切换到：write模式.");
            }
        }else{
          //设置默认读模式
            DataSourceContextHolder.write();
            logger.info("dataSource切换到默认：read模式.");
        }
        return joinPoint.proceed();
    }*/
    
    /*@Around(value = "execution(* com.personal.springboot..service..*ServiceImpl.*(..)) && @annotation(transactional)")
    public Object setWriteDataSourceType(ProceedingJoinPoint joinPoint,Transactional transactional) throws Throwable {
        if(transactional!=null){
            if(transactional.readOnly()){
                DataSourceContextHolder.read();
                logger.info("dataSource切换到：read模式.");
            }else{
                DataSourceContextHolder.write();
                logger.info("dataSource切换到：write模式.");
            }
        }else{
            //设置默认读模式
            DataSourceContextHolder.read();
            logger.info("dataSource切换到默认：read模式.");
        }
        return joinPoint.proceed();
    }*/
    
}