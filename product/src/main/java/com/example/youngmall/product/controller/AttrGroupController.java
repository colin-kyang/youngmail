package com.example.youngmall.product.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.youngmall.product.dao.AttrAttrgroupRelationDao;
import com.example.youngmall.product.entity.AttrAttrgroupRelationEntity;
import com.example.youngmall.product.entity.AttrEntity;
import com.example.youngmall.product.entity.vo.AttrGroupRelationsVo;
import com.example.youngmall.product.entity.vo.AttrGroupWithAttrsVo;
import com.example.youngmall.product.entity.vo.AttrVo;
import com.example.youngmall.product.service.AttrAttrgroupRelationService;
import com.example.youngmall.product.service.CategoryService;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.AttrGroupEntity;
import com.example.youngmall.product.service.AttrGroupService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 属性组
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

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping(value="/list/{catId}",method = RequestMethod.GET)
    public R list(@PathVariable Long catId,@RequestParam Map<String, Object> params){
        //catId 表示检查某一种类下的属性分组
        PageUtils page;
        // 查询所有数据，均需要考虑条件查询
        page = attrGroupService.queryPage(params,catId);
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
     * 保存规格参数 属性
     */
    @RequestMapping(value="/save",method= RequestMethod.POST)
    public R save(@RequestBody AttrGroupEntity attrGroupEntity) {
        attrGroupService.save(attrGroupEntity);
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

    /**
     * 根据分组id 查找需要关联的所有属性
     * @param attrgroupId
     * @return
     */
    @GetMapping("/{attrgroupId}/attr/relation")
    public R getRelation(@PathVariable Long attrgroupId)
    {
        List<AttrEntity> relationList=attrGroupService.getRelationByAttrGroupId(attrgroupId);
        return R.ok().put("data",relationList);
    }

    /**
     * 删除分组_属性关联
     * @param attrGroupRelationsVos
     * @return
     */
    @PostMapping("/attr/relation/delete")
    public R deleteGroupRelation(@RequestBody AttrGroupRelationsVo[] attrGroupRelationsVos)
    {
        //只发一次删除请求，完成批量删除
        attrAttrgroupRelationService.removeItems(attrGroupRelationsVos);
        return R.ok();
    }

    /**
     * 获取当前分组没有关联到到所有属性
     * @param attrgroupId
     * @param params
     * @return
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R findNoAttrRelation(@PathVariable Long attrgroupId,
                                @RequestParam Map<String,Object> params)
    {
        PageUtils page = attrGroupService.findNoAttrRelation(attrgroupId,params);
        return R.ok().put("page",page);
    }


    /**
     * 添加分组-属性
     * @param addAttrRelationVos
     * @return
     */
    @PostMapping("/attr/relation")
    public R addAttrRelation(@RequestBody AttrGroupRelationsVo [] addAttrRelationVos)
    {
        attrAttrgroupRelationService.addItems(addAttrRelationVos);
        return R.ok();
    }

    /**
     * 获取分类下所有分组&关联属性
     * @return
     */
    @GetMapping("/{catelogId}/withattr")
    public R getAttrGroupWithAttrs(@PathVariable Long catelogId){
        List<AttrGroupWithAttrsVo> attrGroupWithAttrsVos = attrGroupService.getAttrGroupWithAttrsByCatelogId(catelogId);
        return R.ok().put("data",attrGroupWithAttrsVos);
    }

}
