package com.example.youngmall.coupon.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.common.to.SkuReductionTO;
import com.example.youngmall.coupon.entity.SkuLadderEntity;
import com.example.youngmall.coupon.service.SkuLadderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.coupon.entity.SkuFullReductionEntity;
import com.example.youngmall.coupon.service.SkuFullReductionService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 商品满减信息
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-27 16:32:10
 */
@RestController
@RequestMapping("coupon/skufullreduction")
@Slf4j
public class SkuFullReductionController {
    @Autowired
    private SkuFullReductionService skuFullReductionService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = skuFullReductionService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		SkuFullReductionEntity skuFullReduction = skuFullReductionService.getById(id);

        return R.ok().put("skuFullReduction", skuFullReduction);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.save(skuFullReduction);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody SkuFullReductionEntity skuFullReduction){
		skuFullReductionService.updateById(skuFullReduction);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		skuFullReductionService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    @PostMapping("/savereduction")
    public R saveFullReduction(@RequestBody SkuReductionTO skuReductionTO)
    {
        log.info(skuReductionTO.toString());
        skuFullReductionService.saveFullReduction(skuReductionTO);
        return R.ok();
    }

}
