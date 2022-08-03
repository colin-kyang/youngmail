package com.example.youngmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;
import com.example.youngmall.product.entity.CategoryEntity;
import com.example.youngmall.product.entity.vo.Catalog2Vo;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    public List<CategoryEntity> listWithTree();

    public void removeCategoryByIds(Long[] catIds);

    Long[] findCategoryPath(Long catelogIdPath);

    List<CategoryEntity> getLevel1Category();
    Map<String,List<Catalog2Vo>> getCatelogJson();

    Map<String,List<Catalog2Vo>> getCatelogJsonFromRedis();

    Map<String, List<Catalog2Vo>> getCatelogJsonFromRedisWithRedisLock() throws InterruptedException;

    Map<String, List<Catalog2Vo>> getCatelogJsonFromRedisWithRedissonLock() throws InterruptedException;
}

