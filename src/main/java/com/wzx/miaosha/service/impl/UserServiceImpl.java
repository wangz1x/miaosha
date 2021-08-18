package com.wzx.miaosha.service.impl;

import com.wzx.miaosha.dao.UserDOMapper;
import com.wzx.miaosha.dao.UserPasswordDOMapper;
import com.wzx.miaosha.dataobject.UserDO;
import com.wzx.miaosha.dataobject.UserPasswordDO;
import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.service.UserService;
import com.wzx.miaosha.service.model.UserModel;
import com.wzx.miaosha.validator.ValidatorImpl;
import com.wzx.miaosha.validator.ValidatorResult;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午12:45
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDOMapper userDOMapper;

    @Autowired
    UserPasswordDOMapper userPasswordDOMapper;

    @Autowired
    ValidatorImpl validator;

    @Override
    public UserModel queryUserById(Integer id) {
        UserDO userDO = userDOMapper.selectByPrimaryKey(id);
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(id);
        return convertFromUserAndPasswordDO(userDO, userPasswordDO);
    }

    /**
     * 注册成功即将用户信息与用户密码入库成功
     * <p>
     * 下边两个不用校验
     * private String registerMode;
     * private String thirdPartyId;
     *
     * @param userModel
     * @return
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void register(UserModel userModel) {
        if (userModel == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "注册信息为空");
        }

        ValidatorResult validate = validator.validate(userModel);
        if (validate.isHasErrors()) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, validate.getErrMsg());
        }

        UserDO userDO = convertFromUserModelToUserDO(userModel);
        try {
            userDOMapper.insertSelective(userDO);
        } catch (DuplicateKeyException ex) {
            throw new BusinessException(BusinessErrorEnum.REPEATED_USER);
        }
        // 插入用户数据后, 获取其主键id
        userModel.setId(userDO.getId());
        UserPasswordDO userPasswordDO = convertFromUserModelToPasswordDO(userModel);
        userPasswordDOMapper.insertSelective(userPasswordDO);
    }

    @Override
    public UserModel validateLogin(String telphone, String password) {

        // 获取用户信息
        UserDO userDO = userDOMapper.selectByTelphone(telphone);
        if (userDO == null) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_EXIST);
        }

        // 再获取用户密码
        UserPasswordDO userPasswordDO = userPasswordDOMapper.selectByUserId(userDO.getId());

        if (!StringUtils.equals(password, userPasswordDO.getEncryptPassword())) {
            throw new BusinessException(BusinessErrorEnum.NOT_MATCH_ERROR);
        }
        return convertFromUserAndPasswordDO(userDO, userPasswordDO);
    }

    /**
     * 对象转换
     *
     * @param userModel
     * @return
     */
    private UserDO convertFromUserModelToUserDO(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserDO userDO = new UserDO();
        BeanUtils.copyProperties(userModel, userDO);
        return userDO;
    }

    /**
     * 对象转换
     *
     * @param userModel
     * @return
     */
    private UserPasswordDO convertFromUserModelToPasswordDO(UserModel userModel) {
        if (userModel == null) {
            return null;
        }
        UserPasswordDO userPasswordDO = new UserPasswordDO();
        userPasswordDO.setUserId(userModel.getId());
        userPasswordDO.setEncryptPassword(userModel.getEncryptPassword());
        return userPasswordDO;
    }



    /**
     * 对象合并转换
     *
     * @param userDO
     * @param userPasswordDO
     * @return
     */
    private UserModel convertFromUserAndPasswordDO(UserDO userDO, UserPasswordDO userPasswordDO) {
        if (userDO == null) {
            return null;
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(userDO, userModel);

        if (userPasswordDO != null) {
            userModel.setEncryptPassword(userPasswordDO.getEncryptPassword());
        }
        return userModel;
    }
}
