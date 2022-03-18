package com.example.common.exception;

public enum BizCodeEnum {
    UNKNOWN_EXCETION(10010,"系统未知错误"),
    VALID_EXCEPTION(10000,"参数错误");

    private Integer code;
    private String msg;

    BizCodeEnum(int code ,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public Integer getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
