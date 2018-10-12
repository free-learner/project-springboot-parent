package com.personal.springboot.common.utils.idcard;

/**
 * 顺序码 
 * 顺序码（身份证第十五位到十七位）为同一地址码所标识的区域范围内， 
 * 对同年、月、日出生的人员编定的顺序号。其中第十七位奇数分给男性， 
 * 偶数分给女性。
 * @author Administrator
 */
public class SeqCode extends Field {

	private Sex sex;
	private final static Sex DEFAULT_SEX = Sex.female; 
	private final static String seqCodePattern = "\\d{3}";
	private final static int seqCodeLength = 3;

	public SeqCode(String seqCode) {
		fieldVal = seqCode;
		pattern = seqCodePattern;
		fieldLength = seqCodeLength;
		this.sex = DEFAULT_SEX;
	}

	public SeqCode(String seqCode, Sex sex) {
		fieldVal = seqCode;
		pattern = seqCodePattern;
		fieldLength = seqCodeLength;
		this.sex = sex;
	}

	public boolean isValid() {
		boolean isValid = false;
		if(super.isValid()) {
			int thirdDigitNum = Integer.parseInt(fieldVal.charAt(2)+"");
			
			boolean maleValid = (sex==Sex.male) && (thirdDigitNum%2==1);
			boolean femaleValid = (sex==Sex.female) && (thirdDigitNum%2==0);
			
			if(maleValid || femaleValid) {
				isValid = true;
			}
		}
		
		return isValid;
	}
	
	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}

}
