package com.wzx.miaosha.service;

import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.service.model.UserModel;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午12:40
 */
public interface UserService {

    UserModel queryUserById(Integer id);

    void register(UserModel userModel) throws BusinessException;
}
