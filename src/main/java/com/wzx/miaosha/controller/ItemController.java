package com.wzx.miaosha.controller;

import com.wzx.miaosha.controller.vo.ItemVO;
import com.wzx.miaosha.response.CommonResponseType;
import com.wzx.miaosha.service.ItemService;
import com.wzx.miaosha.service.model.ItemModel;
import com.wzx.miaosha.service.model.PromoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author wangzx
 * email wangzx22@163.com
 * date 2021/8/18 下午6:36
 */
@RestController
@RequestMapping(value = "/item")
@CrossOrigin(allowCredentials = "true", originPatterns = "*")
public class ItemController {

    @Autowired
    ItemService itemService;

    @Autowired
    HttpServletRequest request;

    private static final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @PostMapping("/create")
    public CommonResponseType<ItemVO> createItem(@RequestParam(value = "title") String title,
                                                 @RequestParam(value = "description") String description,
                                                 @RequestParam(value = "stock") Integer stock,
                                                 @RequestParam(value = "price") BigDecimal price,
                                                 @RequestParam(value = "imageUrl") String imageUrl
    ) {
        ItemModel itemModel = new ItemModel();
        itemModel.setStock(stock);
        itemModel.setTitle(title);
        itemModel.setDescription(description);
        itemModel.setPrice(price);
        itemModel.setImageUrl(imageUrl);
        ItemModel item = itemService.createItem(itemModel);

        ItemVO itemVO = convertFromItemModel(item);
        return CommonResponseType.createSuccess(itemVO);
    }

    private ItemVO convertFromItemModelDetails(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        PromoModel promoModel = itemModel.getPromoModel();
        if (promoModel != null) {
            itemVO.setStartTime(promoModel.getStartTime());
            itemVO.setEndTime(promoModel.getEndTime());
            itemVO.setPromoStatus(promoModel.getStatus());
            itemVO.setPromoPrice(promoModel.getPromoPrice());
            itemVO.setPromoId(promoModel.getId());
        } else {
            itemVO.setPromoStatus(0);
        }
        return itemVO;
    }

    private ItemVO convertFromItemModel(ItemModel itemModel) {
        if (itemModel == null) {
            return null;
        }
        ItemVO itemVO = new ItemVO();
        BeanUtils.copyProperties(itemModel, itemVO);
        return itemVO;
    }

    /**
     * 返回所有的商品
     *
     * @return
     */
    @GetMapping("/list")
    public CommonResponseType<List<ItemVO>> list() {
        List<ItemModel> res = itemService.getAllItem();
        List<ItemVO> collect = res.stream().map(this::convertFromItemModel).collect(Collectors.toList());
        return CommonResponseType.createSuccess(collect);
    }


    @GetMapping("/detail")
    public CommonResponseType<ItemVO> getItemById(@RequestParam(value = "id") Integer id) {
        ItemModel itemById = itemService.getItemById(id);
        ItemVO itemVO = convertFromItemModelDetails(itemById);
        return CommonResponseType.createSuccess(itemVO);
    }
}
