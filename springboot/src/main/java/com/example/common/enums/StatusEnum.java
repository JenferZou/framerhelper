package com.example.common.enums;

public enum StatusEnum {

    OK("审核通过"),
    NO("审核不通过"),
    ;

    public String status;

    StatusEnum(String status) {
        this.status = status;
    }
}
