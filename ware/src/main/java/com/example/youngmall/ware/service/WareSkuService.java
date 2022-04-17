package com.example.youngmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.to.SkuHasStockTo;
import com.example.common.utils.PageUtils;
import com.example.youngmall.ware.entity.WareSkuEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-03 10:03:07
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 商品入库
     * @param skuId
     * @param wareId
     * @param skuNum
     */
    void addStock(Long skuId, Long wareId, Integer skuNum);

    List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds);
}

