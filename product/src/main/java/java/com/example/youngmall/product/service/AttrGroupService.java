package java.com.example.youngmall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.common.utils.PageUtils;


import java.com.example.youngmall.product.entity.AttrGroupEntity;
import java.util.Map;

/**
 * 属性分组
 *
 * @author colinyang
 * @email colin.kyang@outlook.com
 * @date 2022-03-01 15:52:40
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

