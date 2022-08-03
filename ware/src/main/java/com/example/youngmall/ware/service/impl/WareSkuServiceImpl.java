package com.example.youngmall.ware.service.impl;

import com.example.common.to.SkuHasStockTo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.youngmall.ware.dao.WareSkuDao;
import com.example.youngmall.ware.entity.WareSkuEntity;
import com.example.youngmall.ware.service.WareSkuService;
import org.springframework.transaction.annotation.Transactional;


@Service("wareSkuService")
@Slf4j
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    WareSkuDao wareSkuDao;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        //page=1&limit=10&skuId=2&wareId=1
        QueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<>();
        // skuId
        String skuId = (String) params.get("skuId");
        if (!StringUtils.isEmpty(skuId)) {
            wrapper.eq("sku_id", skuId);
        }
        // wareId
        String wareId = (String) params.get("wareId");
        if (!StringUtils.isEmpty(wareId)) {
            wrapper.eq("ware_id", wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),
                wrapper
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional
    public void addStock(Long skuId, Long wareId, Integer skuNum) {
        //1. 判断如果还没有库存记录，那么就是新增擦走哦
        List<WareSkuEntity> list = wareSkuDao.selectList(new QueryWrapper<WareSkuEntity>().eq("sku_id", skuId).eq("ware_id", wareId));
        if (list == null || list.size() == 0) {
            //添加库存
            WareSkuEntity wareSkuEntity = new WareSkuEntity();
            wareSkuEntity.setSkuId(skuId);
            wareSkuEntity.setStock(skuNum);
            wareSkuEntity.setWareId(wareId);
            //TODO: 远程获取sku 的名字
            wareSkuDao.insert(wareSkuEntity);
        } else {
            wareSkuDao.addStock(skuId, wareId, skuNum);
        }

    }

    /**
     * 当前spu对应商品是否拥有库存
     * @param skuIds
     * @return
     */
    @Override
    public List<SkuHasStockTo> getSkusHasStock(List<Long> skuIds) {
        List<SkuHasStockTo> skuHasStockTos = skuIds.stream()
                .map(item -> {
                    // select SUM(stock - stock_locked) from wms_ware_sku where sku_id = ?
                    SkuHasStockTo to = new SkuHasStockTo();
                    //查询当前库存
                    Long sum = baseMapper.getSkuStock(item);
                    to.setSkuId(item);
                    //是否大于 0
                    if (sum != null) {
                        to.setHasStock(sum > 0);
                    } else {
                        // 远程查库存
                        to.setHasStock(false);
                    }
                    return to;
                })
                .collect(Collectors.toList());
        return skuHasStockTos;
    }
}