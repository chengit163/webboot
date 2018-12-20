package com.cit.web.common.exception;

import com.cit.web.common.constant.Code;

public class AceException extends RuntimeException
{
    private Code code;

    public AceException(Code code)
    {
        super(code.getMsg());
        this.code = code;
    }

    public Code getCode()
    {
        return code;
    }
}
