package com.example.youngmall.member.dao;

import com.example.youngmall.member.entity.MemberEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 会员
 * 
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-02 19:43:04
 */
@Mapper
public interface MemberDao extends BaseMapper<MemberEntity> {
	
}
