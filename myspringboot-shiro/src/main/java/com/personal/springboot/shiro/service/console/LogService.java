/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.service.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.personal.springboot.shiro.dao.entity.console.Log;
import com.personal.springboot.shiro.dao.mapper.console.LogMapper;
import com.personal.springboot.shiro.utils.CamelCaseUtil;
import com.personal.springboot.shiro.utils.DateUtil;
import com.personal.springboot.shiro.utils.UuidUtil;

/**
 * author geekcattle
 * date 2017/1/6 0006 上午 11:26
 */
@Service
public class LogService {

    @Autowired
    private LogMapper logMapper;

    public List<Log> getPageList(Log log) {
        PageHelper.offsetPage(log.getOffset(), log.getLimit(), CamelCaseUtil.toUnderlineName(log.getSort())+" "+log.getOrder());
        return logMapper.selectAll();
    }

    public void insert(Log log){
        logMapper.insert(log);
    }

    public void insertLoginLog(String username, String ip, String action){
        Log  log = new Log();
        log.setLogId(UuidUtil.getUUID());
        log.setLogUser(username);
        log.setLogTime(DateUtil.getCurrentTime());
        log.setLogIp(ip);
        log.setLogAction(action);
        this.insert(log);
    }


}
