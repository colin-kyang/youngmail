package com.example.youngmall.product.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

import com.example.youngmall.product.dao.AttrAttrgroupRelationDao;
import com.example.youngmall.product.entity.AttrAttrgroupRelationEntity;
import com.example.youngmall.product.entity.vo.AttrVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.youngmall.product.entity.AttrEntity;
import com.example.youngmall.product.service.AttrService;
import com.example.common.utils.PageUtils;
import com.example.common.utils.R;



/**
 * 商品属性
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    /**
     * 列表 区分规格属性与销售属性
     */
    @RequestMapping(value="/{type}/list/{catlogId}",method = RequestMethod.GET)
    public R list(@RequestParam Map<String, Object> params,@PathVariable Long catlogId,@PathVariable String type){
        int attr_type;
        if(type.equals("base")){
            //规格参数
            attr_type=0;
        } else {
            //销售属性
            attr_type=1;
        }
        PageUtils page = attrService.queryPage(params,catlogId,attr_type);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrEntity attr = attrService.getById(attrId);

        return R.ok().put("attr", attr);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo) throws InvocationTargetException, IllegalAccessException {
        AttrEntity attr=new AttrEntity();
        //保存基本数据
        BeanUtils.copyProperties(attrVo,attr);
        attrService.save(attr);
        //添加进属性——分组关联表
        AttrAttrgroupRelationEntity relationEntity=new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        relationEntity.setAttrId(attrVo.getAttrId());
        attrAttrgroupRelationDao.insert(relationEntity);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrEntity attr){
		attrService.updateById(attr);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
