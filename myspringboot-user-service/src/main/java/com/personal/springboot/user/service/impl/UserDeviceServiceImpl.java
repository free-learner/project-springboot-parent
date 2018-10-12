package com.personal.springboot.user.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.springboot.service.impl.BaseServiceImpl;
import com.personal.springboot.user.dao.entity.UserDevice;
import com.personal.springboot.user.dao.mapper.UserDeviceMapper;
import com.personal.springboot.user.service.UserDeviceService;

/**
 * 用户设备信息服务
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月28日
 */
@Service("userDeviceService")
public class UserDeviceServiceImpl extends BaseServiceImpl<UserDevice> implements UserDeviceService {

	@SuppressWarnings("unused")
    private UserDeviceMapper userDeviceMapper;
	
	@Autowired
	public UserDeviceServiceImpl(UserDeviceMapper userDeviceMapper) {
		super(userDeviceMapper);
		this.userDeviceMapper = userDeviceMapper;
	}


}
