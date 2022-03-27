package com.example.youngmall.product.service.impl;

import com.example.youngmall.product.entity.AttrAttrgroupRelationEntity;
import com.example.youngmall.product.entity.AttrEntity;
import com.example.youngmall.product.entity.vo.AttrGroupRelationsVo;
import io.renren.common.utils.Query;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.AttrAttrgroupRelationDao;

import com.example.youngmall.product.service.AttrAttrgroupRelationService;


@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<AttrAttrgroupRelationEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据attrgroupId 与 attrId 批量删除关联条目
     *
     * @param attrGroupRelationsVos
     */
    @Override
    public void removeItems(AttrGroupRelationsVo[] attrGroupRelationsVos) {
        for (AttrGroupRelationsVo e : attrGroupRelationsVos) {
            QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
            wrapper.eq("attr_id", e.getAttrId()).eq("attr_group_id", e.getAttrGroupId());
            baseMapper.delete(wrapper);
        }
    }


    /**
     * 添加 分组_属性 关联条目
     * @param addAttrRelationVos
     */
    @Override
    public void addItems(AttrGroupRelationsVo[] addAttrRelationVos) {
        List<AttrGroupRelationsVo> listVos = Arrays.asList(addAttrRelationVos);
        //对拷数据，然后批量保存
        List<AttrAttrgroupRelationEntity> relations = listVos.stream().map(
                (item) -> {
                    AttrAttrgroupRelationEntity attrAttrgroupRelationEntity = new AttrAttrgroupRelationEntity();
                    BeanUtils.copyProperties(item, attrAttrgroupRelationEntity);
                    attrAttrgroupRelationEntity.setAttrSort(0);
                    return attrAttrgroupRelationEntity;
                }).collect(Collectors.toList());
        this.saveBatch(relations);
    }
}