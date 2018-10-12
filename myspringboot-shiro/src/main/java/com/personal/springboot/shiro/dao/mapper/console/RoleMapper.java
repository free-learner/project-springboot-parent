/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.dao.mapper.console;

import java.util.List;
import java.util.Set;

import com.personal.springboot.shiro.dao.entity.console.Role;
import com.personal.springboot.shiro.utils.CustomerMapper;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:32
 */
public interface RoleMapper extends CustomerMapper<Role> {
    Set<String> findRoleByUserId(String userId);
    List<Role> selectRoleListByAdminId(String Id);
}
