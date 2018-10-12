/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.controller.console;

import java.util.List;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.personal.springboot.shiro.dao.entity.member.Member;
import com.personal.springboot.shiro.service.member.MemberService;
import com.personal.springboot.shiro.utils.ReturnUtil;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:58
 */
@Controller
@RequestMapping("/console/member")
public class UserController {

    @Autowired
    private MemberService memberService;

    @RequiresPermissions("member:index")
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "console/member/index";
    }

    @RequiresPermissions("member:index")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Member member) {
        ModelMap map = new ModelMap();
        List<Member> Lists = memberService.getPageList(member);
        map.put("pageInfo", new PageInfo<Member>(Lists));
        map.put("queryParam", member);
        return ReturnUtil.Success("加载成功", map, null);
    }

}
