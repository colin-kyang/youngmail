package com.example.youngmall.ware.dao;

import com.example.youngmall.ware.entity.PurchaseEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 采购信息
 * 
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-03 10:03:07
 */
@Mapper
public interface PurchaseDao extends BaseMapper<PurchaseEntity> {
	
}
