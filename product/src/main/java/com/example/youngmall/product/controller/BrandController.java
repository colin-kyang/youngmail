package com.example.youngmall.product.controller;

import java.util.Arrays;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.BrandEntity;
import com.example.youngmall.product.service.BrandService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 品牌
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
@RestController
@RequestMapping("product/brand")
public class BrandController {
    @Autowired
    private BrandService brandService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{brandId}")
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody BrandEntity brand){
		brandService.save(brand);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value="/update",method= RequestMethod.POST)
    public R update(@RequestBody BrandEntity brand){
		brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] brandIds){
		brandService.removeByIds(Arrays.asList(brandIds));

        return R.ok();
    }

    /**
     * 更新brandEntity 的showStatus 字段
     * 1：表示显示
     * 0：表示不显示
     * @param brandId
     * @param showStatus
     * @return
     */
    @RequestMapping(value="/update/{brandId}/{showStatus}",method = RequestMethod.POST)
    public R updateShowStatus(@PathVariable Long brandId,@PathVariable int showStatus)
    {
        brandService.updateShoeStatus(brandId,showStatus);
        return R.ok();
    }

}
