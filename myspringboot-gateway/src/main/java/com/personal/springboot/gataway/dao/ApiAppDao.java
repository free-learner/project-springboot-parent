package com.personal.springboot.gataway.dao;

import java.util.List;

import com.personal.springboot.gataway.dao.entity.ApiApp;
import com.personal.springboot.gataway.dao.entity.ApiAppVO;


public interface ApiAppDao extends BaseDao<ApiApp>  {
	
	public List<ApiAppVO> getAppSysOrgDetails(ApiApp apiApp);
	
}
