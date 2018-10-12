/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.controller.console;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.personal.springboot.shiro.dao.entity.console.Log;
import com.personal.springboot.shiro.service.console.LogService;
import com.personal.springboot.shiro.utils.ReturnUtil;

/**
 * author geekcattle
 * date 2017/1/6 0006 上午 11:35
 */
@Controller
@RequestMapping("/console/log")
public class LogController {

    @Autowired
    private LogService logService;

    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "console/log/index";
    }

    @ResponseBody
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    public ModelMap list(Log log) {
        ModelMap map = new ModelMap();
        List<Log> Lists = logService.getPageList(log);
        map.put("pageInfo", new PageInfo<Log>(Lists));
        map.put("queryParam", log);
        return ReturnUtil.Success("加载成功", map, null);
    }
}
