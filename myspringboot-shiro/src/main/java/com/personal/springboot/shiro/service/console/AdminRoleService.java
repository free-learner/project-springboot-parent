/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.service.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personal.springboot.shiro.dao.entity.console.AdminRole;
import com.personal.springboot.shiro.dao.mapper.console.AdminRoleMapper;

import tk.mybatis.mapper.entity.Example;

/**
 * author geekcattle
 * date 2016/12/6 0006 上午 10:45
 */
@Service
public class AdminRoleService {

    @Autowired
    private AdminRoleMapper adminRoleMapper;

    public void insert(AdminRole adminRole){
        adminRoleMapper.insert(adminRole);
    }

    public void deleteAdminId(String id){
        Example example = new Example(AdminRole.class);
        example.createCriteria().andCondition("admin_id =", id);
        adminRoleMapper.deleteByExample(example);
    }

    public void deleteRoleId(String id){
        Example example = new Example(AdminRole.class);
        example.createCriteria().andCondition("role_id =", id);
        adminRoleMapper.deleteByExample(example);
    }

    public AdminRole selectOne(AdminRole adminRole){
        return adminRoleMapper.selectOne(adminRole);
    }

    public List<AdminRole> getRoleList(AdminRole adminRole){
        return adminRoleMapper.select(adminRole);
    }


}
