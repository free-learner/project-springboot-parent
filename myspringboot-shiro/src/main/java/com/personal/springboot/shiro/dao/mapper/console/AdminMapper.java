/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.dao.mapper.console;

import com.personal.springboot.shiro.dao.entity.console.Admin;
import com.personal.springboot.shiro.utils.CustomerMapper;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:32
 */
public interface AdminMapper extends CustomerMapper<Admin> {
    Admin selectByUsername(String username);
    void deleteById(String Id);
}
