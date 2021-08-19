package com.wzx.miaosha.controller;

import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.response.CommonResponseType;
import com.wzx.miaosha.service.OrderService;
import com.wzx.miaosha.service.model.OrderModel;
import com.wzx.miaosha.service.model.UserModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

import static com.wzx.miaosha.constant.Constant.CONTENT_TYPE_FORMED;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午4:56
 */
@RestController
@RequestMapping("/order")
public class OrderController {


    @Autowired
    HttpServletRequest httpServletRequest;


    @Autowired
    OrderService orderService;


    @PostMapping(value = "/createorder", consumes = {CONTENT_TYPE_FORMED})
    public CommonResponseType<OrderModel> createOrder(@RequestParam("itemId") Integer itemId,
                                                  @RequestParam("amount") Integer amount
                                                  ) {
        Boolean is_login = (Boolean) httpServletRequest.getSession().getAttribute("IS_LOGIN");
        if (!is_login) {
            throw new BusinessException(BusinessErrorEnum.USER_NOT_LOGIN);
        }
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute("LOGIN_USER");

        OrderModel order = orderService.createOrder(userModel.getId(), itemId, amount);

        return CommonResponseType.createSuccess(order);
    }
}
