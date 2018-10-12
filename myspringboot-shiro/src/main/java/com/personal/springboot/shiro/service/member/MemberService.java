/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.service.member;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.pagehelper.PageHelper;
import com.personal.springboot.shiro.dao.entity.member.Member;
import com.personal.springboot.shiro.dao.mapper.member.MemberMapper;
import com.personal.springboot.shiro.utils.CamelCaseUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * author geekcattle
 * date 2017/3/23 0023 上午 11:25
 */
@Service
public class MemberService {

    @Autowired
    private MemberMapper memberMapper;

    public List<Member> getPageList(Member member) {
        PageHelper.offsetPage(member.getOffset(), member.getLimit(), CamelCaseUtil.toUnderlineName(member.getSort())+" "+member.getOrder());
        return memberMapper.selectAll();
    }

    public Integer getCount(Example example){
        return memberMapper.selectCountByExample(example);
    }

    public Member findByUsername(String username) {
        return memberMapper.selectByUsername(username);
    }

    public void insert(Member member){
        memberMapper.insert(member);
    }
}
