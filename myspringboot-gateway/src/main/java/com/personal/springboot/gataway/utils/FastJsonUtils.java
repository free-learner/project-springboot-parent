package com.personal.springboot.gataway.utils;

import org.apache.commons.lang3.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.parser.Feature;
import com.alibaba.fastjson.serializer.SimplePropertyPreFilter;
import com.personal.springboot.gataway.dao.entity.ApiCommonParamDto;


public  class FastJsonUtils {

    @SuppressWarnings("unchecked")
    public static <T> T textToBean(String jsonText,Class clazz) {
        return (T) JSONObject.parseObject(jsonText, clazz);
    }

    public static <T> String beanToText(T object) {
        return beanToText(object,"");
    }    
    
    public static <T> String beanToText(T object,String rootName) {
    	String tem=JSONObject.toJSONString(object);    	
    	if(!StringUtils.isEmpty(rootName)){
    		tem="{\""+rootName+"\":"+tem+"}";
    	}    	
        return tem;
    }
    
    /**  
     * 将java对象转换成json字符串  
     *
     * @param bean  
     * @return  
     */
    public static String beanToText(Object bean, String... properties) {
    	SimplePropertyPreFilter spp =new SimplePropertyPreFilter(properties);    	
        return JSONObject.toJSONString(bean,spp);
    }
    
    
    public static  JSONObject parse(String json ,boolean keepIndex){
    	
    	int type=Feature.config(JSON.DEFAULT_PARSER_FEATURE, Feature.OrderedField, keepIndex);
		JSONObject jasonObject = (JSONObject) JSONObject.parse(json,type);
		
		return jasonObject;
    }
   
    
   
    public static void main(String[] args) {
		
//		String json="{\"returnData\":{},\"return_desc\":\"chenggong\",\"return_code\":\"S000A000\"}";		
//		OpenApiRespDto obj=(OpenApiRespDto)FastJsonUtils.textToBean(json, OpenApiRespDto.class);		
		
		
		ApiCommonParamDto apiCommonParamDto=new ApiCommonParamDto();
    	apiCommonParamDto.setApiID("111");
    	
    	System.out.println(FastJsonUtils.beanToText(apiCommonParamDto));
		
	}
}