package com.example.youngmall.product.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.youngmall.product.service.BrandService;
import com.example.youngmall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.CategoryBrandRelationEntity;
import com.example.youngmall.product.service.CategoryBrandRelationService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 品牌分类关联
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
@RestController
@RequestMapping("product/categorybrandrelation")
public class CategoryBrandRelationController {
    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 获取品牌关联的分类
     * @param brandId
     * @return
     */
    @RequestMapping(value="catelog/list",method=RequestMethod.GET)
//    @GetMapping()
    public R categoryList(@RequestParam("brandId") Long brandId)
    {
        List<CategoryBrandRelationEntity> categoryBrandRelationList=categoryBrandRelationService.findRelationListByBrandId(brandId);
        return R.ok().put("data",categoryBrandRelationList);
    }

    /**
     * 列表
     */
    @RequestMapping(value="/list",method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = categoryBrandRelationService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping(value="/info/{id}",method = RequestMethod.GET)
    public R info(@PathVariable("id") Long id){
		CategoryBrandRelationEntity categoryBrandRelation = categoryBrandRelationService.getById(id);

        return R.ok().put("categoryBrandRelation", categoryBrandRelation);
    }

    /**
     * 新增品牌_分类 条目
     * 添加冗余字段，便于后续查询
     */
    @RequestMapping(value="/save",method=RequestMethod.POST)
    public R saveDetail(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
        Long brandId = categoryBrandRelation.getBrandId();
        Long catelogId = categoryBrandRelation.getCatelogId();
        // 查询品牌 种类名字
        String brandName=brandService.getById(brandId).getName();
        String catelogName=categoryService.getById(catelogId).getName();
        categoryBrandRelation.setBrandName(brandName);
        categoryBrandRelation.setCatelogName(catelogName);
        categoryBrandRelationService.save(categoryBrandRelation);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public R update(@RequestBody CategoryBrandRelationEntity categoryBrandRelation){
		categoryBrandRelationService.updateById(categoryBrandRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public R delete(@RequestBody Long[] ids){
		categoryBrandRelationService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

}
