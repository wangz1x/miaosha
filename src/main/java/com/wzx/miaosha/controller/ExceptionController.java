package com.wzx.miaosha.controller;

import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.exception.CommonError;
import com.wzx.miaosha.response.CommonResponseType;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午3:26
 */

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.OK)
    public Object exceptionHandler(Exception ex) {
        CommonResponseType<Object> commonResponseType = new CommonResponseType<>();
        if (ex instanceof BusinessException) {
            commonResponseType.setMsg(((BusinessException) ex).getErrorMsg());
            commonResponseType.setStatus(((BusinessException) ex).getErrorCode());
        } else {
            commonResponseType.setStatus(BusinessErrorEnum.UNKNOWN_ERROR.getErrorCode());
            commonResponseType.setMsg(BusinessErrorEnum.UNKNOWN_ERROR.getErrorMsg());
        }
        ex.printStackTrace();
        return commonResponseType;
    }
}
