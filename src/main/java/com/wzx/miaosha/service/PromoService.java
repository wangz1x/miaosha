package com.wzx.miaosha.service;

import com.wzx.miaosha.dataobject.PromoDO;
import com.wzx.miaosha.service.model.PromoModel;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午6:43
 */
public interface PromoService {

    // 根据商品id查询其对应活动
    PromoModel queryPromoByItemId(Integer itemId);
}
