package com.example.youngmall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import com.example.common.valid.AddGroup;
import com.example.common.valid.UpdateGroup;
import com.example.youngmall.product.feign.OssFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.BrandEntity;
import com.example.youngmall.product.service.BrandService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;

import javax.validation.Valid;


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

    @Autowired
    private OssFeignService ossFeignService;

    /**
     * 列表
     */
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = brandService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{brandId}",method=RequestMethod.GET)
    public R info(@PathVariable("brandId") Long brandId){
		BrandEntity brand = brandService.getById(brandId);

        return R.ok().put("brand", brand);
    }

    /**
     * 保存
     */
    @RequestMapping(value="/save",method = RequestMethod.POST)
    public R save(@Validated({AddGroup.class}) @RequestBody BrandEntity brand, BindingResult result){
		brandService.save(brand);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value="/update",method= RequestMethod.POST)
    public R update(@Validated({UpdateGroup.class}) @RequestBody BrandEntity brand){
        brandService.updateById(brand);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
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

    @RequestMapping(value="/upload/file",method =RequestMethod.POST)
    public R uploadBrandFile(@RequestParam("filePath") String filePath)
    {
        return ossFeignService.upload(filePath);
    }



}
