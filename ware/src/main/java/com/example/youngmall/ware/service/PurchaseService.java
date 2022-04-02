package com.example.youngmall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.youngmall.ware.entity.MergeVo;
import com.example.youngmall.ware.entity.PurchaseEntity;
import com.example.youngmall.ware.entity.Vo.PurchaseDone;

import java.util.List;
import java.util.Map;

/**
 * 采购信息
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-03 10:03:07
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    PageUtils finUnreceive(Map<String, Object> params);

    void mergePurchase(MergeVo mergeVo);

    void received(List<Long> list);

    void purchaseHasDone(PurchaseDone purchaseDone);
}

