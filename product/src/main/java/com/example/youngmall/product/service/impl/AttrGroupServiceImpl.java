package com.example.youngmall.product.service.impl;

import cn.hutool.db.Page;
import com.example.youngmall.product.dao.AttrAttrgroupRelationDao;
import com.example.youngmall.product.entity.AttrAttrgroupRelationEntity;
import com.example.youngmall.product.entity.AttrEntity;
import com.example.youngmall.product.entity.vo.AttrGroupWithAttrsVo;
import com.example.youngmall.product.service.AttrAttrgroupRelationService;
import com.example.youngmall.product.service.AttrService;
import io.renren.common.utils.Query;
import io.swagger.models.auth.In;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.AttrGroupDao;
import com.example.youngmall.product.entity.AttrGroupEntity;
import com.example.youngmall.product.service.AttrGroupService;


@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private AttrAttrgroupRelationDao attrAttrgroupRelationDao;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;


    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrGroupEntity> page = this.page(
                new Query<AttrGroupEntity>().getPage(params),
                new QueryWrapper<AttrGroupEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 根据catID 下分组信息
     * @param params
     * @param catId
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params, Long catId) {
        String key = (String) params.getOrDefault("key", null);
        if (catId == 0) {
            //查询全部
            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(key)) {
                //存在关键字
                wrapper.and((obj) -> obj.eq("catelog_id", catId).or().like("attr_group_name", key));
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        } else {
            //select * from pms_attr_group where catId = ? and （attr_group_id=key or attr_grou_name like "%key%"）
            //如果存在key，则需要多字段模糊查询

            QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
            if (!StringUtils.isEmpty(key)) {
                wrapper.and((obj) -> {
                    obj.eq("catelog_id", catId).or().like("attr_group_name", key);
                });
            } else {
                wrapper.and((obj) -> {
                    obj.eq("catelog_id", catId);
                });
            }
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }
    }


    /**
     * 基于attrgroupId 查找与之关联的所有 attrEntity
     * @param attrgroupId
     * @return
     */
    @Override
    public List<AttrEntity> getRelationByAttrGroupId(Long attrgroupId) {
        QueryWrapper<AttrAttrgroupRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("attr_group_id", attrgroupId);
        List<AttrAttrgroupRelationEntity> entities = attrAttrgroupRelationDao.selectList(wrapper);
        List<Long> attrList = entities.stream().map((attr) -> {
            return attr.getAttrId();
        }).collect(Collectors.toList());
        List<AttrEntity> result = attrService.listByIds(attrList);
        return result;
    }


    /**
     * 查找与分组处于同一类别下，未建立关联的属性
     * @param attrgroupId
     * @param params
     * @return
     */
    @Override
    public PageUtils findNoAttrRelation(Long attrgroupId, Map<String, Object> params) {
        //1. 当前分组只能与 同属一一个catalog 下的属性建立关联
        //首先获取当前分组所属类别
        AttrGroupEntity attrGroupEntity = baseMapper.selectById(attrgroupId);
        Long catlogId = attrGroupEntity.getCatelogId();
        //2. 当前分组只能关联别的分组没有引用到属性(包括自己的已经引用到的)
        //查找当前分类下到其他分组
//        List<AttrGroupEntity> list = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id", catlogId).ne("attr_group_id", attrgroupId));
        List<AttrGroupEntity> list = baseMapper.selectList(new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catlogId));
        //获取其他分组的 AttrGroupId
        List<Long> collect = list.stream().map((item) -> {
            return item.getAttrGroupId();
        }).collect(Collectors.toList());
        //根据这些分组id 获取其关联到到属性 的AttrId
        QueryWrapper<AttrAttrgroupRelationEntity> relationWrapper = new QueryWrapper<>();
        relationWrapper.in("attr_group_id",collect);
        List<AttrAttrgroupRelationEntity> groupId = attrAttrgroupRelationDao.selectList(relationWrapper);
        //返回同属一个分组下 其他分组（包括自己） 所关联的种类id
        List<Long> attrIds = groupId.stream().map((item) -> {
            return item.getAttrId();
        }).collect(Collectors.toList());
        //从当前分类所关联中的所有属性中，去除这些属性
        QueryWrapper<AttrEntity> attrWrapper = new QueryWrapper<AttrEntity>();
        attrWrapper.eq("catelog_id", catlogId).notIn("attr_id", attrIds);
        String key = (String) params.getOrDefault("key", null);
        //若存在关键字还需要添加条件
        if (!StringUtils.isEmpty(key)) {
            attrWrapper.and((obj) -> {
                obj.eq("attr_id", key).or().like("attr_name", key);
            });
        }
        IPage<AttrEntity> page = attrService.page(
                new Query<AttrEntity>().getPage(params),
                attrWrapper
        );
        return new PageUtils(page);
    }

    /**
     * 查询分组 及该分组下的所有属性
     * @param catelogId
     * @return
     */
    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrsByCatelogId(Long catelogId) {
        //1. 查询分组信息
        List<AttrGroupEntity> attrGroupEntities = this.list(new QueryWrapper<AttrGroupEntity>().eq("catelog_id",catelogId));
        //2. 查询所有属性
        return attrGroupEntities.stream()
                .map(group ->{
                    AttrGroupWithAttrsVo vo = new AttrGroupWithAttrsVo();
                    BeanUtils.copyProperties(group,vo);
                    //查询该分组下的属性列表
                    List<Long> attrIds = attrAttrgroupRelationService.list(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_group_id",group.getAttrGroupId()))
                            .stream()
                            .map(attr -> {
                                return attr.getAttrId();
                            })
                            .collect(Collectors.toList());
                    List<AttrEntity> attrList =  new ArrayList<>();
                    //按照attrIds 依次查询属性条目，封装到 attrList 中
                    attrIds.stream()
                            .forEach(id -> {
                                attrList .add(attrService.getById(id));
                            });
                    vo.setAttrs(attrList);
                    return vo;
                })
                .collect(Collectors.toList());
    }
}