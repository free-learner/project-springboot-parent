package com.personal.springboot.controller.aop;

public enum LogOperation {

	FRONT("FRONT", "前置管理系统"), 
	MANAGER("MANAGER", "风控管理系统"), 
	TOPCMS("TOPCMS", "信贷管理系统");

    public static final String CONTROLLER = "CONTROLLER";
    public static final String SERVICE  = "SERVICE";
	public static final String DAO = "DAO";
	public static final String OUTER = "OUTER";

	public String moduleCode;
	public String moduleName;

	LogOperation(String moduleCode, String moduleName) {
		this.moduleCode = moduleCode;
		this.moduleName = moduleName;
	}

}