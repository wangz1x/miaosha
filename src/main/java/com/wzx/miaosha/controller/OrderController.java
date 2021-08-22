package com.wzx.miaosha.controller;

import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.response.CommonResponseType;
import com.wzx.miaosha.service.OrderService;
import com.wzx.miaosha.service.model.OrderModel;
import com.wzx.miaosha.service.model.UserModel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

import static com.wzx.miaosha.constant.Constant.CONTENT_TYPE_FORMED;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午4:56
 */
@RestController
@RequestMapping("/order")
@CrossOrigin(originPatterns = "*", allowCredentials = "true")
public class OrderController {


    @Autowired
    HttpServletRequest httpServletRequest;

    @Autowired
    OrderService orderService;

    @Autowired
    RedisTemplate redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);


    @PostMapping(value = "/createorder", consumes = {CONTENT_TYPE_FORMED})
    public CommonResponseType<OrderModel> createOrder(@RequestParam("itemId") Integer itemId,
                                                      @RequestParam("amount") Integer amount,
                                                      @RequestParam(value = "promoId", required = false) Integer promoId
    ) {

//        Boolean is_login = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
//        if (is_login == null || !is_login) {
//            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
//        }
//        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        String token = httpServletRequest.getParameter("token");
        if (StringUtils.isEmpty(token)) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }

        UserModel userModel = (UserModel) redisTemplate.opsForValue().get(token);
        if (userModel == null) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }

        OrderModel order = orderService.createOrder(userModel.getId(), itemId, promoId, amount);

        return CommonResponseType.createSuccess(order);
    }
}
