package com.cit.web.common.constant;

import java.util.HashMap;

/**
 *
 */
public class R extends HashMap<String, Object>
{
    private static final String CODE = "code";
    private static final String MSG = "msg";

    private static final String VALID = "valid";

    private R()
    {
        super();
    }

    private R(Code code)
    {
        super();
        put(CODE, code.getCode());
        put(MSG, code.getMsg());
    }

    public static R bind(Code code)
    {
        return new R(code);
    }

    public static R success()
    {
        return R.bind(Code.SUCCESS);
    }

    public static R unknown()
    {
        return R.bind(Code.UNKNOWN);
    }

    public static R bind(Integer code, String msg)
    {
        R r = new R();
        r.put(CODE, code);
        r.put(MSG, msg);
        return r;
    }

    public static R validOk()
    {
        R r = new R();
        r.put(VALID, true);
        return r;
    }

    public static R validError()
    {
        R r = new R();
        r.put(VALID, false);
        return r;
    }

}
