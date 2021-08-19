package com.wzx.miaosha.service.impl;

import com.wzx.miaosha.dao.ItemDOMapper;
import com.wzx.miaosha.dao.OrderDOMapper;
import com.wzx.miaosha.dao.SequenceDOMapper;
import com.wzx.miaosha.dataobject.OrderDO;
import com.wzx.miaosha.dataobject.SequenceDO;
import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.service.ItemService;
import com.wzx.miaosha.service.OrderService;
import com.wzx.miaosha.service.UserService;
import com.wzx.miaosha.service.model.ItemModel;
import com.wzx.miaosha.service.model.OrderModel;
import com.wzx.miaosha.service.model.UserModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午3:47
 */

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    ItemService itemService;

    @Autowired
    UserService userService;

    @Autowired
    OrderService orderService;

    @Autowired
    OrderDOMapper orderDOMapper;

    @Autowired
    ItemDOMapper itemDOMapper;

    @Autowired
    SequenceDOMapper sequenceDOMapper;

    @Override
    @Transactional
    public OrderModel createOrder(Integer userId, Integer itemId, Integer amount) {

        // 1. 校验
        ItemModel itemModel = itemService.getItemById(itemId);
        if (itemModel == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "商品信息不存在");
        }

        UserModel userModel = userService.queryUserById(userId);
        if (userModel == null) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "用户信息不存在");
        }

        if (amount < 1 || amount > 5) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, "购买数量出错了");
        }

        // 2. 落单减库存
        boolean result = itemService.decreaseStock(itemId, amount);
        if (!result) {
            throw new BusinessException(BusinessErrorEnum.STOCK_NOT_ENOUGH);
        }

        // 3. 订单入库
        OrderModel orderModel = new OrderModel();
        orderModel.setUserId(userId);
        orderModel.setItemId(itemId);
        orderModel.setAmount(amount);
        orderModel.setItemPrice(itemModel.getPrice());
        orderModel.setOrderPrice(itemModel.getPrice().multiply(BigDecimal.valueOf(amount)));
        orderModel.setId(generateOrderNumber());
        OrderDO orderDO = convertFromOrderModel(orderModel);
        orderDOMapper.insertSelective(orderDO);

        // 4. 增加销量
        itemDOMapper.increaseSales(itemId, amount);
        return orderModel;
    }


    /**
     * 私有方法会失效
     * @return
     */
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public String generateOrderNumber() {
        StringBuilder sb = new StringBuilder();
        int sequenceBit = 6;
        // 1. 前8位为日期
        LocalDateTime localDateTime = LocalDateTime.now();
        String date = localDateTime.format(DateTimeFormatter.ISO_DATE).replaceAll("-", "");
        sb.append(date);

        // 2. 自增序列
        SequenceDO sequenceDO = sequenceDOMapper.getSequenceByName("order_info");
        int sequence = sequenceDO.getCurrentValue();
        sequenceDO.setCurrentValue(sequence + sequenceDO.getStep());
        sequenceDOMapper.updateByPrimaryKeySelective(sequenceDO);

        String s = String.valueOf(sequence);
        for (int i = 0; i < sequenceBit - s.length(); i++) {
            sb.append("0");
        }
        sb.append(s);

        // 3. 分库分表
        sb.append("01");

        return sb.toString();
    }

    private OrderDO convertFromOrderModel(OrderModel orderModel) {
        if (orderModel == null) {
            return null;
        }
        OrderDO orderDO = new OrderDO();
        BeanUtils.copyProperties(orderModel, orderDO);
        orderDO.setItemPrice(orderModel.getItemPrice().doubleValue());
        orderDO.setOrderPrice(orderModel.getOrderPrice().doubleValue());
        return orderDO;
    }
}
