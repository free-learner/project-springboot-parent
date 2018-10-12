package com.personal.springboot.controller.base;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.personal.springboot.common.cons.Constants;
import com.personal.springboot.common.cons.ErrorCodeConstant;
import com.personal.springboot.common.utils.FastJsonUtil;
import com.personal.springboot.common.utils.UUIDGenerator;
import com.personal.springboot.controller.utils.MessageSourceUtil;
import com.personal.springboot.controller.vo.AppAuthHeader;
import com.personal.springboot.controller.vo.ResultInfo;
import com.personal.springboot.controller.vo.ResultPageInfo;
import com.personal.springboot.service.RedisCacheService;

/**
 * 基础请求Controller
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月10日
 */
public class BaseController<E, T extends BaseController<E, T>> {

    private static final Map<Class<?>, Logger> LOGGERS_MAP = new HashMap<Class<?>, Logger>();
    private static final Logger LOGGER = LoggerFactory.getLogger(BaseController.class);
    private Class<T> controllerClass;

    @Autowired(required = false)
    private MessageSourceUtil messageSourceUtil;

    @Autowired(required = false)
    private RedisCacheService redisCacheService;

    @SuppressWarnings("unchecked")
    public BaseController() {
        Type[] types = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments();
        this.controllerClass = (Class<T>) types[1];
    }

    public String getMessage(String code, Object[] args, String defaultMessage) {
        return messageSourceUtil.getMessage(code, args, defaultMessage);
    }
    
   /* public String getMessage(String code) {
        return this.getMessage(code);
    }

    public String getMessage(String code, Object[] args) {
        return messageSourceUtil.getMessage(code, args);
    }*/

    public String getMessage(String code, Object... args) {
        return messageSourceUtil.getMessage(code, args);
    }

    protected <V> ResultInfo<V> getSuccessResultInfo(V data) {
        return new ResultInfo<V>().buildSuccess(data);
    }

    protected <V> ResultInfo<V> getFailureResultInfo(String code, Object... args) {
        return new ResultInfo<V>().buildFailure(code, messageSourceUtil.getMessage(code, args));
    }

    protected Logger getLogger() {
        return getLogger(this.controllerClass);
    }

    private static Logger getLogger(Class<?> clazz) {
        Logger logger = LOGGERS_MAP.get(clazz);
        if (logger == null) {
            logger = LoggerFactory.getLogger(clazz);
            LOGGERS_MAP.put(clazz, logger);
        }
        return logger;
    }

    /**
     * 解析request对象请求参数信息
     */
    protected Map<String, Object> getMapFromServletRequest(HttpServletRequest request) {
        Map<String, String[]> requestMap = request.getParameterMap();
        Map<String, Object> resultMap = new HashMap<String, Object>();
        if (requestMap != null) {
            for (String key : requestMap.keySet()) {
                String[] values = requestMap.get(key);
                resultMap.put(key, values.length == 1 ? values[0].trim() : values);
            }
        }
        LOGGER.debug("转换请求参数信息执行了,resultMap:{}", JSON.toJSONString(resultMap));
        return resultMap;
    }

    /**
     * 获取对应的分页请求数据实体对象
     */
    protected ResultPageInfo<Collection<E>> fillPageParam(Map<String, Object> requestMap) {
        return fillPageParam(requestMap, null);
    }

    protected ResultPageInfo<Collection<E>> fillPageParam(Map<String, Object> requestMap, Integer totalCount) {
        // 公共参数获取
        int pageIndex = Integer.valueOf(requestMap.get("pageIndex") != null ? requestMap.get("pageIndex").toString() : "0");
        int pageSize = Integer.valueOf(requestMap.get("pageSize") != null ? requestMap.get("pageSize").toString() : "10");
        return new ResultPageInfo<Collection<E>>().buildSuccess(pageIndex, pageSize, totalCount, null);
    }

    /**
     * 获取ttl对应时间,单位为秒 -2:不存在或已经过期 -1:永远不过期 >0:有效秒数
     */
    protected ResultInfo<Long> getTtlResultInfo(String key) {
        long data = getTtl(key);
        if (data <= 0) {
            return getFailureResultInfo(ErrorCodeConstant.ERROR_CODE_TOKEN_EXPIRED);
        }
        return new ResultInfo<Long>().buildSuccess(data);
    }

    private long getTtl(String key) {
        long data = 0;
        data = redisCacheService.ttlByKey(key);
        LOGGER.info("获取的ttl信息为:{}", data);
        /*try {
        } catch (IOException e) {
            LOGGER.error("获取的ttl信息为:{}", e);
            data = 0;
        }*/
        return data;
    }

    /**
     * 生成用户Token信息 规则: key:Token_MobilePhone value:Token_UUID
     */
    protected String generateToken() {
        String token = Constants.CACHEKEY_TOKEN + UUIDGenerator.generate();
        return token;
    }

    protected String getCacheByKey(String cacheKey) {
        String value = null;
        value = redisCacheService.getString(cacheKey);
        LOGGER.info("获取的getCacheByKey[cacheKey={}]信息为:{}", cacheKey, value);
       /*try {
        } catch (IOException e) {
            LOGGER.error("获取的getCacheByKey[cacheKey={}]信息异常:{}", cacheKey, e);
        }*/
        return value;
    }

    protected void expireByKey(String cacheKey) {
        // redisCacheService.expireByKey(cacheKey, 1);
        redisCacheService.deleteByKey(cacheKey);
        /*try {
        } catch (IOException e) {
            LOGGER.error("失效的expireByKey:[cacheKey={}]信息异常:{}", cacheKey, e);
        }*/
    }

    protected <V> V copyProperties(V dest, Object orig) {
        try {
            BeanUtils.copyProperties(dest, orig);
        } catch (IllegalAccessException e) {
            LOGGER.error("属性拷贝方法copyProperties异常:[orig={}]信息异常:{}", orig, e);
            dest = null;
        } catch (InvocationTargetException e) {
            LOGGER.error("属性拷贝方法copyProperties异常:[orig={}]信息异常:{}", orig, e);
            dest = null;
        }
        if (dest != null) {
            LOGGER.info("属性拷贝方法转换非空实体结果为:{}", JSON.toJSONString(dest));
        }
        return dest;
    }

    protected String userTokenGetFromRequest(HttpServletRequest request) {
        // 获取用户token
        String userToken = null;
        AppAuthHeader appAuthHeader = userHeaderGetFromRequest(request);
        if (null != appAuthHeader) {
            userToken = appAuthHeader.getUserToken();
        }
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("userCodeGetFromRequest请求头获取userToken为:{}", userToken);
        }
        return userToken;
    }

    /**
     * 获取请求参数:用户mobilePhone手机号
     */
    protected String userMobilePhoneGetFromRequest(HttpServletRequest request) {
        String mobilePhone = null;
        AppAuthHeader appAuthHeader = userHeaderGetFromRequest(request);
        if (null != appAuthHeader) {
            mobilePhone = appAuthHeader.getMobilePhone();
        }
        return mobilePhone;
    }

    protected AppAuthHeader userHeaderGetFromRequest(HttpServletRequest request) {
        String header = request.getHeader(Constants.AUTH_HEADER);
        if (LOGGER.isInfoEnabled()) {
            LOGGER.info("HttpServletRequest请求头校验信息header为:{}", header);
        }
        if (StringUtils.isNoneBlank(header)) {
            return FastJsonUtil.parseObject(header, AppAuthHeader.class);
        }
        return null;
    }

}
