package com.example.youngmall.product.service.impl;

import io.renren.common.utils.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.SpuImagesDao;
import com.example.youngmall.product.entity.SpuImagesEntity;
import com.example.youngmall.product.service.SpuImagesService;


@Service("spuImagesService")
public class SpuImagesServiceImpl extends ServiceImpl<SpuImagesDao, SpuImagesEntity> implements SpuImagesService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuImagesEntity> page = this.page(
                new Query<SpuImagesEntity>().getPage(params),
                new QueryWrapper<SpuImagesEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 保存spu_id 图片 地址
     * @param images
     * @param id
     */
    @Override
    public void saveImages(List<String> images, Long id) {
        if(images == null || images.size() == 0){
                return ;
        } else {
            List<SpuImagesEntity> spuImagesEntities = images.stream().map(img ->{
                SpuImagesEntity entity = new SpuImagesEntity();
                entity.setSpuId(id);
                entity.setImgUrl(img);
                return entity;
            }).collect(Collectors.toList());
            this.saveBatch(spuImagesEntities);
        }
    }
}