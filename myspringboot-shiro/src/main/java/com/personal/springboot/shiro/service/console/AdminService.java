/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.service.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.personal.springboot.shiro.dao.entity.console.Admin;
import com.personal.springboot.shiro.dao.mapper.console.AdminMapper;
import com.personal.springboot.shiro.utils.CamelCaseUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:43
 */
@Service
public class AdminService {

    @Autowired
    private AdminMapper adminMapper;

    public List<Admin> getPageList(Admin admin) {
        PageHelper.offsetPage(admin.getOffset(), admin.getLimit(), CamelCaseUtil.toUnderlineName(admin.getSort())+" "+admin.getOrder());
        return adminMapper.selectAll();
    }

    public Integer getCount(Example example){
        return adminMapper.selectCountByExample(example);
    }

    public Admin getById(String id) {
        return adminMapper.selectByPrimaryKey(id);
    }

    public Admin findByUsername(String username) {
        return adminMapper.selectByUsername(username);
    }

    public void deleteById(String id) {
        adminMapper.deleteByPrimaryKey(id);
    }

    public void insert(Admin admin){
        adminMapper.insert(admin);
    }

    public void save(Admin admin) {
        if (admin.getUid() != null) {
            adminMapper.updateByPrimaryKey(admin);
        } else {
            adminMapper.insert(admin);
        }
    }

    public void updateExample(Admin admin, Example example){
        adminMapper.updateByExampleSelective(admin, example);
    }



}
