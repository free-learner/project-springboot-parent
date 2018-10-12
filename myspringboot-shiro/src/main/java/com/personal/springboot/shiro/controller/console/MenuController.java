/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.controller.console;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.personal.springboot.shiro.dao.entity.console.Menu;
import com.personal.springboot.shiro.service.console.MenuService;
import com.personal.springboot.shiro.service.console.RoleMenuService;
import com.personal.springboot.shiro.utils.DateUtil;
import com.personal.springboot.shiro.utils.MenuTreeUtil;
import com.personal.springboot.shiro.utils.ReturnUtil;
import com.personal.springboot.shiro.utils.UuidUtil;

import tk.mybatis.mapper.entity.Example;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:58
 */
@Controller
@RequestMapping("/console/menu")
@RequiresAuthentication
public class MenuController {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @RequiresPermissions("menu:index")
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        ArrayList<Menu> menuLists = new ArrayList<>();
        List<Menu> Lists = menuService.getChildMenuList(menuLists,"0");
        model.addAttribute("menus", Lists);
        return "console/menu/index";
    }

    @RequiresPermissions("menu:index")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list() {
        ModelMap map = new ModelMap();
        List<Menu> List = menuService.getMenuAll();
        MenuTreeUtil menuTreeUtil = new MenuTreeUtil(List, null);
        List<Menu> treeGridList = menuTreeUtil.buildTreeGrid();
        map.put("treeList", treeGridList);
        map.put("total", List.size());
        return ReturnUtil.Success("加载成功", map, null);
    }

    @RequiresPermissions("admin:edit")
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String add(Menu menu, Model model) {
        if (StringUtils.isBlank(menu.getParentId())) {
            menu.setParentId("0");
        }
        if (!StringUtils.isBlank(menu.getMenuId())) {
            menu = menuService.getById(menu.getMenuId());
            if (!"null".equals(menu)) {
                menu.setUpdatedAt(DateUtil.getCurrentTime());
            }
        } else {
            menu.setChildNum(0);
            menu.setListorder(0);
            menu.setMenuType("menu");
            menu.setCreatedAt(DateUtil.getCurrentTime());
            menu.setUpdatedAt(DateUtil.getCurrentTime());
        }
        model.addAttribute("menu", menu);
        return "console/menu/from";
    }

    @RequiresPermissions("menu:save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @Transactional
    @ResponseBody
    public ModelMap save(@Valid Menu menu, BindingResult result) {
        try {
            if (result.hasErrors()) {
                for (ObjectError er : result.getAllErrors())
                    return ReturnUtil.Error(er.getDefaultMessage(), null, null);
            }
            if (StringUtils.isBlank(menu.getMenuId())) {
                String Id = UuidUtil.getUUID();
                menu.setMenuId(Id);
                menuService.insert(menu);
            } else {
                menuService.save(menu);
            }
            if(!menu.getParentId().equals("0")){
                //更新父类总数
                Example example = new Example(Menu.class);
                example.createCriteria().andCondition("parent_id = ", menu.getParentId());
                Integer parentCount = menuService.getCount(example);
                Menu parentMenu = menuService.getById(menu.getParentId());
                menuService.getById(menu.getParentId());
                parentMenu.setChildNum(parentCount);
                menuService.save(parentMenu);
            }
            return ReturnUtil.Success("操作成功", null, "/console/menu/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequiresPermissions("menu:listorder")
    @RequestMapping(value = "/listorder", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap updateOrder(String id, Integer listorder) {
        if (StringUtils.isNotBlank(id)) {
            Menu menu = new Menu();
            menu.setListorder(listorder);
            Example example = new Example(Menu.class);
            example.createCriteria()
                    .andCondition("id = ", id);
            menuService.update(menu, example);
            return ReturnUtil.Success("Success", null, null);
        } else {
            return ReturnUtil.Error("Error", null, null);
        }
    }

    @ResponseBody
    @RequiresPermissions("menu:delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    public ModelMap delete(String[] ids) {
        try {
            if ("null".equals(ids) || "".equals(ids)) {
                return ReturnUtil.Error("Error", null, null);
            } else {
                for (String id : ids) {
                    roleMenuService.deleteMenuId(id);
                    menuService.deleteById(id);
                }
                return ReturnUtil.Success("Success", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("Error", null, null);
        }
    }


}
