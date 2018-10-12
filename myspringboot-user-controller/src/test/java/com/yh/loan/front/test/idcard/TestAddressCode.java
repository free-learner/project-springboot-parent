package com.yh.loan.front.test.idcard;

import com.personal.springboot.common.utils.idcard.AddressCode;

public class TestAddressCode {
	
	public static void main(String[]args) {
		AddressCode ac = new AddressCode("000000");
		System.out.println(ac.isValid());
	}

}
