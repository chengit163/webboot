package com.cit.web.common.constant;

public enum Method
{
    UNKNOWN(-1, "UNKNOWN"),
    GET(0, "GET"),
    POST(1, "POST"),
    PUT(2, "PUT"),
    DELETE(3, "DELETE"),
    HEADER(4, "HEADER"),
    OPTIONS(5, "OPTIONS"),
    //
    ;

    private Integer key;

    private String val;

    Method(Integer key, String val)
    {
        this.key = key;
        this.val = val;
    }

    public Integer getKey()
    {
        return key;
    }

    public String getVal()
    {
        return val;
    }

    public static Method getMethod(String method)
    {
        if (GET.val.equalsIgnoreCase(method))
        {
            return GET;
        } else if (POST.val.equalsIgnoreCase(method))
        {
            return POST;
        } else if (PUT.val.equalsIgnoreCase(method))
        {
            return PUT;
        } else if (DELETE.val.equalsIgnoreCase(method))
        {
            return DELETE;
        } else if (HEADER.val.equalsIgnoreCase(method))
        {
            return HEADER;
        } else if (OPTIONS.val.equalsIgnoreCase(method))
        {
            return OPTIONS;
        } else
        {
            return UNKNOWN;
        }
    }
}
