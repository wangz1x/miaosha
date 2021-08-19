package com.wzx.miaosha.service.impl;

import com.wzx.miaosha.dao.PromoDOMapper;
import com.wzx.miaosha.dataobject.PromoDO;
import com.wzx.miaosha.service.PromoService;
import com.wzx.miaosha.service.model.PromoModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午6:44
 */

@Service
public class PromoServiceImpl implements PromoService {

    @Autowired
    PromoDOMapper promoDOMapper;

    @Override
    public PromoModel queryPromoByItemId(Integer itemId) {
        PromoDO promoDO = promoDOMapper.selectByItemId(itemId);
        PromoModel promoModel = convertFromPromoDO(promoDO);
        if (promoModel == null) {
            return null;
        }
        Date now = new Date();
        // 活动时间在后边, 即还没开始
        if (promoModel.getStartTime().after(now)) {
            promoModel.setStatus(1);
        } else if (promoModel.getEndTime().after(now)) {
            promoModel.setStatus(2);
        } else {
            promoModel.setStatus(3);
        }
        return promoModel;
    }

    private PromoModel convertFromPromoDO(PromoDO promoDO) {
        if (promoDO == null) {
            return null;
        }
        PromoModel promoModel = new PromoModel();
        // 会忽略 promoPrice promoStatus
        BeanUtils.copyProperties(promoDO, promoModel);
        promoModel.setPromoPrice(BigDecimal.valueOf(promoDO.getPromoPrice()));
        return promoModel;
    }
}
