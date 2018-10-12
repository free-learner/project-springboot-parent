package com.personal.springboot.common.cons;

/**
 * 异常码常量定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年3月29日
 */
public final class ErrorCodeConstant {
	
	public static final String SUCCESS = "0";
	public static final String FAILURE = "1";
	//默认异常提示文案信息
	public static final String ERROR_CODE_DEFAULT= "error.message.default";
	
	//请求参数信息不正确!
	public static final String REQUIRED_PARAMETER_ISNOTPRESENT= "required.parameter.isnotpresent";
	//请求JSON数据格式不正确!
	public static final String REQUIRED_COULDNOT_READJSON= "required.couldnot.readjson";
	//请求参数绑定失败!
	public static final String REQUIRED_COULDNOT_BINDPARAM= "required.couldnot.bindparam";
	//请求参数校验失败!
	public static final String REQUIRED_VALIDATION_EXCEPTION= "required.validation.exception";
	//请求方法{0}不支持!
	public static final String REQUIRED_METHOD_NOTSUPPORTED= "required.method.notsupported";
	//请求类型{0}不支持!
	public static final String REQUIRED_CONTENTTYPE_NOTSUPPORTED= "required.contenttype.notsupported";
	//请求参数信息{}不能为空!
	public static final String REQUIRED_PARAMETER_ISNOTEMPTY= "required.parameter.isnotempty";
	//操作(更新/删除/添加)数据失败!
	public static final String ERROR_CODE_DATAOPT= "error.code.dataopt";
	
	//用户Token不存在/不正确/校验失败/已失效!
	public static final String ERROR_CODE_TOKEN_EXPIRED= "99999";
	//NoAuth 访问资源未授权!
	public static final String ERROR_CODE_NOAUTH = "99998";
	//请求头Header信息不存在!
	public static final String ERROR_CODE_NOHEADER = "99997";
	//验证码信息错误,请重新输入!
    public static final String ERROR_CODE_99996 = "99996";
  //token信息保存异常,可能是redisCacheService异常!当前Token信息已过期,请返回刷新后重试!
    public static final String ERROR_CODE_99995 = "99995";
    //上传文件信息错误,可能太大!
    public static final String ERROR_CODE_99990 = "99990";
	
	//服务端异常响应码
	public static final String ERROR_CODE_400 = "error.400";
	public static final String ERROR_CODE_401 = "error.401";
	public static final String ERROR_CODE_404 = "error.404";
	public static final String ERROR_CODE_405 = "error.405";
	public static final String ERROR_CODE_500 = "error.500";
	
	//其他异常码编码
	//10000-19999 基础信息相关异常
	//请求参数属性转换异常!
	public static final String ERROR_CODE_10000 = "10000";
	//调用方法的Mapper文件不存在!
	public static final String ERROR_CODE_10001 = "10001";
	//数据库异常,请稍候重试!
	public static final String ERROR_CODE_10002 = "10002";
	//操作数据库出现异常:字段重复/有外键关联等!
	public static final String ERROR_CODE_10003= "10003";
	
	//用户登录模块 20000-29999
	//验证码[{0}]输入信息错误!
	public static final String ERROR_CODE_20000 = "20000";
	//用户密码或密码错误!
	public static final String ERROR_CODE_20001 = "20001";
	//手机号格式输入错误
	public static final String ERROR_CODE_20002 = "20002";
	//用户密码重置失败!
	public static final String ERROR_CODE_20003 = "20003";
	//验证码类型[{0}]信息不存在!
	public static final String ERROR_CODE_20004 = "20004";
	//当前用户已注册,请直接登录!
	public static final String ERROR_CODE_20005 = "20005";
	//用户注册失败,请稍后重新注册!
	public static final String ERROR_CODE_20006 = "20006";
	//用户密码异常,请稍后重试!
	public static final String ERROR_CODE_20007 = "20007";
	//验证码操作过于频繁,请稍后重试!
	public static final String ERROR_CODE_20008 = "20008";
	//今日错误次数过多,请明天再试!
	public static final String ERROR_CODE_20009 = "20009";
	//当前用户[{0}]信息不存在,请注册后再登录!
	public static final String ERROR_CODE_20010 = "20010";
	//验证码错误,请重新生成!
    public static final String ERROR_CODE_20011 = "20011";
    //今日获取次数过多,请明天再试!
    public static final String ERROR_CODE_20012 = "20012";
	
	//个人中心,数据字典及ODS等接口 30000-39999
	//当前查询的数据字典[{0}]数据不存在!
	public static final String ERROR_CODE_30000 = "30000";
	//银行卡格式输入错误
	public static final String ERROR_CODE_30001 = "30001";
	//银行卡对应发卡银行信息不存在
	public static final String ERROR_CODE_30002 = "30002";
	//用户实名认证失败
	public static final String ERROR_CODE_30003 = "30003";
	//银行四要素认证失败
	public static final String ERROR_CODE_30004 = "30004";
	//短信验证码发送失败
	public static final String ERROR_CODE_30005 = "30005";
	//该用户已身份认证
	public static final String ERROR_CODE_30006 = "30006";
	//该用户已背景认证
	public static final String ERROR_CODE_30007 = "30007";
	//该用户已联系人认证
	public static final String ERROR_CODE_30008 = "30008";
	//该用户芝麻信用已授权
	public static final String ERROR_CODE_30009 = "30009";
	//芝麻信用授权失败
	public static final String ERROR_CODE_30010 = "30010";
	//安心签开户失败
	public static final String ERROR_CODE_30011 = "30011";
	//内部用户标签失败
	public static final String ERROR_CODE_30012 = "30012";
	//身份信息未认证
	public static final String ERROR_CODE_30013 = "30013";
	
	//借款还款、信贷等接口 40000-49999
	//产品列表查询失败!
	public static final String ERROR_CODE_40000 = "40000";
	//我的账单查询失败!
	public static final String ERROR_CODE_40001 = "40001";
	//我的卡券列表查询失败!
	public static final String ERROR_CODE_40002 = "40002";
	//借条数据列表查询失败!
	public static final String ERROR_CODE_40003 = "40003";
	//借条详情查询失败!
	public static final String ERROR_CODE_40004 = "40004";
	//立即还款提交失败!
	public static final String ERROR_CODE_40005 = "40005";
	//安心签开户提交失败!
	public static final String ERROR_CODE_40006 = "40006";
	//安心签确认短信失败
	public static final String ERROR_CODE_40007 = "40007";
	//安心签合同签署失败
	public static final String ERROR_CODE_40008 = "40008";
	//服务详情查询失败
	public static final String ERROR_CODE_40009 = "40009";
	//还款金额查询失败
	public static final String ERROR_CODE_40010 = "40010";
	
	
}
