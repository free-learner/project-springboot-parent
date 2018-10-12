/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.dao.mapper.console;

import java.util.List;
import java.util.Set;

import com.personal.springboot.shiro.dao.entity.console.Menu;
import com.personal.springboot.shiro.utils.CustomerMapper;

public interface MenuMapper extends CustomerMapper<Menu> {
    Set<String> findMenuCodeByUserId(String userId);
    Set<String> getALLMenuCode();
    List<Menu> selectMenuByAdminId(String userId);
    List<Menu> selectAllMenu();
    List<Menu> selectMenuByRoleId(String roleId);
}