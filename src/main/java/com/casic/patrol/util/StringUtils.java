package com.casic.patrol.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/7.
 */
public class StringUtils extends com.casic.patrol.core.util.StringUtils{

    public static final String SYS_USER = "_current_user";
    public static final String USERID = "userId";
    public static final String USERNAME = "userName";
    public static final String LOG_SYS = "系统日志";
    public static final String LOG_RUN = "运行日志";
    public static final String LOG_ERROR = "错误日志";
    public static final String LOG_ALARM = "报警日志";
    public static final String LOG_TASK = "流程操作";
    public static final String LOG_TASK_ERROR = "流程操作报错";
    public static final String LOG_TASK_RELATED = "流程相关操作";

    public static List<Long> ConvertToLongList(String[] array)
    {
        List<Long> list = new ArrayList<Long>();
        for(String str : array)
        {
            list.add(Long.parseLong(str));
        }
        return list;
    }
}
