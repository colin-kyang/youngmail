package java.com.example.youngmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;

import java.com.example.youngmall.product.entity.SpuInfoEntity;
import java.util.Map;

/**
 * spu信息
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 15:52:40
 */
public interface SpuInfoService extends IService<SpuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

