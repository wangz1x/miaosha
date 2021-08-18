package com.wzx.miaosha.service.impl;

import com.wzx.miaosha.constant.Constant;
import com.wzx.miaosha.constant.GeneralEnum;
import com.wzx.miaosha.dao.UserDOMapper;
import com.wzx.miaosha.dao.UserPasswordDOMapper;
import com.wzx.miaosha.dataobject.UserDO;
import com.wzx.miaosha.dataobject.UserPasswordDO;
import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.service.UserService;
import com.wzx.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

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
    public void register(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "注册信息为空");
        }
        if (userModel.getAge() == null || userModel.getGender() == null
                || StringUtils.isEmpty(userModel.getName()) || StringUtils.isEmpty(userModel.getTelphone())
                || StringUtils.isEmpty(userModel.getEncryptPassword())) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "注册信息不完整");
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
