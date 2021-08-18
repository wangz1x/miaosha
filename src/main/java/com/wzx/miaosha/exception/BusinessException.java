package com.wzx.miaosha.exception;

/**
 * 包装器业务异常类实现?
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午3:05
 */
public class BusinessException extends RuntimeException{

    private final CommonError commonError;

    public BusinessException(CommonError commonError) {
        super();
        this.commonError = commonError;
    }

    public BusinessException(CommonError commonError, String errorMsg) {
        super();
        this.commonError = commonError.setErrorMsg(errorMsg);
    }

    public int getErrorCode() {
        return commonError.getErrorCode();
    }

    public String getErrorMsg() {
        return commonError.getErrorMsg();
    }
}
