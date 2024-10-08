package com.paymentonboard.exception;

public enum ErrorCode {

    // ------------- AUTH ERROR CODE -----------------
    AUTH101("AUTH101", "You do not have privilege to access this API"),
    AUTH102("AUTH102", "Full authentication is required to access this resource"),

    MERC101("MERC101", "Partner for Merchant not found");

    private String code;
    private String msg;

    private ErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
