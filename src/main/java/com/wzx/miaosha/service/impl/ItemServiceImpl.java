package com.wzx.miaosha.service.impl;

import com.wzx.miaosha.dao.ItemDOMapper;
import com.wzx.miaosha.dao.ItemStockDOMapper;
import com.wzx.miaosha.dataobject.ItemDO;
import com.wzx.miaosha.dataobject.ItemStockDO;
import com.wzx.miaosha.exception.BusinessErrorEnum;
import com.wzx.miaosha.exception.BusinessException;
import com.wzx.miaosha.service.ItemService;
import com.wzx.miaosha.service.PromoService;
import com.wzx.miaosha.service.model.ItemModel;
import com.wzx.miaosha.service.model.PromoModel;
import com.wzx.miaosha.validator.ValidatorImpl;
import com.wzx.miaosha.validator.ValidatorResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/18 下午8:34
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    ValidatorImpl validator;

    @Autowired
    ItemDOMapper itemDOMapper;

    @Autowired
    ItemStockDOMapper itemStockDOMapper;

    @Autowired
    PromoService promoService;

    /**
     * 一般写入的时候需要保证原子性?
     * @param itemModel
     * @return
     */
    @Override
    @Transactional
    public ItemModel createItem(ItemModel itemModel) {
        // 校验入参
        ValidatorResult validate = validator.validate(itemModel);
        if (validate.isHasErrors()) {
            throw new BusinessException(BusinessErrorEnum.PARAMETER_VALIDATION_ERROR, validate.getErrMsg());
        }

        // 对象转换 Model->DO
        ItemDO itemDO = convertItemDOFromItemModel(itemModel);

        // 写入数据库
        // 写入之前是没有id的, 插入数据库后, 生成的id会写入到ItemDo中?
        itemDOMapper.insertSelective(itemDO);
        itemModel.setId(itemDO.getId());
        ItemStockDO itemStockDO = covertItemStockFromItemModel(itemModel);
        itemStockDOMapper.insertSelective(itemStockDO);

        // 返回创建完成的对象, 为啥不能直接这样..
        // return itemModel;
        return getItemById(itemModel.getId());
    }

    /**
     * 模型转换
     *
     * @param itemModel
     * @return
     */
    private ItemDO convertItemDOFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemDO itemDO = new ItemDO();
        BeanUtils.copyProperties(itemModel, itemDO);
        // 注意price类型不同
        itemDO.setPrice(itemModel.getPrice().doubleValue());
        return itemDO;
    }

    private ItemStockDO covertItemStockFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemStockDO itemStockDO = new ItemStockDO();
        itemStockDO.setItemId(itemModel.getId());
        itemStockDO.setStock(itemModel.getStock());
        return itemStockDO;
    }


    @Override
    public List<ItemModel> getAllItem() {
        List<ItemDO> itemModels = itemDOMapper.listItem();
        List<ItemModel> collect = itemModels.stream().map(itemDO -> {
            ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
            ItemModel itemModel = combineFromItemDOAndItemStockDO(itemDO, itemStockDO);
            return itemModel;
        }).collect(Collectors.toList());
        return collect;
    }

    @Override
    public ItemModel getItemById(Integer id) {
        ItemDO itemDO = itemDOMapper.selectByPrimaryKey(id);
        if (itemDO == null) {
            return null;
        }
        // 获取库存
        ItemStockDO itemStockDO = itemStockDOMapper.selectByItemId(itemDO.getId());
        ItemModel itemModel = combineFromItemDOAndItemStockDO(itemDO, itemStockDO);

        // 获取营销活动
        PromoModel promoModel = promoService.queryPromoByItemId(itemModel.getId());
        if (promoModel != null && promoModel.getStatus() != 3) {
            itemModel.setPromoModel(promoModel);
        }
        return itemModel;
    }

    @Override
    @Transactional
    public boolean decreaseStock(Integer itemId, Integer amount) {
        int row = itemStockDOMapper.decreaseStock(itemId, amount);
        if (row > 0) {
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public void increaseSales(Integer itemId, Integer amount) {
        itemDOMapper.increaseSales(itemId, amount);
    }

    private ItemModel combineFromItemDOAndItemStockDO(ItemDO itemDO, ItemStockDO itemStockDO) {
        if (itemDO == null || itemStockDO == null) {
            return null;
        }
        ItemModel itemModel = new ItemModel();
        BeanUtils.copyProperties(itemDO, itemModel);
        itemModel.setPrice(BigDecimal.valueOf(itemDO.getPrice()));
        itemModel.setStock(itemStockDO.getStock());
        return itemModel;
    }
}
