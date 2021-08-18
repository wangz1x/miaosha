package com.wzx.miaosha.exception;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午3:02
 */
public enum BusinessErrorEnum implements CommonError{

    PARAMETER_VALIDATION_ERROR(10001, "参数不合法"),

    UNKNOWN_ERROR(10002, "未知错误"),

    USER_NOT_EXIST(20001, "用户不存在"),

    REPEATED_USER(20002, "手机号已注册")

    ;

    public void test() {
        BusinessErrorEnum[] values = values();
        for (BusinessErrorEnum value : values) {

        }
    }

    private int errorCode;
    private String errorMsg;

    BusinessErrorEnum(int errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    @Override
    public int getErrorCode() {
        return this.errorCode;
    }

    @Override
    public String getErrorMsg() {
        return this.errorMsg;
    }

    /**
     * 详细描述具体哪些参数不合法
     * @param errorMsg
     * @return
     */
    @Override
    public CommonError setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }
}
