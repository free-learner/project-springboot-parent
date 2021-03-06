/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.dao.entity.console;

import javax.persistence.*;

@Table(name = "admin_role")
public class AdminRole {
    @Id
    @Column(name = "admin_id")
    private String adminId;

    @Id
    @Column(name = "role_id")
    private String roleId;

    /**
     * @return admin_id
     */
    public String getAdminId() {
        return adminId;
    }

    /**
     * @param adminId
     */
    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    /**
     * @return role_id
     */
    public String getRoleId() {
        return roleId;
    }

    /**
     * @param roleId
     */
    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}