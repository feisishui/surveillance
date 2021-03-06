package com.casic.sgjg.core.util;

/**
 * 任何api或者dao方法的执行结果数据结构
 * <p/>
 * 包含执行是否成功的信息和错误提示。不包含特定额外的返回值，需要额外返回值的请使用{@link ExecResult}
 * Created by wp on 2016/4/25.
 */
public class ExecInfo {

    private static final ExecInfo SUCC_EMPTY = new ExecInfo(true, "");

    private boolean succ;

    private String msg;

    public static ExecInfo fail(String message) {
        return new ExecInfo(false, message);
    }

    public static ExecInfo succ() {
        return SUCC_EMPTY;
    }

    public static ExecInfo succ(String message) {
        return new ExecInfo(true, message);
    }

    private ExecInfo(boolean succ, String msg) {
        this.succ = succ;
        this.msg = msg;
    }

    public boolean isSucc() {
        return succ;
    }

    public String getMsg() {
        return msg;
    }

}
