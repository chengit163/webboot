package com.cit.web.common.constant;

public enum Code
{
    UNKNOWN(-1, "未知错误"),
    SUCCESS(0, "成功"),
    INVALID(1, "非法参数"),
    //
    HTTP_403(403, "Forbidden"),
    HTTP_500(500, "Internal Server Error"),
    //
    UNAUTHORIZED(10000, "用户名或密码错误"),

    //
    ;
    /**
     * 错误码
     */
    private Integer code;
    /**
     * message
     */
    private String msg;

    Code(Integer code, String msg)
    {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode()
    {
        return code;
    }

    public String getMsg()
    {
        return msg;
    }
}
