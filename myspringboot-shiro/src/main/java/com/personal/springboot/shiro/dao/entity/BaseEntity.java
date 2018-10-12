/*
 * Copyright (c) 2017 <l_iupeiyu@qq.com> All rights reserved.
 */

package com.personal.springboot.shiro.dao.entity;

import javax.persistence.Transient;

/**
 * 基础信息
 */
public class BaseEntity {

    @Transient
    private Integer offset = 0;

    @Transient
    private Integer limit = 10;

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
