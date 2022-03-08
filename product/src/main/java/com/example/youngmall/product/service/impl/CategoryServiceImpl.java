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

    @Override
    public List<CategoryEntity> listWithTree() {
        //查询所有分类数据
        List<CategoryEntity> list=baseMapper.selectList(null);

        //1. 为所有分类建立索引
        Map<Long,CategoryEntity> mapp=new HashMap<>();
        for(CategoryEntity e:list)
        {
            mapp.put(e.getCatId(),e);
            e.setChildren(new ArrayList<>());
        }
        //2. 为每一个非第一层级设置其子分类
        for(CategoryEntity e:list)
        {
            //此项目非第一层级
            if(e.getParentCid()!=0)
            {
               mapp.get(e.getParentCid()).addchildren(e);
            }
        }
        //3. 找到第一层级，并设置其子类列表
        List<CategoryEntity> result=new ArrayList<>();
        for(CategoryEntity e:list)
        {
            if(e.getParentCid()==0)
            {
                result.add(e);
            }
            if(e.getChildren().size()!=0)
            {
                e.getChildren().sort(new Comparator<CategoryEntity>() {
                    @Override
                    public int compare(CategoryEntity o1, CategoryEntity o2) {
                        return o1.getSort()-o2.getSort();
                    }
                });
            }

        }
        result.sort(new Comparator<CategoryEntity>() {
            @Override
            public int compare(CategoryEntity o1, CategoryEntity o2) {
                return o1.getSort()-o2.getSort();
            }
        });
        return result;
    }


}