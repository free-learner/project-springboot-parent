package com.personal.springboot.common.entity;

import java.sql.Timestamp;

import org.apache.ibatis.type.Alias;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 基础BASE实体
 * 
 * @Author LiuBao
 * @Version 2.0 2017年4月7日
 */
@Alias("baseEntity")
public abstract class BaseEntity extends ToString {
    private static final long serialVersionUID = 5396865564545888757L;
    // 主键id
    private Long id;
    // 业务code
    private String code;
    // 删除标记位0,未删除,1,删除
    private Boolean delFlag;
    // 创建人
    private String createBy;
    // 更新人
    private String updateBy;
    // 创建时间
    private Timestamp createDate;
    // 更新时间
    private Timestamp updateDate;
    
    public BaseEntity() {
    }

    @JSONField(serialize=false)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    @JSONField(serialize=false)
    public Boolean getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(Boolean delFlag) {
        this.delFlag = delFlag;
    }

    @JSONField(serialize=false)
    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy == null ? null : createBy.trim();
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy == null ? null : updateBy.trim();
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Timestamp getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Timestamp createDate) {
        this.createDate = createDate;
    }

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
    public Timestamp getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Timestamp updateDate) {
        this.updateDate = updateDate;
    }

}
