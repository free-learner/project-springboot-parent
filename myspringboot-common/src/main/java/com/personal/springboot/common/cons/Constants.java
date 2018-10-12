package com.personal.springboot.common.cons;

import java.util.ResourceBundle;

/**
 * 常量定义类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public interface Constants{
	   //接口资源
    public static final ResourceBundle bundle = ResourceBundle.getBundle("commonResources");
    
    /**
     * 验证码类型
     */
    public static final String TYPE_CODE_IMG = "0";//图片验证码    
    public static final String TYPE_CODE_SMS = "1";//短信验证码
    public static final String TYPE_CODE_VOICE = "2";//语音验证码
    
    /**
     * 验证码发送场景类型
     */
    public static final String SEND_TYPE_REGISTER = "register";//注册场景
    public static final String SEND_TYPE_LOGIN_PWD = "loginpwd";//登录场景
    public static final String SEND_TYPE_LOGIN_SMS = "loginsms";//免密码登录场景
    public static final String SEND_TYPE_RESET_PASSWORD = "resetpwd";//重置密码场景
    public static final String SEND_TYPE_BIND_BANKCARD = "bindCard";//绑定银行卡场景
    
    //用户密码登录方式
    public static final String LOGIN_TYPE_PASSWD = "0";
    //用户快捷登录方式
    public static final String LOGIN_TYPE_SMS = "1";
    
    //消息状态值:0 初始发送 1 已读
    public static final int MESSAGE_STATUS_INIT = 0;
    public static final int MESSAGE_STATUS_ISREAD = 1;
    
    /**
     * 广告条显示
     */
    public static final String BANNER_TYOE_CODE_SHUFFLING = "0";//轮播图
    public static final String BANNER_TYOE_CODE_ADVERTISING = "1";//广告图
    
    //用户操作类型
    public static final String USER_OPT_TYPE_REGISTER = "register";
    public static final String USER_OPT_TYPE_LOGIN_PWD = "loginpwd";
    public static final String USER_OPT_TYPE_LOGIN_SMS = "loginsms";
    public static final String USER_OPT_TYPE_LOGOUT = "logout";
    public static final String USER_OPT_TYPE_RESETPWD = "resetpwd";
    
    public static final String AUTH_HEADER = "Auth-Header";
    //缓存key前缀定义
    public static final String CACHEKEY_SEPERATOR = "_";
    public static final String CACHEKEY_TOKEN = "TOKEN_";
    public static final String CACHEKEY_COUNTER = "COUNTER_";
    public static final String CACHEKEY_COUNTER_ERR_PWD= "COUNTER_ERR_PWD_";
    public static final String CACHEKEY_COUNTER_ERR_VERIFYCODE= "COUNTER_ERR_VERIFYCODE_";
    public static final String CACHEKEY_COUNTER_ERR_PWD_FOR_VERIFYCODE= "COUNTER_ERR_PWD_FOR_VERIFYCODE_";
    //无密码登录,有密码登录位置通过返回的key进行操作
    public static final String CACHEKEY_LOGIN_PWD = "LOGIN_PWD_";
    public static final String CACHEKEY_LOGIN_SMS= "LOGIN_SMS_";
    public static final String CACHEKEY_REGISTER = "REGISTER_";
    public static final String CACHEKEY_RESETPWD = "RESETPWD_";
    public static final String CACHEKEY_BINDPCARD = "BINDPCARD_";
    //缓存相关key信息定义
    public static final String CACHEKEY_MESSAGESWITCHINFO_SELECTALL = "MessageSwitchInfo.selectAll";
    
    //用户类型
    public static final String USERTYPE_INNER = "1";
    public static final String USERTYPE_OUTTER = "2";
    
    //用户来源渠道
    public static final String USERCHANNELTYPE_XJD_APP = "1";
    
    /**
     * 用户状态
     */
    public static enum UserStatus {
        ACTIVE(0, "已激活" ),                     
        AUTH(1, "已认证" ),                     
        FROZEN(2, "冻结(人工)" ),                   
        INVALID(3, "失效(自动)" ),                   
        NOTACTIVE(4, "未激活" ),                     
        NA(-1, "-未知" );     // N/A 未知
        
        private int key;
        private String value;
        
        private UserStatus(int key, String value) {
            this.key = key;
            this.value = value;
        }
        public int getKey() {
            return key;
        }
        public void setKey(int key) {
            this.key = key;
        }
        public String getValue() {
            return value;
        }
        public void setValue(String value) {
            this.value = value;
        }
        
        public static UserStatus getByKey(int key) {
            UserStatus[] os = UserStatus.values();
            for (int i = 0; i < os.length; i++) {
                if (os[i].getKey() == key) {
                    return os[i];
                }
            }
            return NA;
        }

        @Override
        public String toString() {
            return getValue();
        }
    }
    
    /**
     * 我的资料
     */
    //身份信息
    public static final String MY_IDENTITY_INFORMATION = "FK01";
    //背景信息
    public static final String MY_BACKGROUND_INFORMATION = "FK02";
    //联系人信息
    public static final String MY_CONTACT_INFORMATION = "FK03";
    
	/** 证件类型*/
	public static final String ID_TYPE_CERTTIFY= "01"; //身份证
	
	/** 安心签是否签约*/
	public static final String SIGN_FLAG_NO = "0"; //未签约
	public static final String SIGN_FLAG_YES = "1"; //签约
	
	/** 实名是否验证*/
	public static final String CERTIFY_FLAG_NO = "0"; //未认证
	public static final String CERTIFY_FLAG_YES = "1"; //认证    
	
	/** 来源渠道*/
	public static final String SOURCE_CHANNEL_TYPE_APP = "APP"; //APP来源
	
	/** 用户类型*/
	public static final String REGISTER_USER = "01"; //已注册用户
	
	/** 用户状态*/
	public static final int USER_STATUS_NORMAL = 1; //正常
	
	/** 联系人类型*/
	public static final String CONTACTS_TYPE_EMERGENCY = "1"; //紧急联系人 
	public static final String CONTACTS_TYPE_STANDBY = "2"; //备用联系人 
	
	/** 我的资料是否填写完成*/
	public static final String MY_RESOURECE_YES = "1"; 
	public static final String MY_RESOURECE_NO = "0"; 
	
	/** 芝麻授权状态*/
	public static final int ZM_CREDIT_STATUS_AUTH = 0; //正常
	public static final int ZM_CREDIT_STATUS_NOAUTH = 1; //未授权
	
	/** 客户类型*/
    public static final String USER_TYPE_OUTTER = "1";//外部用户
    public static final String USER_TYPE_INNER = "2";//内部用户
    
    /** 卡券接口类型*/
    public static final String COUPON_INTERFACE_01 = "01";//全部卡券
    public static final String COUPON_INTERFACE_02 = "02";//产品可用卡券

}
