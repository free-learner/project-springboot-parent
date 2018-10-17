package com.personal.springboot.common.exception;

/**
 * 异常码常量定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年3月29日
 */
public abstract class AbsErrorCodeConstant{
	
    public static final String ZERO = "0";
    //默认提示文案信息
	public static final String SUCCESS = "00000";
	//服务端信息异常
	public static final String FAILURE = "99999";
	//服务端正忙,请稍后重试
	public static final String ERROR_CODE_DEFAULT= "99998";
	
	//服务端异常响应码
	public static final String ERROR_CODE_302 = "error.302";
    public static final String ERROR_CODE_400 = "error.400";
    public static final String ERROR_CODE_401 = "error.401";
    public static final String ERROR_CODE_404 = "error.404";
    public static final String ERROR_CODE_405 = "error.405";
    public static final String ERROR_CODE_500 = "error.500";
    
    //服务端其他异常响应码 99000-99998
    //请求参数信息{}不能为空!
    public static final String ERROR_CODE_99000= "99000";
	//请求参数信息不正确!
	public static final String ERROR_CODE_99001= "99001";
	//请求JSON数据格式不正确!
	public static final String ERROR_CODE_99002= "99002";
	//请求参数绑定失败!
	public static final String ERROR_CODE_99003= "99003";
	//请求参数校验失败!
	public static final String ERROR_CODE_99004= "99004";
	//请求方法{0}不支持!
	public static final String ERROR_CODE_99005= "99005";
	//请求类型ContentType:{0}不支持!
	public static final String ERROR_CODE_99006= "99006";
	
	//特殊错误码定义 90000-90999
	//请求头Header信息不存在!
	public static final String ERROR_CODE_90000 = "90000";
	//用户Token不存在/不正确/校验失败/已失效!
	public static final String ERROR_CODE_90001= "90001";
	//NoAuth 访问资源未授权!
	public static final String ERROR_CODE_90002 = "90002";
	
	//其他Http请求相关错误码定义 91000-91999
	//获取响应信息失败
	public static final String ERROR_CODE_91000 = "91000";
	//Http请求连接失败
	public static final String ERROR_CODE_91001= "91001";
	//Http请求连接超时
	public static final String ERROR_CODE_91002 = "91002";
	//主机信息解析失败
	public static final String ERROR_CODE_91003 = "91003";
	//Http请求响应异常
	public static final String ERROR_CODE_91004 = "91004";
	//Http网络连接异常
	public static final String ERROR_CODE_91005 = "91005";
	//SSL信息初始化异常
	public static final String ERROR_CODE_91006 = "91006";
	//URL信息编解码异常
	public static final String ERROR_CODE_91007 = "91007";
	//Https协议解析失败
	public static final String ERROR_CODE_91008 = "91008";
	//Http请求响应数据为空
	public static final String ERROR_CODE_91009 = "91009";
	
	//工具類相关错误码定义 92000-92999
	//初始化RSA加密字符串异常
	public static final String ERROR_CODE_92000 = "92000";
	//加解密阀值数据异常
	public static final String ERROR_CODE_92001 = "92001";
	//验签字符串异常
	public static final String ERROR_CODE_92002 = "92002";
	//签名字符串异常
	public static final String ERROR_CODE_92003 = "92003";
	//解密字符串异常
	public static final String ERROR_CODE_92004 = "92004";
	//加密字符串异常
	public static final String ERROR_CODE_92005 = "92005";
	
}
