package com.wzx.miaosha.controller;

import com.wzx.miaosha.constant.Constant;
import com.wzx.miaosha.controller.vo.UserVO;
import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.response.CommonResponseType;
import com.wzx.miaosha.service.UserService;
import com.wzx.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/13 下午12:35
 */

@RestController
@RequestMapping("/user")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    UserService userService;

    @Autowired
    HttpServletRequest request;

    @Autowired
    RedisTemplate redisTemplate;

    @PostMapping("/login")
    public CommonResponseType<String> login(@RequestParam(value = "telphone") String telphone,
                                            @RequestParam(value = "password") String password) throws BusinessException {
        // 验证
        if (StringUtils.isEmpty(telphone) || StringUtils.isEmpty(password)) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "手机号或密码为空");
        }
        UserModel userModel = userService.validateLogin(telphone, getMD5(password));

//        request.getSession().setAttribute("LOGIN_USER", userModel);
//        request.getSession().setAttribute("IS_LOGIN", true);
//        logger.info("LOGIN_USER: {}", request.getSession().getAttribute("LOGIN_USER"));
//        logger.info("IS_LOGIN: {}", request.getSession().getAttribute("IS_LOGIN"));

        // 把生成的token放到redis中, 受不了这个气
        String token = UUID.randomUUID().toString().replace("-", "");
        redisTemplate.opsForValue().set(token, userModel, Duration.ofHours(1));

        return CommonResponseType.createSuccess(token);
    }

    @PostMapping("/register")
    public CommonResponseType<String> register(@RequestParam(value = "telphone") String telphone,
                                               @RequestParam(value = "otpCode") String otpCode,
                                               @RequestParam(value = "name") String name,
                                               @RequestParam(value = "password") String password,
                                               @RequestParam(value = "age") Integer age,
                                               @RequestParam(value = "gender") Integer gender) throws BusinessException {
        // 验证手机号和optcode是否符合
        String attribute = (String) request.getSession().getAttribute(telphone);
        boolean equals = StringUtils.equals(attribute, otpCode);
        if (!equals) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "验证码错误!!!");
        }
        // 注册流程
        UserModel userModel = new UserModel();
        userModel.setName(name);
        userModel.setAge(age);
        userModel.setGender(new Byte(String.valueOf(gender)));
        userModel.setEncryptPassword(getMD5(password));
        userModel.setTelphone(telphone);
        logger.info("register userModel: {}", userModel);
        userService.register(userModel);
        // 到这里就服务器处理来说都是成功的
        return CommonResponseType.createSuccess(null);
    }

    @GetMapping("/{id}")
    public CommonResponseType<UserVO> queryUserById(@PathVariable("id") Integer id
    ) throws BusinessException {
        UserModel userModel = userService.queryUserById(id);
        UserVO userVO = convertFromModel(userModel);
        return CommonResponseType.createSuccess(userVO);
    }

    /**
     * opt: one-time password
     */
    @PostMapping("/getotp")
    public CommonResponseType<Object> produceOpt(@RequestParam(value = "telphone") String telphone,
                                                 HttpSession httpSession) {
        Random random = new Random(System.currentTimeMillis());
        int opt = random.nextInt(99999) + 10000;
        String optStr = String.valueOf(opt);
        request.getSession().setAttribute(telphone, optStr);
        logger.info("telphone: {} optStr: {}", telphone, optStr);
        Map<String, String> data = new HashMap<>();
        data.put("telphone", telphone);
        data.put("optStr", optStr);
        return CommonResponseType.createSuccess(data);
    }

    private UserVO convertFromModel(UserModel userModel) throws BusinessException {
        if (userModel == null) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_EXIST);
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(userModel, userVO);
        return userVO;
    }

    /**
     * MD5 加密密码
     *
     * @param password
     * @return
     */
    private String getMD5(String password) {
        String base = Constant.SALT + "/" + password;
        return DigestUtils.md5DigestAsHex(base.getBytes());
    }
}
