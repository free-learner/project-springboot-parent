package com.personal.springboot.common.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

/**
 * 字符串工具类。
 */
public abstract class StringUtil {

	private StringUtil() {}

    public static boolean checkNull(Object target) {
        if (target == null || StringUtils.isBlank(target.toString().trim())) {
            return true;
        }
        return false;
    }
	
    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     * 
     * @param value 待检查的字符串
     * @return true/false
     */
	public static boolean isEmpty(String value) {
		int strLen;
		if (value == null || (strLen = value.length()) == 0) {
			return true;
		}
		for (int i = 0; i < strLen; i++) {
			if ((Character.isWhitespace(value.charAt(i)) == false)) {
				return false;
			}
		}
		return true;
	}

    /**
     * 检查对象是否为数字型字符串,包含负数开头的。
     */
	public static boolean isNumeric(Object obj) {
		if (obj == null) {
			return false;
		}
		char[] chars = obj.toString().toCharArray();
		int length = chars.length;
		if(length < 1)
			return false;
		
		int i = 0;
		if(length > 1 && chars[0] == '-')
			i = 1;
		
		for (; i < length; i++) {
			if (!Character.isDigit(chars[i])) {
				return false;
			}
		}
		return true;
	}

    /**
     * 检查指定的字符串列表是否不为空。
     */
	public static boolean areNotEmpty(String... values) {
		boolean result = true;
		if (values == null || values.length == 0) {
			result = false;
		} else {
			for (String value : values) {
				result &= !isEmpty(value);
			}
		}
		return result;
	}

    /**
     * 把通用字符编码的字符串转化为汉字编码。
     */
	public static String unicodeToChinese(String unicode) {
		StringBuilder out = new StringBuilder();
		if (!isEmpty(unicode)) {
			for (int i = 0; i < unicode.length(); i++) {
				out.append(unicode.charAt(i));
			}
		}
		return out.toString();
	}

    /**
     * 过滤不可见字符
     */
	public static String stripNonValidXMLCharacters(String input) {
		if (input == null || ("".equals(input)))
			return "";
		StringBuilder out = new StringBuilder();
		char current;
		for (int i = 0; i < input.length(); i++) {
			current = input.charAt(i);
			if ((current == 0x9) || (current == 0xA) || (current == 0xD)
					|| ((current >= 0x20) && (current <= 0xD7FF))
					|| ((current >= 0xE000) && (current <= 0xFFFD))
					|| ((current >= 0x10000) && (current <= 0x10FFFF)))
				out.append(current);
		}
		return out.toString();
	}
	
	/**
     * 分割以指定符号的字符串
     */
    public static String[] getSplitString(String splitStr, String splitType) {
        StringTokenizer str = new StringTokenizer(splitStr, splitType);
        int strNum = str.countTokens();
        String strTemp[] = new String[strNum];
        int i = 0;
        while (str.hasMoreElements()) {
            strTemp[i] = (String) str.nextElement();
            i++;
        }
        return strTemp;
    }
    
    /**
     * get the domain as String with url
     * @param url
     * @return
     */
    public static String getURLMainDomain(String url){
        String svr=url;
        boolean isFullURL=false;
        try{
            //decode url, if the url is encoded
            svr=URLDecoder.decode(url,"UTF-8");
        }catch (UnsupportedEncodingException e) {
            svr=url;
        }
        //remove the protocal part, http,https, etc
        // modify by caifa 2012-11-21 begin 
        if(svr.contains("http://") && svr.contains(".com")){
            svr = svr.substring(svr.indexOf("http://"), svr.indexOf(".com")+4);
            isFullURL=true;
        }else if(svr.contains("http://") && svr.contains(".cn")){
            svr = svr.substring(svr.indexOf("http://"), svr.indexOf(".cn")+3);
            isFullURL=true;
        }else if(svr.contains("https://") && svr.contains(".com") ){
            svr = svr.substring(svr.indexOf("https://"), svr.indexOf(".com")+4);
            isFullURL=true;
        }else if(svr.contains("https://") && svr.contains(".cn") ){
            svr = svr.substring(svr.indexOf("https://"), svr.indexOf(".cn")+3);
            isFullURL=true;
        }
        svr=org.apache.commons.lang3.StringUtils.stripStart(svr, "https://");
        svr=org.apache.commons.lang3.StringUtils.stripStart(svr, "http://");
        //get the first part as server name
        svr=svr.split("/")[0];
        String svrMainDomain=svr;
        //if is full url then start getting main domain, otherwise, return empty
        //if all the svr name removes. is numbers, then the server is using IP, 
        //otherwise, only need to compare the last 2 part
        if (isFullURL){
            if (!isNumeric(svr.replace(".", ""))){
                String domainParts[]=svr.split("\\.");
                int svrPartCount=domainParts.length;
                //if the is 3 parts in the server name, will need to compare the 2nd,3rd part only, for others, will compare all
                if(svrPartCount>=3) {
                    svrMainDomain=domainParts[svrPartCount-2]+"."+domainParts[svrPartCount-1];
                }
            }
        }else{
            svrMainDomain="";
        }
        return svrMainDomain;
    }
    
    /**
     * if url is allowed
     * @param urlString
     * @param allowDomain(*.suning.com,*.cnsuning.com)
     * @return
     */
    public static boolean urlAccessAllowed(String domainString, String[] allowDomains){
        boolean allow=false;
        for (int i=0;i<allowDomains.length;i++){ 
            String s=allowDomains[i];
            //generate regex
            s = s.replace('.', '#');
            s = s.replaceAll("#", "\\.");
            s = s.replace('*', '#');
            s = s.replaceAll("#", ".*");
            s = s.replace('?', '#');
            s = s.replaceAll("#", ".?");
            s = "^" + s + "$";;
            Pattern p = Pattern.compile(s);
            Matcher m = p.matcher(domainString);
            boolean b=m.matches();
            if (b){
                allow=true;
                break;
            }
        }
        return allow;
    }

}
