package com.wzx.miaosha.service;

import com.wzx.miaosha.service.model.ItemModel;

import java.util.List;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/18 下午8:33
 */
public interface ItemService {

    // 创建商品
    ItemModel createItem(ItemModel itemModel);

    // 商品浏览
    List<ItemModel> getAllItem();

    // 商品详情浏览?
    ItemModel getItemById(Integer id);
}
