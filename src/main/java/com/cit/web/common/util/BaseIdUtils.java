package com.cit.web.common.util;

public class BaseIdUtils
{
    private static final int MENU_ID_MIN = 1;
    private static final int MENU_ID_MAX = 100;

    private static final int ROLE_ID_MIN = 1;
    private static final int ROLE_ID_MAX = 10;

    private static final int USER_ID_MIN = 1;
    private static final int USER_ID_MAX = 2;


    /**
     * 判断是否基础菜单ID
     *
     * @param id
     * @return
     */
    public static boolean isBaseMenuId(int id)
    {
        return id >= MENU_ID_MIN && id <= MENU_ID_MAX;
    }

    /**
     * 判断是否基础角色ID
     *
     * @param id
     * @return
     */
    public static boolean isBaseRoleId(int id)
    {
        return id >= ROLE_ID_MIN && id <= ROLE_ID_MAX;
    }

    /**
     * 判断是否基础用户ID
     *
     * @param id
     * @return
     */
    public static boolean isBaseUserId(int id)
    {
        return id >= USER_ID_MIN && id <= USER_ID_MAX;
    }
}
