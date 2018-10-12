package com.yh.loan.front.test.idcard;

import com.personal.springboot.common.utils.idcard.Field;

public class TestField {
	
	public static void main(String[]args) {
		Field lf = new Field();
		System.out.println(lf.isValid());
		
		Field lf2 = new Field("123", 3, "\\d{3}");
		System.out.println(lf2.isValid());
	}

}
