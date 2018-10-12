package com.personal.springboot.user.controller.base;

public interface InterfaceConstants {
    /** 接口请求*/
    public static final String CODE_SUCCESS = "SUCCESS";
    public static final String CODE_ERROR = "ERROR";
    
    //接口报文形式(发送)
    public static final String IDNUMBER         = "ID_NUMBER"; //身份证号
    
    public static final String ZHRC0040         = "ZHRC0040"; //身份证号（34接口需求）
    
    public static final String AUTHCODE         = "AUTH_CODE"; //授权码
    
    //接口报文形式(接收)
    public static final String REGCODE          = "REG_CODE"; //返回码
    
    public static final String REGMSG           = "REG_MSG"; //返回信息
    
    //url
    public static final String YHFIN0033        = "YHFIN0033";
    
    public static final String YHFIN0034        = "YHFIN0034";
    
    public static final String YHFIN0035        = "YHFIN0035";
    
    //返回报文信息
    public static final String SUCCESS          = "S"; //成功
    
    public static final String ERROR            = "E"; //失败
    
    //安心签
    public static final String AXQUSERID        = "userId"; //安心签userid
    
    //安心签 证件类型
    public static final String AXQUSERID_IDTYPE_0 = "0"; //身份证
    public static final String AXQUSERID_IDTYPE_1 = "1"; //护照
    
    //安心签短信验证错误码
    public static final String ERROR_CODE_AXQIAN_1 = "60030501"; //短信不正确
    
    //ODS 返回码
    public static final String ODS_SUCCESS = "00"; //成功
    public static final String ODS_FAIL = "99"; //失败
    public static final String OK = "OK"; //成功

    /** HR接口返回的状态码*/
    public static final String CUSTOEMR_SUCCESS = "S"; //成功
    
    public static final String CUSTOEMR_ERROR = "E"; //失败
    
    /** 员工雇佣状态*/
    public static final String LEAVEOFFICE = "0"; //离职
    
    public static final String INERTIA = "1"; //不活动
    
    public static final String RETIRE = "2"; //退休
    
    public static final String INSERVICE = "3"; //在职
    
    public static final String NO_PERSON = "4"; //无此人信息
    
    /** 员工组名称 **/
    public static final String EMP_GROUP_1 = "1"; //员工组-正式员工
    
    /** 内部员工高管0级 **/
    public static final String USER_LABEL_100 = "100"; //内部员工高管0级
    /** 内部员工高管1级 **/
    public static final String USER_LABEL_101 = "101"; //内部员工高管1级
    /** 内部员工高管2级 **/
    public static final String USER_LABEL_102 = "102"; //内部员工高管2级
    /** 内部员工高额度 **/
    public static final String USER_LABEL_103 = "103"; //内部员工高额度
    /** 内部员工低额度 **/
    public static final String USER_LABEL_104 = "104"; //内部员工低额度
    /** 永辉会员 **/
    public static final String USER_LABEL_201 = "201"; //永辉会员
    
    //ODS接口
    /** 用户实名认证 */
    public static final String ODS_USER_AUTHENTICATION = "ODS0001";
    
    /** 银行卡四要素认证*/
    public static final String BANK_FOUR_AUTHENTICATION = "ODS0002";
    
    /** 发送短信验证码*/
    public static final String SEND_MESSAGE_SMS = "ODS0003";
    
    /** 发送语言短信验证码*/
    public static final String SEND_MESSAGE_VOICE = "ODS0004";
    
    /** 手机定位*/
    public static final String MOBILE_PHONE_POSITIONING = "ODS0005";
    
    /** 芝麻信用授权*/
    public static final String ODS_0006 = "ODS0006";
    
    /** 推送消息*/
    public static final String ODS_0007 = "ODS0007";
    
    /** 安心签开户*/
    public static final String ODS_0008 = "ODS0008";

    /** 安心签发送签署短信*/
    public static final String ODS_0009 = "ODS0009";
    
    /** 安心签确认短信*/
    public static final String ODS_0010 = "ODS0010";
    
    /** 安心签签订合同*/
    public static final String ODS_0011 = "ODS0011";
    
    /** 金融云银行卡四要素认证*/
    public static final String ODS_0012 = "ODS0012";
    
    /** 好多、百融、同盾、linkface金融云四要素验证*/
    public static final String ODS_0013 = "ODS0013";
    
    /** 芝麻信用授权*/
    public static final String ODS_0014 = "ODS0014";
    
    /** 活体检测-人脸识别查询*/
    public static final String ODS_0015 = "ODS0015";
    /** 活体检测-人脸识别上传*/
    public static final String ODS_0016 = "ODS0016";
    
    //信贷RCMS接口
    /** 我的账单查询*/
    public static final String RCMS_0001 = "RCMS0001";  
    /** 产品列表查询*/
    public static final String RCMS_0002 = "RCMS0002";  
    /** 服务详情*/
    public static final String RCMS_0003 = "RCMS0003";  
    /** 安心签开户推送*/
    public static final String RCMS_0004 = "RCMS0004";  
    /** 卡券查询*/
    public static final String RCMS_0005 = "RCMS0005";  
    /** 借据查询*/
    public static final String RCMS_0006 = "RCMS0006";  
    /** 申请借款*/
    public static final String RCMS_0007 = "RCMS0007";
    /** 立即还款*/
    public static final String RCMS_0008 = "RCMS0008";
    /** 显示还款金额*/
    public static final String RCMS_0009 = "RCMS0009";
    /** 信贷申请编号*/
    public static final String RCMS_0010 = "RCMS0010";
    /** 更新内部用户*/
    public static final String RCMS_0011 = "RCMS0011";
    /** 产品可用卡券*/
    public static final String RCMS_0012 = "RCMS0012";
    /** 用户标签对应产品*/
    public static final String RCMS_0013 = "RCMS0013";
}
