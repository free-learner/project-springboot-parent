package com.yh.loan.front.test.utils;

import java.lang.reflect.Method;

import org.junit.Test;

import com.personal.springboot.common.utils.ObjectUtils;

/**
 * 反射工具测试类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年6月6日
 */
@SuppressWarnings("unused")
public class ReflectUtilsTest {

    @Test
    public void testInvoke01() throws Exception {
        Class<?>[] argsClass = new Class[] {};
        String methodName="method01";
        Method method = ReflectUtilsTest.class.getMethod(methodName,argsClass);
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance());
        System.out.println("反射调用结果为:" + rtnObj);
    }
    
    @Test
    public void testInvoke02() throws Exception {
        Class<?>[] argsClass = new Class[] {};
        String methodName="method02";
        Method method = ReflectUtilsTest.class.getMethod(methodName,argsClass);
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance());
        System.out.println("反射调用结果为:" + rtnObj);
    }
    
    @Test
    public void testInvoke03() throws Exception {
        Class<?>[] argsClass = new Class[] { Object.class };
        String methodName="method03";
        String params = new String("请求参数数据提信息!".getBytes(), "UTF-8").trim();
        Method method = ReflectUtilsTest.class.getMethod(methodName,argsClass);
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance(),params);
        System.out.println("反射调用结果为:" + rtnObj);
    }
    
    @Test
    public void testInvoke04() throws Exception {
        Class<?>[] argsClass = new Class[] { Object.class };
        String methodName="method04";
        String params = new String("请求参数数据提信息!".getBytes(), "UTF-8").trim();
        Method method = ReflectUtilsTest.class.getMethod(methodName,argsClass);
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance(),params);
        System.out.println("反射调用结果为:" + rtnObj!=null?rtnObj.toString():null);
    }
    
    /**
     * 获取私有方法测试工具类
     * getDeclaredMethod/setAccessible
     */
    @Test
    public void testInvoke05() throws Exception {
        Class<?>[] argsClass = new Class[] {};
        String methodName="method05";
        Method method = ReflectUtilsTest.class.getDeclaredMethod(methodName,argsClass);
        method.setAccessible(true); 
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance());
        System.out.println("反射调用结果为:" + rtnObj!=null?rtnObj.toString():null);
    }
    
    /**
     * 获取私有方法测试工具类
     * getDeclaredMethod/setAccessible
     */
    @Test
    public void testInvoke06() throws Exception {
        Class<?>[] argsClass = new Class[] {};
        String methodName="method06";
        Method method = ReflectUtilsTest.class.getDeclaredMethod(methodName,argsClass);
        method.setAccessible(true); 
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance());
        System.out.println("反射调用结果为:" + rtnObj!=null?rtnObj.toString():null);
    }
    
    /**
     * 获取私有方法测试工具类
     * getDeclaredMethod/setAccessible
     */
    @Test
    public void testInvoke07() throws Exception {
        Class<?>[] argsClass = new Class[] {String.class};
        String methodName="method07";
        Method method = ReflectUtilsTest.class.getDeclaredMethod(methodName,argsClass);
        method.setAccessible(true); 
        String params = new String("请求参数数据提信息!".getBytes(), "UTF-8").trim();
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance(),params);
        System.out.println("反射调用结果为:" + rtnObj==null?rtnObj.toString():null);
    }
    
    /**
     * 获取私有方法测试工具类
     * getDeclaredMethod/setAccessible
     */
    @Test
    public void testInvoke08() throws Exception {
        Class<?>[] argsClass = new Class[] {Object.class};
        String methodName="method08";
        Method method = ReflectUtilsTest.class.getDeclaredMethod(methodName,argsClass);
        method.setAccessible(true); 
        String params = new String("请求参数数据提信息!".getBytes(), "UTF-8").trim();
        Object rtnObj = (Object) method.invoke(ReflectUtilsTest.class.newInstance(),params);
        System.out.println("反射调用结果为:" + rtnObj!=null?rtnObj.toString():null);
    }
    
    /**
     * 测试方法01:无参数,无返回值
     */
    public void method01(){
        System.out.println(this.getClass().getName()+"方法method01执行了");
    }
    
    /**
     * 测试方法02:无参数,有返回值
     */
    public Object method02(){
        System.out.println(this.getClass().getName()+"方法method02执行了");
        return "我的测试返回结果数值";
    }
    
    /**
     * 测试方法03:有参数,无返回值
     */
    public void method03(Object obj){
        System.out.println(this.getClass().getName()+"方法method03执行了");
        System.out.println("请求参数信息为:"+obj);
    }
    
    /**
     * 测试方法04:有参数,有返回值
     */
    public Object method04(Object obj){
        System.out.println(this.getClass().getName()+"方法method04执行了");
        return obj==null?null:obj.toString()+"====>测试方法04结果!";
    }
    
    /**
     * 私有方法测试;
     * 
     */
    /**
     * 测试方法05:无参数,无返回值
     */
    private void method05(){
        System.out.println(this.getClass().getName()+"方法method05执行了");
    }
    
    /**
     * 测试方法06:无参数,有返回值
     */
    private Object method06(){
        System.out.println(this.getClass().getName()+"方法method06执行了");
        return "我的测试返回结果数值";
    }
    
    /**
     * 测试方法07:有参数,无返回值
     */
    private void method07(String obj){
        System.out.println(this.getClass().getName()+"方法method07执行了");
        System.out.println("请求参数信息为:"+obj);
    }
    
    /**
     * 测试方法08:有参数,有返回值
     */
    private Object method08(Object obj){
        System.out.println(this.getClass().getName()+"方法method08执行了");
        return obj==null?null:obj.toString()+"====>测试方法08结果!";
    }

}
