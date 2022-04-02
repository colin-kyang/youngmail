package com.example.youngmall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.CategoryBrandRelationDao;
import com.example.youngmall.product.entity.CategoryBrandRelationEntity;
import com.example.youngmall.product.service.CategoryBrandRelationService;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 通过品牌id 获取对应种类
     * @param brandId
     * @return
     */
    @Override
    public List<CategoryBrandRelationEntity> findRelationListByBrandId(Long brandId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id",brandId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public List<CategoryBrandRelationEntity> findRelationListByCatelogId(Long catelogId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("catelog_id",catelogId);
        return baseMapper.selectList(wrapper);
    }

    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity relationEntity=new CategoryBrandRelationEntity();
        relationEntity.setCatelogId(catId);
        relationEntity.setCatelogName(name);
        //更新采用 UpdatWrapper
        baseMapper.update(relationEntity,new UpdateWrapper<CategoryBrandRelationEntity>().eq("catlogId",catId));
    }

    @Override
    public void updateBrandById(Long brandId, String brandName) {
        baseMapper.updateBrand(brandId,brandName);
    }
}