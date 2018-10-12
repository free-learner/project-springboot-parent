package com.personal.springboot.common.utils.idcard;

public class BirthDate extends Field {

    private final static String birthDatePattern = "\\d{8}";
    private final static int birthDateLength = 8;

    public BirthDate(String birthDate) {
        fieldVal = birthDate;
        pattern = birthDatePattern;
        fieldLength = birthDateLength;
    }

}
