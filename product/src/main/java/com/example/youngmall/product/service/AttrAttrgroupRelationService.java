package com.example.youngmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.youngmall.product.entity.AttrAttrgroupRelationEntity;
import com.example.youngmall.product.entity.vo.AttrGroupRelationsVo;

import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void removeItems(AttrGroupRelationsVo[] attrGroupRelationsVos);

    void addItems(AttrGroupRelationsVo[] addAttrRelationVos);
}

