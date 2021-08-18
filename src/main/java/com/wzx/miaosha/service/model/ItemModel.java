package com.wzx.miaosha.service.model;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/18 下午6:21
 */
public class ItemModel {

    private Integer id;

    @NotNull(message = "商品名称不能为空")
    private String title;

    @NotNull(message = "商品描述不能为空")
    private String description;

    @NotNull(message = "商品库存不能为空")
    private Integer stock;

    private Integer sales;

    @NotNull(message = "商品价格不能为空")
    @Min(value = 0, message = "商品价格大于0")
    private BigDecimal price;

    @NotNull(message = "商品图片不能为空")
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "ItemModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", stock=" + stock +
                ", sales=" + sales +
                ", price=" + price +
                '}';
    }

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
}
