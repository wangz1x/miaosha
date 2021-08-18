package com.wzx.miaosha.exception;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午2:58
 */
public interface CommonError {

    int getErrorCode();

    String getErrorMsg();

    CommonError setErrorMsg(String errorMsg);
}
