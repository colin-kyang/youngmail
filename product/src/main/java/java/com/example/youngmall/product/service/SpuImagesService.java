package java.com.example.youngmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;

import java.com.example.youngmall.product.entity.SpuImagesEntity;
import java.util.Map;

/**
 * spu图片
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 15:52:40
 */
public interface SpuImagesService extends IService<SpuImagesEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

