package com.example.youngmall.product.dao;

import com.example.youngmall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 23:47:49
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
