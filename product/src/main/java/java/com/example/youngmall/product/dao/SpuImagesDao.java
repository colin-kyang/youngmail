package java.com.example.youngmall.product.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

import java.com.example.youngmall.product.entity.SpuImagesEntity;

/**
 * spu图片
 * 
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 15:52:40
 */
@Mapper
public interface SpuImagesDao extends BaseMapper<SpuImagesEntity> {
	
}
