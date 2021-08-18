package com.wzx.miaosha.controller.vo;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/18 下午6:37
 */
public class ItemVO {
    private Integer id;

    private String title;

    private String description;

    private Integer stock;

    private Integer sales;

    private BigDecimal price;

    private String imageUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Integer getSales() {
        return sales;
    }

    public void setSales(Integer sales) {
        this.sales = sales;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
