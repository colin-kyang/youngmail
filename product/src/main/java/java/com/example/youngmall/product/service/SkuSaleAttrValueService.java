package java.com.example.youngmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;


import java.com.example.youngmall.product.entity.SkuSaleAttrValueEntity;
import java.util.Map;

/**
 * sku销售属性&值
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 15:52:40
 */
public interface SkuSaleAttrValueService extends IService<SkuSaleAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

