package com.wzx.miaosha.response;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午2:18
 */
public class CommonResponseType<T> {
    private int status;
    private String msg;
    private T data;

    public CommonResponseType() {
    }

    public CommonResponseType(T data) {
        this(200, "success", data);
    }

    public CommonResponseType(int status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public CommonResponseType(int status, String msg) {
        this.status = status;
        this.msg = msg;
    }

    public static <TT> CommonResponseType<TT> createSuccess(TT data) {
        return new CommonResponseType<>(data);
    }

    public static <TT> CommonResponseType<TT> create(int status, String msg, TT data) {
        return new CommonResponseType<>(status, msg, data);
    }

    public static <TT> CommonResponseType<TT> create(int status, String msg) {
        return new CommonResponseType<>(status, msg);
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

