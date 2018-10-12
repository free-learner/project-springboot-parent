package com.yh.loan.front.test.idcard;

import com.personal.springboot.common.utils.idcard.Field;
import com.personal.springboot.common.utils.idcard.SeqCode;
import com.personal.springboot.common.utils.idcard.Sex;

public class TestSeqCode {
	
	public static void main(String[]args) {
		Field seqCode = new SeqCode("001");	// invalid
		System.out.println(seqCode.isValid());
		
		seqCode = new SeqCode("222", Sex.male);	// invalid
		System.out.println(seqCode.isValid());
		
		seqCode = new SeqCode("346", Sex.female);	// valid
		System.out.println(seqCode.isValid());
	}

}
