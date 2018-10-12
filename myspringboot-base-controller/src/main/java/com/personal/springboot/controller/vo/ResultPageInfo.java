package com.personal.springboot.controller.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * 统一返回数据实体类<分页结果>
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月10日
 */
public class ResultPageInfo<T> extends ResultInfo<T> {
    private static final long serialVersionUID = -4112055105377876700L;
    //当前页码
    private Integer pageIndex;
    //每页显示记录条数
    private Integer pageSize;
    //总记录条数
    private Integer totalCount;
    //总页数
    private Integer totalPages;
    
    public ResultPageInfo() {
        super();
    }
    
    public ResultPageInfo<T> buildSuccess(Integer pageIndex, Integer pageSize, Integer totalCount, T data) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        if (pageSize < 0) {
            this.pageSize = 20;
        }
        if (pageIndex < 1) {
            this.pageIndex = 1;
        }
        this.setData(data);
        return this;
    }
    
    @JSONField(name = "totalPages")
    public Integer getTotalPages() {
        this.totalPages = this.totalCount % this.pageSize == 0 ? this.totalCount
                / this.pageSize : this.totalCount / this.pageSize + 1;
        return totalPages;
    }
    
    public void setPageIndex(Integer pageIndex) {
        this.pageIndex = pageIndex;
    }
    
    @JSONField(name = "pageIndex")
    public Integer getPageIndex() {
        return pageIndex;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
    
    @JSONField(name = "pageSize")
    public Integer getPageSize() {
        return pageSize;
    }

    public void setTotalCount(Integer totalCount) {
        this.totalCount = totalCount;
    }
    
    @JSONField(name = "totalCount")
    public Integer getTotalCount() {
        return totalCount;
    }
    
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}
