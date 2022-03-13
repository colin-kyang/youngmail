package com.example.youngmall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.CategoryEntity;
import com.example.youngmall.product.service.CategoryService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 商品三级分类
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
@RestController
@RequestMapping("product/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;


    /**
     * 查出所有分类及其子类，并按照树形结构进行返回
     */
    @RequestMapping(value="/list/tree",method= RequestMethod.GET)
    public R list(){
        List<CategoryEntity> categoryEntityList=categoryService.listWithTree();
        return R.ok().put("categoryEntityList", categoryEntityList);
    }


    /**
     * 根据商品类别id ，获取其具体向下
     */
    @RequestMapping(value="/info/{catId}",method=RequestMethod.GET)
    public R info(@PathVariable("catId") Long catId){
		CategoryEntity category = categoryService.getById(catId);

        return R.ok().put("category", category);
    }

    /**
     * 新增商品类别
     */
    @RequestMapping(value="/save",method=RequestMethod.POST)
    public R save(@RequestBody CategoryEntity category){
		categoryService.save(category);
        return R.ok();
    }

    /**
     * 修改商品类别
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public R update(@RequestBody CategoryEntity category){
		categoryService.updateById(category);
        return R.ok();
    }


    /**
     * 删除
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public R delete(@RequestBody Long[] catIds){
		categoryService.removeCategoryByIds(catIds);
        return R.ok();
    }

}
