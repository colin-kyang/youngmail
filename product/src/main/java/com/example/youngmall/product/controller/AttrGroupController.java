package com.example.youngmall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.example.youngmall.product.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.AttrGroupEntity;
import com.example.youngmall.product.service.AttrGroupService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 属性分组
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 列表
     */
    @RequestMapping(value="/list/{catId}",method = RequestMethod.GET)
    public R list(@PathVariable Long catId,@RequestParam Map<String, Object> params){
        PageUtils page;
        // 查询所有数据，均需要考虑条件查询
        if(catId == 0) {
            page = attrGroupService.queryPage(params);
        } else {
            //查找特定 第三分类的属性分组
            page=attrGroupService.queryPage(params,catId);
        }
        return R.ok().put("page", page);
    }


    /**
     * 详细信息
     */
    @RequestMapping(value="/info/{attrGroupId}",method=RequestMethod.GET)
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getById(attrGroupId);
        //还需要拼接完整路径 [数组形式]
        //查询所属类别的三级完整属性
        Long [] catlogIdPath=categoryService.findCategoryPath(attrGroup.getCatelogId());
        attrGroup.setCatelogIdPath(catlogIdPath);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping(value="/update",method=RequestMethod.POST)
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping(value="/delete",method=RequestMethod.POST)
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));
        return R.ok();
    }

}
