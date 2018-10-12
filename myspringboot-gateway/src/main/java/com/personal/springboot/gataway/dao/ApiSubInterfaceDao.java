package com.personal.springboot.gataway.dao;

import java.util.List;

import com.personal.springboot.gataway.dao.entity.ApiSubInterface;

public interface ApiSubInterfaceDao extends BaseDao<ApiSubInterface> {
    
    /**
     * 按app_sub_id查询
     * @param app_sub_id
     * @return
     */
	List<ApiSubInterface> selectByAppSubId(String app_sub_id);
   
    /**
     * 按app_pub_id查询
     * @param app_pub_id
     * @return
     */
	List<ApiSubInterface> selectByAppPubId(String app_pub_id);
    
    /**
     * 按api_id查询
     * @param app_pub_id
     * @return
     */
	List<ApiSubInterface> selectByAppApiId(String api_id);
    
	/***
	 * 判断该apiId是否被某组织给下面的app订阅
	 * @param appId
	 * @param orgId
	 * @return
	 */
	boolean isSubByOrg(String apiId, String orgId);
}