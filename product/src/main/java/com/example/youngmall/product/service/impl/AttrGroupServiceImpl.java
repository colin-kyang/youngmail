package com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Map;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.AttrGroupDao;
import com.example.youngmall.product.entity.AttrGroupEntity;
import com.example.youngmall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        if (catId == 0) {
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), new QueryWrapper<AttrGroupEntity>());
            return new PageUtils(page);
        } else {
            //select * from pms_attr_group where catId = ? and （attr_group_id=key or attr_grou_name like "%key%"）
            //如果存在key，则需要多字段模糊查询
            String key = (String) params.get("key");
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(key)) {
                wrapper.and((obj) -> {
                    obj.eq("catelog_id", catId).or().like("attr_group_name", key);
                });
            } else {
                    wrapper.and((obj) -> {obj.eq("catelog_id", catId);});
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }
}