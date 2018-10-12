/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.dao.mapper.member;

import com.personal.springboot.shiro.dao.entity.member.Member;
import com.personal.springboot.shiro.utils.CustomerMapper;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:32
 */
public interface MemberMapper extends CustomerMapper<Member> {
    Member selectByUsername(String username);
}
