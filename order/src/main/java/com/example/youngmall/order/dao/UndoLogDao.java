package com.example.youngmall.order.dao;

import com.example.youngmall.order.entity.UndoLogEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 
 * 
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-03 09:30:21
 */
@Mapper
public interface UndoLogDao extends BaseMapper<UndoLogEntity> {
	
}
