package com.personal.springboot.common.utils.idcard;

public class AddressCode extends Field {

    private final static String addressCodePattern = "\\d{6}";
    private final static int addressCodeLength = 6;

    public AddressCode(String addressCode) {
        fieldVal = addressCode;
        pattern = addressCodePattern;
        fieldLength = addressCodeLength;
    }

}
