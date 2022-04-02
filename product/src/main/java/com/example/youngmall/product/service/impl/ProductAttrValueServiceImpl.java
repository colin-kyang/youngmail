package com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.ProductAttrValueDao;
import com.example.youngmall.product.entity.ProductAttrValueEntity;
import com.example.youngmall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
@Slf4j
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存商品spu 的规格参数
     * @param spuAttrs
     * @param spuId
     */
    @Override
    @Transactional
    public void updateBySpuId(List<ProductAttrValueEntity> spuAttrs, Long spuId) {
        spuAttrs = spuAttrs.stream().map(
                item ->{
                    item.setSpuId(spuId);
                    return item;
                }).collect(Collectors.toList());
        //首先将原有的 spu_id 为spuId 的条目全部删除
        baseMapper.delete(new QueryWrapper<ProductAttrValueEntity>().eq("spu_id",spuId));
        //然后将新的参数插入
        this.saveBatch(spuAttrs);
    }
}