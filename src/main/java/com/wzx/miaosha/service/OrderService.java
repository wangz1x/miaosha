package com.wzx.miaosha.service;

import com.wzx.miaosha.service.model.OrderModel;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午3:35
 */
public interface OrderService {

    /**
     * 创建订单
     * @param userId 用户id
     * @param itemId 商品id
     * @param amount 购买数量
     * @return
     */
    OrderModel createOrder(Integer userId, Integer itemId, Integer amount);
}
