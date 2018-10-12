package com.personal.springboot.gataway.service.impl;

import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.personal.springboot.gataway.dao.ApiAppDao;
import com.personal.springboot.gataway.dao.ApiInterfaceDao;
import com.personal.springboot.gataway.dao.ApiSubInterfaceDao;
import com.personal.springboot.gataway.dao.entity.ApiApp;
import com.personal.springboot.gataway.dao.entity.ApiInterface;
import com.personal.springboot.gataway.dao.entity.ApiSubInterface;
import com.personal.springboot.gataway.dao.entity.RedisFastDto;
import com.personal.springboot.gataway.service.RedisService;
import com.personal.springboot.gataway.utils.RedisPredicate;

@Service("redisService")
public class RedisServiceImpl implements RedisService {

	public static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);
	private static final Logger msglog = LoggerFactory.getLogger("msglog");
	private static final Logger redislog = LoggerFactory.getLogger("redislog");
	
	@Resource(name = "redisTemplate_openapi")
	private RedisTemplate<String, Object> redisTemplate;

	@Inject
	private ApiAppDao apiAppDao;

	@Inject
	private ApiInterfaceDao apiInterfaceDao;

	@Inject
	private ApiSubInterfaceDao apiSubInterfaceDao;
	
	@Value("${redis.ignoreRedis}")
	private String ignoreRedis ;
	
	@Value("${redis.prefix}")
	private String redisPrefix;
	

	@Override
	public void saveRedisFastDto(ApiApp app, ApiInterface apiInterface,
			ApiSubInterface apiSubInterface) {
		
		String key =redisPrefix+apiSubInterface.getAppSubId()+ "."+ apiSubInterface.getApiId() ;//+"."+version;
		try{
			if (!"true".equalsIgnoreCase(ignoreRedis)) {			
				RedisFastDto redisFastDto=new RedisFastDto(app,apiInterface,apiSubInterface);
				redislog.debug("POST  key["+RedisFastDto.class.getName()+"]   hashkey["+key+"]  obj["+redisFastDto+"]");
				redisTemplate.opsForHash().put(RedisFastDto.class.getName(), key,redisFastDto);			
			}	
		}catch(Exception e){
			redislog.error("redis 异常",e);
		}
	}


	@Override
	public RedisFastDto findRedisFastDtoByKey(String apiSubId, String api_id,String version) {
		//目前一个api只支持一个版本
		String key =redisPrefix+  apiSubId+ "."+ api_id ;//+"."+version;
		
		try{
			if (!"true".equalsIgnoreCase(ignoreRedis)) {
				
				Object obj = redisTemplate.opsForHash().get(RedisFastDto.class.getName(), key);
				redislog.debug("GET  key["+RedisFastDto.class.getName()+"]   hashkey["+key+"]  obj["+obj+"]");
				
				if (obj != null) {
					return (RedisFastDto) obj;
				}
			}	
		}catch(Exception e){
			redislog.error("redis 异常",e);
		}
		return null;
	}


	
	@Override
	public ApiSubInterface findApiSubInterfaceByKey(String appSubId, String api_id,String version) {
		//目前一个api只支持一个版本
		String key =redisPrefix+  appSubId + "."+ api_id;//+"."+version;
		boolean redisError =false;
		
		try{
			if (!"true".equalsIgnoreCase(ignoreRedis)) {
				
				Object obj = redisTemplate.opsForHash().get(ApiSubInterface.class.getName(), key);
				redislog.debug("GET  key["+ApiSubInterface.class.getName()+"]   hashkey["+key+"]  obj["+obj+"]");
				
				if (obj != null) {
					return (ApiSubInterface) obj;
				}
			}
		}catch(Exception e){
			redisError=true;
			redislog.error("redis 异常",e);
		}
		
		Object obj =(ApiSubInterface)apiSubInterfaceDao.selectOneByEntity(new ApiSubInterface(appSubId,api_id));
			
		if(null != obj && !redisError){
			redislog.debug("PUT  key["+ApiSubInterface.class.getName()+"]   hashkey["+key+"]  obj["+obj+"]");
			redisTemplate.opsForHash().put(ApiSubInterface.class.getName(), key,obj);			
		}		

		return  null!=obj?(ApiSubInterface)obj:null;
	}

	
		
	
	@Override
	public ApiApp findApiAppByAppId(String app_id) {
	
		String key=redisPrefix+ "app_id:"+app_id;
		
		boolean redisError =false;
		
		try{
			if (!"true".equalsIgnoreCase(ignoreRedis)) {	
				Object obj = redisTemplate.opsForHash().get(ApiApp.class.getName(),key);
				redislog.debug("GET  key["+ApiApp.class.getName()+"]   hashkey["+key+"]  obj["+obj+"]");
				
				if (null != obj) {		
					return (ApiApp)obj;
				}
			}
		}catch(Exception e){
			redisError=true;
			redislog.error("redis 异常",e);
		}

		ApiApp apiAppTem = new ApiApp();
		apiAppTem.setAppId(app_id);
		Object obj = apiAppDao.selectOneByEntity(apiAppTem);
				
		if(null != obj && !redisError){
			redislog.debug("PUT  key["+ApiApp.class.getName()+"]   hashkey["+key+"]  obj["+obj+"]");
			redisTemplate.opsForHash().put(ApiApp.class.getName(), key,obj);
			
		}
		
		return   (ApiApp)obj;
	}

	
	private String getRedisKey(String api_id, String version){
		ApiInterface ai = new ApiInterface();
		ai.setApiId(api_id);
		ai.setVersion(version);
		return ai.getRedisKey();
	}
	
	@Override
	public ApiInterface findApiInterfaceByKey(String api_id, String version) {
		/***
		 * 避免RedisFastDto中的两级redis缓存不一致，加上版本号
		 * modi by @author jiangyouliang1 2017-01-11
		 */
		//String key =redisPrefix+ "api_id:"+api_id ;//+ "version:" + version;
		String key =redisPrefix+ getRedisKey(api_id, version);
		boolean redisError =false;
		
		try{
			if (!"true".equalsIgnoreCase(ignoreRedis)) {
				Object obj = redisTemplate.opsForHash().get(ApiInterface.class.getName(), key);
				redislog.debug("GET  key["+ApiInterface.class.getName()+"]   hashkey["+key+"]  obj["+obj+"]");
				
				if (obj != null) {			
					return (ApiInterface) obj;
				}
			}		
		}catch(Exception e){
			redisError=true;
			redislog.error("redis 异常",e);
		}

		ApiInterface tem = new ApiInterface();
		tem.setApiId(api_id);
		tem.setVersion(version);
		List<ApiInterface> list = apiInterfaceDao.selectWithOutDelFlagData(tem);
		ApiInterface apiInterface=null;
		
		if(list!=null && !redisError && list.size()>0  ){
			apiInterface=list.get(0);
		}
		
		redislog.debug("PUT  key["+ApiInterface.class.getName()+"]   hashkey["+key+"]  obj["+apiInterface+"]");
		redisTemplate.opsForHash().put(ApiInterface.class.getName(), key,apiInterface);
		
		return apiInterface;
	}




	@Override
	public void delApiSubInterfaceByApi(String api_id) {
//		String key =redisPrefix+  apiSubInterface.getAppSubId() + "."+ apiSubInterface.getApiId();
		
		String key =redisPrefix+".*" + "[.]"+api_id;
		Set keySet= redisTemplate.opsForHash().keys(ApiSubInterface.class.getName());		
		CollectionUtils.filter(keySet, new RedisPredicate(key));
		if(keySet!=null && keySet.size()>0){
		    redislog.debug("DEL  key["+ApiSubInterface.class.getName()+"]   hashkey["+keySet+"]");
		    redisTemplate.opsForHash().delete(ApiSubInterface.class.getName(), keySet.toArray());		
		}		
		
				
		//删除fastDto对象
		keySet= redisTemplate.opsForHash().keys(RedisFastDto.class.getName());		
		CollectionUtils.filter(keySet, new RedisPredicate(key));
		if(keySet!=null && keySet.size()>0){
		    redislog.debug("DEL  key["+RedisFastDto.class.getName()+"]   hashkey["+keySet+"]");
		    redisTemplate.opsForHash().delete(RedisFastDto.class.getName(), keySet.toArray());		
		}
		
		
	}
	
	@Override
	public void delFastDto(String apiId){
		String key =redisPrefix+".*" + "[.]"+apiId;
		Set keySet= redisTemplate.opsForHash().keys(RedisFastDto.class.getName());		
		CollectionUtils.filter(keySet, new RedisPredicate(key));
		if(keySet!=null && keySet.size()>0){
		    redislog.debug("DEL  key["+RedisFastDto.class.getName()+"]   hashkey["+keySet+"]");
		    redisTemplate.opsForHash().delete(RedisFastDto.class.getName(), keySet.toArray());		
		}
	}


	@Override
	public void delApiSubInterfaceByApp(String app_id) {
	
		String key =redisPrefix+app_id+"[.].*";
		
		Set keySet= redisTemplate.opsForHash().keys(ApiSubInterface.class.getName());		
		CollectionUtils.filter(keySet, new RedisPredicate(key));
		
		if(keySet!=null && keySet.size()>0){
			redislog.debug("DEL  key["+ApiSubInterface.class.getName()+"]   hashkey["+keySet+"]");
			redisTemplate.opsForHash().delete(ApiSubInterface.class.getName(), keySet.toArray());	
		}
		
		
		//删除fastDto对象
		keySet= redisTemplate.opsForHash().keys(RedisFastDto.class.getName());		
		CollectionUtils.filter(keySet, new RedisPredicate(key));
		if(keySet!=null && keySet.size()>0){
		redislog.debug("DEL  key["+RedisFastDto.class.getName()+"]   hashkey["+keySet+"]");
		redisTemplate.opsForHash().delete(RedisFastDto.class.getName(), keySet.toArray());		
		}		
				
		
	}







	

}
