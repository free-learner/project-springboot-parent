package com.personal.springboot.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 正则校验工具类
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年5月21日
 */
public class RegexUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegexUtils.class);
    
    /**
     * 验证用户名，支持中英文（包括全角字符）、数字、下划线和减号 （全角及汉字算两位）,
     * 长度为4-20位,必须同时包含英文和数字
     */
    public static boolean validateENString(String inputStr) {
        String regex = "^[\\w\\-_[０-９]]+$";
        boolean result = isMatches(regex, inputStr);
        if (result) {
            result = isNumberAndEng(inputStr);
        }
        if (result) {
            int strLenth = getLength(inputStr);
            if (strLenth < 6 || strLenth > 20) {
                result = false;
            }
        }
        return result;
    }
    
    public static boolean validateCNString(String inputStr) {
        String regex = "^[\\w\\-_[０-９]\u4e00-\u9fa5\uFF21-\uFF3A\uFF41-\uFF5A]+$";
        boolean result = isMatches(regex, inputStr);
        if (result) {
            int length = getLength(inputStr);
            if (length < 6 || length > 20) {
                result = false;
            }
        }
        return result;
    }
    
    /**
     * 密码校验:字母和数字组合,长度为6-20位数
     */
    public static boolean validatePassword(String inputStr) {
        boolean result = isNumberAndEng(inputStr);
        if (result) {
            int length = getLength(inputStr);
            if (length < 6 || length > 20) {
                result = false;
            }
        }
        return result;
    }
    
    public static String hiddenMobile(final String phone) { 
        return hiddenString(phone, phone.length()-8, phone.length()-3);
    }
    
    public static String hiddenBankCard(final String bankCardNo) { 
        return hiddenString(bankCardNo, 0, bankCardNo.length()-4);
    }
    
    public static String hiddenIdCard(final String idCardNo) { 
        return hiddenString(idCardNo, 0, idCardNo.length()-4);
    }
    
    public static String hiddenName(final String name) { 
        return hiddenString(name, 0, name.length()-1);
    }

    /**
     * 获取掩盖字符串
     * 包含:beginIndex
     * 不包含:endIndex
     */ 
    private static String hiddenString(String inputStr,int beginIndex,int endIndex) { 
        LOGGER.info("获取掩盖字符串inputStr={},beginIndex={},endIndex={}",inputStr,beginIndex,endIndex);
        int inputStrLength = RegexUtils.getLength(inputStr);
        int length = inputStr.length();
        int scal=inputStrLength/length;
        LOGGER.info("获取掩盖字符串inputStrLength={},length={},scal={}",inputStrLength,length,scal);
        String result=null;
        if(StringUtils.isBlank(inputStr)||beginIndex>endIndex){
            result=inputStr;
        }else{
            int i=0;
            result=inputStr.substring(0,beginIndex);
            while (beginIndex+i<endIndex) {
                i++;
                result+="*";
            }
            result+=inputStr.substring(endIndex);
        }
        LOGGER.info("获取掩盖字符串结果inputStr={},result={}",inputStr,result);
        return result;
    }
    
    /**
     * 验证Email
     * 格式:xxx@yyy.com.cn
     */ 
    public static boolean isEmail(String inputStr) { 
      String regex = "\\w+@\\w+\\.[a-z]+(\\.[a-z]+)?"; 
      return isMatches(regex,inputStr);
    }
    
    /**
     * 验证身份证号码
     *  居民身份证号码15位或18位，最后一位可能是数字或字母
     */ 
    public static boolean isIdCard(String inputStr) { 
      String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}"; 
      return isMatches(regex,inputStr);
    } 
    
    /**
     * 验证固定电话号码
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字，
     * 数字之后是空格分隔的国家（地区）代码。</p>
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号——
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p>
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p>
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isPhone(String inputStr) { 
      String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$"; 
      return isMatches(regex,inputStr);
    } 
    
    /**
     * 验证手机号码
     * （支持国际格式，+86135xxxx...（中国内地），+00852137xxxx...（中国香港））
     * @param mobile 移动、联通、电信运营商的号码段
     *<p>移动的号段：134(0-8)、135、136、137、138、139、147（预计用于TD上网卡）
     *、150、151、152、157（TD专用）、158、159、187（未启用）、188（TD专用）</p>
     *<p>联通的号段：130、131、132、155、156（世界风专用）、185（未启用）、186（3g）</p>
     *<p>电信的号段：133、153、180（未启用）、189</p>
     * @return 验证成功返回true，验证失败返回false
     */ 
    public static boolean isMobile(String inputStr) {
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        // String regex = "^[1][3,4,5,7,8][0-9]{9}$";
        String regex = "(\\+\\d+)?1[34578]\\d{9}$";
        return isMatches(regex,inputStr);
    }

    /*
     * 校验过程： 1、从卡号最后一位数字开始，逆向将奇数位(1、3、5等等)相加。
     * 2、从卡号最后一位数字开始，逆向将偶数位数字，先乘以2（如果乘积为两位数，将个位十位数字相加，即将其减去9），再求和。
     * 3、将奇数位总和加上偶数位总和，结果应该可以被10整除。
     */
    /**
     * 校验银行卡卡号
     */
    public static boolean isBankCard(String inputStr) {
        if (StringUtils.isBlank(inputStr) || inputStr.length() < 15 || inputStr.length() > 19) {
            return false;
        }
        char bit = getBankCardCheckCode(inputStr.substring(0, inputStr.length()-1));
        if (bit == 'N') {
            return false;
        }
        return inputStr.charAt(inputStr.length() - 1) == bit;
    }
    
    /**
     * 
     * 校验:应为或字母
     */
    public static boolean isNumberAndEng(String inputStr) {
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,32}$";
        return isMatches(regex,inputStr);
    }

    /**
     * 
     * 校验:中文或字母
     */
    public static boolean isChineseOrEng(String inputStr) {
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        String regex = "^([\u4E00-\uFA29]|[\uE7C7-\uE7F3]|[a-zA-Z])*$";
        return isMatches(regex,inputStr);
    }
    
    /**
     * 验证中文字符
     */ 
    public static boolean isChinese(String inputStr) { 
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
      String regex = "^[\u4E00-\u9FA5]+$"; 
      return isMatches(regex,inputStr);
    }
    
    /**
     * 验证连续4个中文字符
     */ 
    public static boolean is4Chinese(String inputStr) { 
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        String regex = "^.*[\u4E00-\u9FA5]{4}.*$";
        return isMatches(regex,inputStr);
    }
    
    /**
     * 过滤特殊字符串正则 
     * [`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？] 
     */ 
    public static boolean isContainsSpecial(String inputStr) { 
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        String regex="[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“'。，、？]"; 
        return isMatches(regex,inputStr);
    }
    
    /**
     * 验证URL地址
     * 格式：https http www ftp 
     */ 
    public static boolean isURL(String inputStr) {
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        // String regex ="(https?://(w{3}\\.)?)?\\w+\\.\\w+(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";
        String regex = "^(https|http|www|ftp|)?(://)?(\\w+(-\\w+)*)(\\.(\\w+(-\\w+)*))*((:\\d+)?)(/(\\w+(-\\w+)*))*(\\.?(\\w)*)(\\?)?"
                + "(((\\w*%)*(\\w*\\?)*(\\w*:)*(\\w*\\+)*(\\w*\\.)*(\\w*&)*(\\w*-)*(\\w*=)*(\\w*%)*(\\w*\\?)*"
                + "(\\w*:)*(\\w*\\+)*(\\w*\\.)*" + "(\\w*&)*(\\w*-)*(\\w*=)*)*(\\w*)*)$";
        return isMatches(regex,inputStr);
    }
    
    /**
     * 获取网址 URL 的一级域
     */
    public static String getDomain(String inputStr) {
      Pattern pattern = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
      // 获取完整的域名
      // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);
      Matcher matcher = pattern.matcher(inputStr);
      matcher.find();
      return matcher.group();
    }
    
    /**
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小)
     * @param ipAddress IPv4标准地址
     */
    public static boolean isIpAddress(String inputStr) { 
      String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))"; 
      return isMatches(regex,inputStr);
    } 

    /**
     * 
     * 金额验证:判断小数点后2位的数字的正则表达式
     */
    public static boolean isNumber(String inputStr) {
        if (StringUtils.isBlank(inputStr)) {
            return false;
        }
        String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
        return isMatches(regex,inputStr);
    }
    
    /**
     * 验证整数（正整数和负整数）
     */ 
    public static boolean isDigit(String inputStr) { 
      String regex = "\\-?[0-9]\\d+"; 
      return isMatches(regex,inputStr);
    } 
    
    /**
     * 验证整数和浮点数（正负整数和正负浮点数）如：1.23，233.30
     */ 
    public static boolean isDecimals(String inputStr) { 
      String regex = "\\-?[1-9]\\d+(\\.\\d+)?"; 
      return isMatches(regex,inputStr);
    } 
    
    /**
     * 验证空白字符
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B
     */ 
    public static boolean isBlankSpace(String inputStr) { 
      String regex = "\\s+"; 
      return isMatches(regex,inputStr);
    } 

    /**
     * 获取字符串的长度，对双字符（包括汉字）按两位计数
     */
    public static int getLength(String inputStr) {
        int length = 0;
        for (int i = 0; i < inputStr.length(); i++) {
            String temp = inputStr.substring(i, i + 1);
            if (isChinese(temp)) {
                length += 2;
            } else {
                length += 1;
            }
        }
        return length;
    }
    
    public static boolean checkBankCreditCardLength(String inputStr) {
        int length = getLength(inputStr);
        return length>=14&& length<=16;
    }
    
    /**
     * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
     */
    private static char getBankCardCheckCode(String nonCheckCodeBankCard) {
        if (nonCheckCodeBankCard == null || nonCheckCodeBankCard.trim().length() == 0
                || !nonCheckCodeBankCard.matches("\\d+")) {
            // 如果传的不是数据返回N
            return 'N';
        }
        char[] chs = nonCheckCodeBankCard.trim().toCharArray();
        int luhmSum = 0;
        for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
            int k = chs[i] - '0';
            if (j % 2 == 0) {
                k *= 2;
                k = k / 10 + k % 10;
            }
            luhmSum += k;
        }
        return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
    }
    
    public static boolean isMatches(String regex,String inputStr) {
        boolean result=false;
        if (StringUtils.isNoneBlank(inputStr, regex)) {
            result = Pattern.matches(regex, inputStr);
        }
        if(LOGGER.isDebugEnabled()){
            LOGGER.debug("当前正则信息校验参数为{},输入为:{},结果为:{}",regex,inputStr,result);
        }
        return result;
    }

}
