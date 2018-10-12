package com.personal.springboot.common.aop;

/**
 * 數據源配置定義,读库可能是多个,写库只有一个
 * 
 * @Author  LiuBao
 * @Version 2.0
 *   2017年4月27日
 */
public enum DataSourceType{
//    read("read", "从库"), write("write", "主库");
    WRITE("write", "写库"),READ("read", "读库");
    private String type;
    private String name;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    DataSourceType(String type, String name) {
        this.type = type;
        this.name = name;
    }
    
}