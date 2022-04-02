package com.example.youngmall.product.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.youngmall.product.dao.AttrAttrgroupRelationDao;
import com.example.youngmall.product.entity.AttrAttrgroupRelationEntity;
import com.example.youngmall.product.entity.ProductAttrValueEntity;
import com.example.youngmall.product.entity.vo.AttrVo;
import com.example.youngmall.product.entity.vo.spuAttrVo;
import com.example.youngmall.product.service.ProductAttrValueService;
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

    @Autowired
    private ProductAttrValueService productAttrValueService;

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
     * 保存商品属性，以及其与商品分组的关联关系
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attrVo) throws InvocationTargetException, IllegalAccessException {
        AttrEntity attr=new AttrEntity();
        //保存基本数据
        BeanUtils.copyProperties(attrVo,attr);
        attrService.save(attr);//注意，这里保存你无法得知它的编号
        //添加属性-属性分组 关联列表
        AttrAttrgroupRelationEntity relationEntity=new AttrAttrgroupRelationEntity();
        relationEntity.setAttrSort(0);
        //获取当前分组的GroupId
        relationEntity.setAttrGroupId(attrVo.getAttrGroupId());
        //insert 后会自动回填 TableId
        relationEntity.setAttrId(attr.getAttrId());
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

    /**
     * 获取spu 规格
     * @return
     */
    @GetMapping("/base/listforspu/{spuId}")
    public R getSpu(@PathVariable Long spuId){
         List<ProductAttrValueEntity> list = productAttrValueService.list(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
         return R.ok().put("data",list);
    }

    /**
     * 更新商品spu 规格
     * @param spuId
     * @return
     */
    @PostMapping("/update/{spuId}")
    public R updateSpuById(@RequestBody List<ProductAttrValueEntity> spuAttrs,@PathVariable Long spuId) {
        productAttrValueService.updateBySpuId(spuAttrs,spuId);
        return R.ok();
    }

}
