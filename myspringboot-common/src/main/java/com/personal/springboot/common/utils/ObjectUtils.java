package com.personal.springboot.common.utils;

import java.util.Collection;
import java.util.Date;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Object判断工具类
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月20日
 */
public class ObjectUtils {
    @SuppressWarnings("unused")
    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectUtils.class);

    @SuppressWarnings({ "rawtypes", "unused" })
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;
        
        if (obj instanceof Integer) {
            int value = ((Integer) obj).intValue();
           } else if (obj instanceof String) {
            String s = (String) obj;
           } else if (obj instanceof Double) {
            double d = ((Double) obj).doubleValue();
           } else if (obj instanceof Float) {
            float f = ((Float) obj).floatValue();
           } else if (obj instanceof Long) {
            long l = ((Long) obj).longValue();
           } else if (obj instanceof Boolean) {
            boolean b = ((Boolean) obj).booleanValue();
           } else if (obj instanceof Date) {
            Date d = (Date) obj;
           }  

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            
            return empty;
        }
        return false;
    }
}
