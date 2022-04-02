package com.example.youngmall.product.service.impl;

import com.example.common.to.SkuReductionTO;
import com.example.common.utils.R;
import com.example.youngmall.product.entity.*;
import com.example.youngmall.product.entity.vo.*;
import com.example.youngmall.product.feign.CouponFeignService;
import com.example.youngmall.product.service.*;
import io.renren.common.utils.Query;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;

import com.example.youngmall.product.dao.SpuInfoDao;
import org.springframework.transaction.annotation.Transactional;


@Service("spuInfoService")
@Slf4j
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {
    @Autowired
    SpuInfoDescService spuInfoDescService;

    @Autowired
    SpuImagesService spuImagesService;

    @Autowired
    AttrService attrService;

    @Autowired
    ProductAttrValueService productAttrValueService;

    @Autowired
    SkuInfoService skuInfoService;

    @Autowired
    SkuImagesService skuImagesService;

    @Autowired
    SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    CouponFeignService couponFeignService;


    /**
     * spu 管理：条件查询
     * @param params
     * @return
     */
    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        QueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<>();
        //1) 关键词
        String key =(String) params.get("key");
        if(!StringUtils.isEmpty(key)){
            wrapper.and((e)->{e.eq("id",key).or().like("spu_name",key);});
        }
        //2) 品牌
        String brandId = (String) params.get("brandId");
        if(!StringUtils.isEmpty(brandId) && !"0".equals(brandId)){
            wrapper.eq("brand_id",Long.parseLong(brandId));
        }
        //3）分类
        String catlogId = (String) params.get("catelogId");
        if(!StringUtils.isEmpty(catlogId) && !"0".equals(catlogId)){
            wrapper.eq("catalog_id",Long.parseLong(catlogId));
        }
        //4）状态
        String status = (String) params.get("status");
        if(!StringUtils.isEmpty(status)){
            wrapper.eq("publish_status",Integer.parseInt(status));
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                wrapper
        );
        return new PageUtils(page);
    }


    /**
     * 保存发布商品信息
     * @param spuInfo
     */
    @Override
    @Transactional
    public void saveSpuInfo(SpuSaveVo spuInfo) {
        //保存基本信息 pms_product_spu_info
        SpuInfoEntity infoEntity = new SpuInfoEntity();
        BeanUtils.copyProperties(spuInfo, infoEntity);
        infoEntity.setCreateTime(new Date());
        infoEntity.setUpdateTime(new Date());
        this.saveBaseSpuInfo(infoEntity);
        //保存 Spu 的描述图片 pms_spu_info_desc
        List<String> descript = spuInfo.getDecript();
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        log.info(infoEntity.toString());
        spuInfoDescEntity.setSpuId(infoEntity.getId());
        //String join 拼接原图片地址
        //- to do - :若该iamgeurl 字符串为空，则不保存
        spuInfoDescEntity.setDecript(String.join(",", descript));
        log.info(spuInfoDescEntity.toString());
        this.saveSpuInfoDesc(spuInfoDescEntity);
        //保存 Spu 图片集 pms_spu_images
        List<String> images = spuInfo.getImages();
        spuImagesService.saveImages(images, infoEntity.getId());
        //保存 Spu 的规格参数 pms_product_attr_name
        List<BaseAttrs> baseAttrsList = spuInfo.getBaseAttrs();
        List<ProductAttrValueEntity> AttrValueList = baseAttrsList.stream().map(
                attr -> {
                    ProductAttrValueEntity valueEntity = new ProductAttrValueEntity();
                    valueEntity.setAttrId(attr.getAttrId());
                    //查询规格参数名
                    valueEntity.setAttrName(attrService.getById(attr.getAttrId()).getAttrName());
                    valueEntity.setAttrValue(attr.getAttrValues());
                    valueEntity.setQuickShow(attr.getShowDesc());
                    valueEntity.setSpuId(infoEntity.getId());
                    return valueEntity;
                }).collect(Collectors.toList());
        //批量保存
        productAttrValueService.saveBatch(AttrValueList);

        // 保存spu的 积分信息 sms_spu_bounds
        Bounds bounds = spuInfo.getBounds();
        SpuBoundsTo spuBoundsTo = new SpuBoundsTo();
        BeanUtils.copyProperties(bounds,spuBoundsTo);
        spuBoundsTo.setSpuId(infoEntity.getId());
        R r = couponFeignService.save(spuBoundsTo);
        if((Integer) r.get("code") != 0){
            log.info("远程保存 sku 积分设置信息失效");
        }

        // sku 的基本信息 pms_sku_info
        List<Skus> SkuList = spuInfo.getSkus();
        //sku 插入后的主键有用
        if (SkuList != null && SkuList.size() > 0) {
            //先总结默认图片
            SkuList.forEach(item -> {
                String defaultImg = "";
                for(Images image : item.getImages()){
                    if(image.getDefaultImg() == 1){
                        defaultImg = image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setBrandId(infoEntity.getBrandId());
                skuInfoEntity.setCatalogId(infoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(infoEntity.getId());
                // 设置默认图片
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                BeanUtils.copyProperties(item, skuInfoEntity);
                skuInfoService.save(skuInfoEntity);
                //回调的skuId
                Long skuId = skuInfoEntity.getSkuId();
                //保存的sku imagesEntity
                List<SkuImagesEntity> imagesEntities = item.getImages().stream().map(img -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(img.getImgUrl());
                    skuImagesEntity.setDefaultImg(img.getDefaultImg());
                    return skuImagesEntity;

                }).collect(Collectors.toList());

                //sku 的图片信息 pms_sku_images
                skuImagesService.saveBatch(imagesEntities);

                // sku 的销售属性信息 pms_sku_sale_attr_value
                List<Attr> saleAttrList = item.getAttr();
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntites = saleAttrList.stream()
                        .map(a -> {
                            SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                            BeanUtils.copyProperties(a,skuSaleAttrValueEntity);
                            skuSaleAttrValueEntity.setSkuId(skuId);
                            return skuSaleAttrValueEntity;
                        }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntites);

                //sku 的优惠、满减等信息 : gulimall_sms
                //sms_sku_full_reduction
                SkuReductionTO skuReductionTo = new SkuReductionTO();
                BeanUtils.copyProperties(item,skuReductionTo);
                skuReductionTo.setSkuId(skuId);
                skuReductionTo.setMemberPrice(item.getMemberPrice());
                if(skuReductionTo.getFullCount() > 0 || skuReductionTo.getFullPrice().compareTo(new BigDecimal(0)) == 1){
                    R r1 = couponFeignService.saveFullReduction(skuReductionTo);
                    if((Integer) r1.get("code") != 0){
                        log.info("sku 优惠、满减、会员价格 信息保存失败");
                    }
                }
            });
            log.info("商品信息保存成功！");
        }
    }

    /**
     * 保存 spuInfoDesc 图片描述信息
     *
     * @param spuInfoDescEntity
     */
    private void saveSpuInfoDesc(SpuInfoDescEntity spuInfoDescEntity) {
        spuInfoDescService.save(spuInfoDescEntity);
    }

    /**
     * 保存spu_info 基本信息
     *
     * @param infoEntity
     */
    private void saveBaseSpuInfo(SpuInfoEntity infoEntity) {
        baseMapper.insert(infoEntity);
        return;
    }
}