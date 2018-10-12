/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.controller.console;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.personal.springboot.shiro.dao.entity.console.Menu;
import com.personal.springboot.shiro.dao.entity.console.Role;
import com.personal.springboot.shiro.dao.entity.console.RoleMenu;
import com.personal.springboot.shiro.service.console.AdminRoleService;
import com.personal.springboot.shiro.service.console.MenuService;
import com.personal.springboot.shiro.service.console.RoleMenuService;
import com.personal.springboot.shiro.service.console.RoleService;
import com.personal.springboot.shiro.utils.DateUtil;
import com.personal.springboot.shiro.utils.MenuTreeUtil;
import com.personal.springboot.shiro.utils.ReturnUtil;
import com.personal.springboot.shiro.utils.UuidUtil;

/**
 * author geekcattle
 * date 2016/10/21 0021 下午 15:58
 */
@Controller
@RequestMapping("/console/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminRoleService adminRoleService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @RequiresPermissions("role:index")
    @RequestMapping(value = "/index", method = {RequestMethod.GET})
    public String index(Model model) {
        return "console/role/index";
    }

    @RequiresPermissions("role:index")
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap list(Role role) {
        ModelMap map = new ModelMap();
        List<Role> Lists = roleService.getPageList(role);
        for (Role list : Lists) {
            List<Menu> menuList = menuService.selectMenuByRoleId(list.getRoleId());
            list.setMenuList(menuList);
        }
        map.put("pageInfo", new PageInfo<Role>(Lists));
        map.put("queryParam", role);
        return ReturnUtil.Success("加载成功", map, null);
    }


    @RequiresPermissions("role:edit")
    @RequestMapping(value = "/from", method = {RequestMethod.GET})
    public String from(Role role, Model model) {
        if (!StringUtils.isBlank(role.getRoleId())) {
            role = roleService.getById(role.getRoleId());
        }
        model.addAttribute("role", role);
        return "console/role/from";
    }

    @RequiresPermissions("role:save")
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap save(@Valid Role role, BindingResult result) {
        if (result.hasErrors()) {
            for (ObjectError er : result.getAllErrors()) return ReturnUtil.Error(er.getDefaultMessage(), null, null);
        }
        try {
            if (StringUtils.isBlank(role.getRoleId())) {
                role.setRoleId(UuidUtil.getUUID());
                role.setCreatedAt(DateUtil.getCurrentTime());
                role.setUpdatedAt(DateUtil.getCurrentTime());
                roleService.insert(role);
            } else {
                role.setUpdatedAt(DateUtil.getCurrentTime());
                roleService.save(role);
            }
            return ReturnUtil.Success("操作成功", null, "/console/role/index");
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequiresPermissions("role:delete")
    @RequestMapping(value = "/delete", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap delete(String[] ids) {
        try {
            if ("null".equals(ids) || "".equals(ids)) {
                return ReturnUtil.Error("Error", null, null);
            } else {
                for (String id : ids) {
                    adminRoleService.deleteRoleId(id);
                    roleService.deleteById(id);
                }
                return ReturnUtil.Success("操作成功", null, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequestMapping(value = "/combobox", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelMap comboBox() {
        ModelMap map = new ModelMap();
        List<Role> roleList = roleService.getFromAll();
        map.put("roleList", roleList);
        return ReturnUtil.Success(null, map, null);
    }

    @RequestMapping(value = "/menutree", method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public ModelMap comboTree(String id) {
        List<Menu> menuLists = menuService.getComboTree(null);
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(id);
        List<RoleMenu> roleMenuLists = roleMenuService.getRoleList(roleMenu);
        MenuTreeUtil menuTreeUtil = new MenuTreeUtil(menuLists, roleMenuLists);
        List<Map<String, Object>> mapList = menuTreeUtil.buildTree();
        return ReturnUtil.Success(null, mapList, null);
    }

    @RequiresPermissions("role:grant")
    @RequestMapping(value = "/grant", method = {RequestMethod.POST})
    @ResponseBody
    public ModelMap grant(String roleId, String[] menuIds) {
        try {
            if (menuIds != null && StringUtils.isNotEmpty(roleId)) {
                if (StringUtils.isNotEmpty(menuIds.toString())) {
                    roleMenuService.deleteRoleId(roleId);
                    for (String menuId : menuIds) {
                        RoleMenu roleMenu = new RoleMenu();
                        roleMenu.setMenuId(menuId);
                        roleMenu.setRoleId(roleId);
                        roleMenuService.insert(roleMenu);
                    }
                }
            } else if (menuIds == null && StringUtils.isNotEmpty(roleId)) {
                roleMenuService.deleteRoleId(roleId);
            }
            return ReturnUtil.Success("操作成功", null, null);
        } catch (Exception e) {
            e.printStackTrace();
            return ReturnUtil.Error("操作失败", null, null);
        }
    }

    @RequiresPermissions("role:grant")
    @RequestMapping(value = "/grant", method = {RequestMethod.GET})
    public String grantForm(String roleId, Model model) {
        model.addAttribute("roleId", roleId);
        return "/console/role/grant";
    }

    @RequestMapping(value = "/menulist", method = {RequestMethod.GET})
    @ResponseBody
    public ModelMap menulist(String id) {
        ModelMap map = new ModelMap();
        RoleMenu roleMenu = new RoleMenu();
        roleMenu.setRoleId(id);
        List<RoleMenu> roleMenuLists = roleMenuService.getRoleList(roleMenu);
        ArrayList<String> roleList = new ArrayList<>();
        for (RoleMenu roleMenuList : roleMenuLists) {
            roleList.add(roleMenuList.getMenuId());
        }
        map.put("id", id);
        map.put("roleList", roleList);
        return ReturnUtil.Success("操作成功", map, null);
    }
}
