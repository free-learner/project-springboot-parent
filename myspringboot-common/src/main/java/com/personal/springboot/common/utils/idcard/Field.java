package com.personal.springboot.common.utils.idcard;

public class Field {

    protected int fieldLength; 
    protected String pattern; 
    protected String fieldVal;

    protected final static int DEFAULT_FIELD_LEN = 7;
    protected final static String DEFAULT_PATTERN = ".{7}";
    protected final static String DEFAULT_FIELD_VAL = "DEFAULT";

    public int getFieldLength() {
        return fieldLength;
    }

    public void setFieldLength(int fieldLength) {
        this.fieldLength = fieldLength;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }

    public String getFieldVal() {
        return fieldVal;
    }

    public void setFieldVal(String fieldVal) {
        this.fieldVal = fieldVal;
    }

    public Field() {
        this(DEFAULT_FIELD_VAL, DEFAULT_FIELD_LEN, DEFAULT_PATTERN);
    }

    public Field(String fieldVal) {
        this(fieldVal, DEFAULT_FIELD_LEN, DEFAULT_PATTERN);
    }

    public Field(String fieldVal, int fieldLength) {
        this(fieldVal, fieldLength, DEFAULT_PATTERN);
    }

    public Field(String fieldVal, int fieldLength, String pattern) {
        fieldVal = (fieldVal != null && (!(fieldVal.equals("")))) ? fieldVal : DEFAULT_FIELD_VAL;
        fieldLength = (fieldLength > 1) ? fieldLength : DEFAULT_FIELD_LEN;
        pattern = (pattern == null) ? DEFAULT_PATTERN : pattern;
    }

    public boolean isValid() {

        if (fieldVal.length() != fieldLength || !fieldVal.matches(pattern))
            return false;

        return true;
    }

    public String toString() {
        return fieldVal;
    }

}
