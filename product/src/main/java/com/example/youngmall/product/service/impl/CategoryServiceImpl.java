package com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.*;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.CategoryDao;
import com.example.youngmall.product.entity.CategoryEntity;
import com.example.youngmall.product.service.CategoryService;


@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, CategoryEntity> implements CategoryService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryEntity> page = this.page(
                new Query<CategoryEntity>().getPage(params),
                new QueryWrapper<CategoryEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 商品 类别-树状结构查询
     *
     * @return
     */
    @Override
    public List<CategoryEntity> listWithTree() {
        //查询所有分类数据
        List<CategoryEntity> list = baseMapper.selectList(null);

        //1. 为所有分类建立索引
        Map<Long, CategoryEntity> mapp = new HashMap<>();
        for (CategoryEntity e : list) {
            mapp.put(e.getCatId(), e);
            e.setChildren(new ArrayList<>());
        }
        //2. 为每一个非第一层级设置其子分类
        for (CategoryEntity e : list) {
            //此项目非第一层级
            if (e.getParentCid() != 0) {
                CategoryEntity parent = mapp.get(e.getParentCid());
                if (parent != null) {
                    parent.addchildren(e);
                }
            }
        }
        //3. 找到第一层级，并设置其子类列表
        List<CategoryEntity> result = new ArrayList<>();
        for (CategoryEntity e : list) {
            if (e.getParentCid() == 0) {
                result.add(e);
            }
            if (e.getChildren().size() != 0) {
                e.getChildren().sort(new Comparator<CategoryEntity>() {
                    @Override
                    public int compare(CategoryEntity o1, CategoryEntity o2) {
                        return o1.getSort() - o2.getSort();
                    }
                });
            }

        }
        result.sort(new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity o1, CategoryEntity o2) {
                return o1.getSort() - o2.getSort();
            }
        });
        return result;
    }


    /**
     * 删除某一级菜单及其子菜单
     *
     * @param catIds
     */
    @Override
    public void removeCategoryByIds(Long[] catIds) {
        Map<Long, CategoryEntity> categoryEntityMap = new HashMap<>();
        List<Long> deleteList = new ArrayList<>();
        //最多扫两边算法
        for (Long id : catIds) {
            deleteList.add(id);
        }
        List<CategoryEntity> dict = baseMapper.selectList(null);
        //第一遍，保证将需要删除的二级菜单搜索完毕，第二遍保证将三级菜单搜索完毕
        for (int i = 0; i < 2; i++) {
            for (CategoryEntity e : dict) {
                //若当前条目尚未加入列表，且其父菜单在列表中
                if (!deleteList.contains(e.getCatId()) && deleteList.contains(e.getParentCid())) {
                    deleteList.add(e.getCatId());
                }
            }
        }
        if (deleteList.size() != 0) {
            baseMapper.deleteBatchIds(deleteList);
        }
    }


}