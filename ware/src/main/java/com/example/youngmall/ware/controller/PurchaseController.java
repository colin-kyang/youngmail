package com.example.youngmall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.example.youngmall.ware.entity.MergeVo;
import com.example.youngmall.ware.entity.Vo.PurchaseDone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.ware.entity.PurchaseEntity;
import com.example.youngmall.ware.service.PurchaseService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 采购信息
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-03 10:03:07
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;



    @GetMapping("unreceive/list")
    public R findUnreceive(@RequestParam Map<String,Object> params){
        PageUtils page = purchaseService.finUnreceive(params);
        return R.ok().put("page",page);
    }


    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 合并整单
     * @param mergeVo
     * @return
     */
    @PostMapping("/merge")
    public R mergePurchase(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 领取采购单
     * @param list
     * @return
     */
    @PostMapping("/received")
    public R receivePurchase(@RequestParam List<Long> list){
        purchaseService.received(list);
        return R.ok();
    }


    @PostMapping("/done")
    public R done(@RequestBody PurchaseDone purchaseDone){
        purchaseService.purchaseHasDone(purchaseDone);
        return R.ok();
    }

}
