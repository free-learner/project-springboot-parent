package com.personal.springboot.gataway.service;

import com.personal.springboot.gataway.dao.entity.ApiApp;
import com.personal.springboot.gataway.dao.entity.ApiInterface;
import com.personal.springboot.gataway.dao.entity.ApiSubInterface;
import com.personal.springboot.gataway.dao.entity.RedisFastDto;

public interface RedisService{
	
	public ApiApp  findApiAppByAppId(String app_id);
	public ApiInterface findApiInterfaceByKey(String api_id,String version);	
	public ApiSubInterface  findApiSubInterfaceByKey(String appSubId, String api_id,String version) ;
	public RedisFastDto findRedisFastDtoByKey(String appSubId, String api_id,String version) ;
	public void saveRedisFastDto(ApiApp app,ApiInterface apiInterface,ApiSubInterface apiSubInterface);
	
	public void delApiSubInterfaceByApi(String api_id);
	public void delApiSubInterfaceByApp(String app_id);
	
	
	public void delFastDto(String apiId);
	
}
