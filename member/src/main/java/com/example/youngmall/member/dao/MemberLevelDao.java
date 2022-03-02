package com.example.youngmall.member.dao;

import com.example.youngmall.member.entity.MemberLevelEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员等级
 * 
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-02 19:43:04
 */
@Mapper
public interface MemberLevelDao extends BaseMapper<MemberLevelEntity> {
	
}
