package com.example.youngmall.coupon.service.impl;

import com.example.common.to.MemberPrice;
import com.example.common.to.SkuReductionTO;
import com.example.youngmall.coupon.entity.MemberPriceEntity;
import com.example.youngmall.coupon.entity.SkuLadderEntity;
import com.example.youngmall.coupon.service.MemberPriceService;
import com.example.youngmall.coupon.service.SkuLadderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.youngmall.coupon.dao.SkuFullReductionDao;
import com.example.youngmall.coupon.entity.SkuFullReductionEntity;
import com.example.youngmall.coupon.service.SkuFullReductionService;


@Service("skuFullReductionService")
@Slf4j
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    SkuLadderService skuLadderService;

    @Autowired
    MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public void saveFullReduction(SkuReductionTO skuReductionTO) {
        log.info(skuReductionTO.toString());
        //1) 保存计件优惠
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTO.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTO.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTO.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTO.getCountStatus());
        skuLadderService.save(skuLadderEntity);

        //2) 保存满减优惠
        SkuFullReductionEntity skuFullReduction = new SkuFullReductionEntity();
        skuFullReduction.setSkuId(skuReductionTO.getSkuId());
        skuFullReduction.setAddOther(skuReductionTO.getPriceStatus());
        skuFullReduction.setReducePrice(skuReductionTO.getReducePrice());
        skuFullReduction.setFullPrice(skuReductionTO.getFullPrice());
        if(skuFullReduction.getFullPrice().compareTo(new BigDecimal(0)) == 1){
            baseMapper.insert(skuFullReduction);
        }

        //3) 保存会员价格
        List<MemberPrice> memberPriceList = skuReductionTO.getMemberPrice();
        List<MemberPriceEntity> memberPriceEntities = memberPriceList.stream()
                .filter(item ->{
                    //过滤掉无效会员价格
                    return item.getPrice().compareTo(new BigDecimal(0)) ==1;
                })
                .map(item ->{
                    MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                    memberPriceEntity.setSkuId(skuFullReduction.getSkuId());
                    memberPriceEntity.setMemberLevelId(item.getId());
                    memberPriceEntity.setMemberLevelName(item.getName());
                    memberPriceEntity.setMemberPrice(item.getPrice());
                    memberPriceEntity.setAddOther(1);
                    return memberPriceEntity;
                })
                .collect(Collectors.toList());
        memberPriceService.saveBatch(memberPriceEntities);
    }
}