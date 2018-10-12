package com.personal.springboot.multidatasource.aop;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.personal.springboot.common.aop.DataSourceType;
import com.personal.springboot.common.aop.TargetDataSource;
import com.personal.springboot.multidatasource.DataSourceContextHolder;
 
/**
 * 切换数据源Advice
 * @Order(-10)
 * //保证该AOP在@Transactional之前执行
 */
@Aspect
@Order(-10)
@Component
public class DynamicDataSourceAspect {
   
    private static final Logger LOGGER = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    
    /*
     * @Before("@annotation(targetDataSource)")
     * 的意思是：会拦截注解targetDataSource的方法，否则不拦截;
     */
    @Before("@annotation(targetDataSource)")
    public void changeDataSource(JoinPoint point, TargetDataSource targetDataSource) throws Throwable {
       //获取当前的指定的数据源;
        DataSourceType dsType = targetDataSource.value();
        //如果不在我们注入的所有的数据源范围之内，那么输出警告信息，系统自动使用默认的数据源。
        if (!DataSourceContextHolder.contains(dsType)) {
            LOGGER.info("数据源[{}]不存在，使用默认数据源 > {}"+targetDataSource.value()+point.getSignature());
            DataSourceContextHolder.setDataSourceType(DataSourceType.WRITE);
        } else {
            LOGGER.info("Use DataSource : {} > {}"+targetDataSource.value()+point.getSignature());
            DataSourceContextHolder.setDataSourceType(dsType);
        }
    }
   
    @After("@annotation(targetDataSource)")
    public void restoreDataSource(JoinPoint point, TargetDataSource targetDataSource) {
       LOGGER.info("Remove DataSource : {} > {}"+targetDataSource.value()+point.getSignature());
       DataSourceContextHolder.remove();
    }
   
}