package com.personal.springboot.common.utils.idcard;

public class ICNumber {
    private final static AddressCode DEFAULT_ADDRESS_CODE;
    private final static BirthDate DEFAULT_BIRTH_DATE;
    private final static SeqCode DEFAULT_SEQ_CODE;
    static {
        DEFAULT_ADDRESS_CODE = new AddressCode("110108"); // 默认为海淀区的地址码
        DEFAULT_BIRTH_DATE = new BirthDate("19900101");// 默认出生日期为19900101
        DEFAULT_SEQ_CODE  = new SeqCode("128"); // 默认序列码为128
    }

    private AddressCode addressCode = DEFAULT_ADDRESS_CODE;
    private BirthDate birthDate = DEFAULT_BIRTH_DATE;
    private SeqCode seqCode = DEFAULT_SEQ_CODE;
    private String checkDigitStr;   // 校验位字符串
    
    private String first17Digits;

    public ICNumber() {
        first17Digits = addressCode.toString()+birthDate.toString()+seqCode.toString();
        checkDigitStr = caculateCheckDigitStr();
    }

    public ICNumber(AddressCode addressCode, BirthDate birthDate,
            SeqCode seqCode) {
        if (addressCode.isValid() && birthDate.isValid() && seqCode.isValid()) {
            this.addressCode = addressCode;
            this.birthDate = birthDate;
            this.seqCode = seqCode;
            first17Digits = addressCode.toString()+birthDate.toString()+seqCode.toString();
            checkDigitStr = caculateCheckDigitStr();
        }
    }

    /*
     * 第十八位数字（校验码）的计算方法为： 
     * 1.将前面的身份证号码17位数分别乘以不同的系数。从第一位到第十七位的系数分别为：
     * 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
     * 2.将这17位数字和系数相乘的结果相加。 
     * 3.用加出来和除以11，看余数是多少？ 
     * 4. 余数只可能有0 1 2 3 4 5 6 7 8 9 10这11个数字。其分别对应的最后一位身份证
     * 的号码为1 0 X 9 8 7 6 5 4 3 2。 
     * 5.通过上面得知如果余数是2，就会在身份证的第18位数字上出现罗马数字的Ⅹ。
     * 如果余数是10，身份证的最后一位号码就是2。 
     */
    private String caculateCheckDigitStr() {
        // 根据前面17位计算校验位
        long[] digitArray = splitIntoArray(first17Digits);
        long[] factorArray = {7, 9, 10, 5, 8, 4, 2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2};
        long sumOfProduct = 0;
        
        for(int i=0;i<17;i++) {
            sumOfProduct += digitArray[i]*factorArray[i];
        }
        
        long remainder = sumOfProduct%11;   // 整除11的余数
        char[] digitMap = {'1', '0', 'X', '9', '8', '7', '6', '5', '4', '3', '2'};
        
        
        return digitMap[(int)remainder]+"";
    }
    
    private long[] splitIntoArray(String first17Digits) {
        long[] digitArray = new long[17];
        for(int i=0;i<digitArray.length;i++) {
            digitArray[i] = Long.parseLong(first17Digits.charAt(i)+"");
        }
        
        return digitArray;
    }
    

    public String toString() {
        return addressCode.toString() + birthDate.toString()
                + seqCode.toString() + checkDigitStr;
    }
    
    public String getSex() {
        return seqCode.getSex()==Sex.male ? "男" : "女";
    }
    
    // to do
    public static boolean isICNumberValid(String icNumber) {
        return false;
    }
}
