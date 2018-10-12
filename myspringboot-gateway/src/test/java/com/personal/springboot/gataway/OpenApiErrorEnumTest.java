package com.personal.springboot.gataway;

import com.personal.springboot.gataway.conf.OpenApiErrorEnum;

public class OpenApiErrorEnumTest {

	public static void main(String[] args) {
		OpenApiErrorEnum[] oaees = OpenApiErrorEnum.values();
		for(OpenApiErrorEnum oaee:oaees){
			System.out.println(oaee.getErrCode()+"||"+oaee.getErrMsg());
		}
	}

}
