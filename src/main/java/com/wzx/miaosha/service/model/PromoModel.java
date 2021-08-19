package com.wzx.miaosha.service.model;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/19 下午6:15
 */
public class PromoModel {

    private Integer id;

    // 1. 未开始 2. 已开始 3. 已结束
    private Integer status;

    private String promoName;

    private Date startTime;

    private Date endTime;

    // 适用的秒杀产品id
    private Integer itemId;

    // 秒杀价格
    private BigDecimal promoPrice;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getPromoName() {
        return promoName;
    }

    public void setPromoName(String promoName) {
        this.promoName = promoName;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public BigDecimal getPromoPrice() {
        return promoPrice;
    }

    public void setPromoPrice(BigDecimal promoPrice) {
        this.promoPrice = promoPrice;
    }
}
