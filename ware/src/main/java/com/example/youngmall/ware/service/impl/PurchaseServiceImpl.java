package com.example.youngmall.ware.service.impl;

import com.example.common.constant.WareConstant;
import com.example.youngmall.ware.controller.PurchaseDetailController;
import com.example.youngmall.ware.entity.MergeVo;
import com.example.youngmall.ware.entity.PurchaseDetailEntity;
import com.example.youngmall.ware.entity.Vo.PurchaseDone;
import com.example.youngmall.ware.entity.Vo.itemVo;
import com.example.youngmall.ware.service.PurchaseDetailService;
import com.example.youngmall.ware.service.WareSkuService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.common.utils.PageUtils;
import com.example.common.utils.Query;

import com.example.youngmall.ware.dao.PurchaseDao;
import com.example.youngmall.ware.entity.PurchaseEntity;
import com.example.youngmall.ware.service.PurchaseService;
import org.springframework.transaction.annotation.Transactional;


@Service("purchaseService")
@Slf4j
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    PurchaseDetailService purchaseDetailService;

    @Autowired
    WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>()
        );

        return new PageUtils(page);
    }


    /**
     * 查找未被分配的采购单
     *
     * @param params
     * @return
     */
    @Override
    public PageUtils finUnreceive(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<PurchaseEntity>().eq("status", 0).or().eq("status", 1)
        );
        return new PageUtils(page);
    }

    /**
     * 合并需求单
     *
     * @param mergeVo
     */
    @Override
    @Transactional
    public void mergePurchase(MergeVo mergeVo) {
        log.info(mergeVo.toString());
        String purchaseId = String.valueOf(mergeVo.getPurchaseId());
        if (!StringUtils.isEmpty(purchaseId)) {
            //创建一个采购单
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setCreateTime(new Date());
            purchaseEntity.setUpdateTime(new Date());
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId = new String(purchaseId);
        }
        //合并采购单时，需要判断采购单状态
        List<Long> list = mergeVo.getItems();
        //批量处理请求单
        Long Id = Long.parseLong(purchaseId);
        List<PurchaseDetailEntity> detailEntities = list.stream().map(i -> {
            PurchaseDetailEntity detailEntity = new PurchaseDetailEntity();
            detailEntity.setPurchaseId(Id);
            detailEntity.setId(i);
            detailEntity.setStatus(WareConstant.PurchaseDetailedStatusEnum.ASSIGNED.getCode());
            return detailEntity;
        }).filter(item -> {
            //只有处于 "新建" 或者 "已经分配" 才可以进行合并
            if(item.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode() || item.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()){
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(detailEntities);
        PurchaseEntity entity = new PurchaseEntity();
        entity.setUpdateTime(new Date());
        entity.setId(Id);
        this.updateById(entity);
    }

    @Override
    @Transactional
    public void received(List<Long> list) {
        if (list != null && list.size() != 0) {
            //1. 确认当前采购单是新建后者已经分配状态
            List<PurchaseEntity> entites = list.stream().map(item -> {
                PurchaseEntity purchase = baseMapper.selectById(item);
                return purchase;
            }).filter(e -> {
                if (e.getStatus() == WareConstant.PurchaseStatusEnum.ASSIGNED.getCode() || e.getStatus() == WareConstant.PurchaseStatusEnum.CREATED.getCode()) {
                    return true;
                }
                return false;
            }).map(item -> {
                item.setStatus(WareConstant.PurchaseStatusEnum.RECEIVED.getCode());
                item.setUpdateTime(new Date());
                return item;
            }).collect(Collectors.toList());
            //2. 更新采购单状态
            this.updateBatchById(entites);
            //3. 该变采购项状态
            List<PurchaseDetailEntity> purchaseDetails = entites.stream().map(PurchaseEntity::getId)
                    .flatMap(e -> {
                        return purchaseDetailService.list(new QueryWrapper<PurchaseDetailEntity>().eq("purchase_id",e)).stream();
                    })
                    .map(item ->{
                        item.setStatus(WareConstant.PurchaseDetailedStatusEnum.BUYING.getCode());
                        return item;
                    }).collect(Collectors.toList());
            // 批量更新采购单
            purchaseDetailService.updateBatchById(purchaseDetails);
        }
    }

    /**
     * 完成采购任务
     * @param purchaseDone
     */
    @Override
    @Transactional
    public void purchaseHasDone(PurchaseDone purchaseDone) {
        //1. 改变采购单状态
        Long id = purchaseDone.getId();

        //2. 改变采购项状态
        List<itemVo> items = purchaseDone.getItems();
        List<PurchaseDetailEntity> entities = new ArrayList<>();
        Boolean flag = true;
        for(itemVo item : items){
            PurchaseDetailEntity entity = new PurchaseDetailEntity();
            entity.setId(item.getItemId());
            if(item.getStatus() == WareConstant.PurchaseDetailedStatusEnum.FAIL.getCode()){
                flag = false;
                entity.setStatus(item.getStatus());
            } else {
                entity.setStatus(WareConstant.PurchaseDetailedStatusEnum.DONE.getCode());
                // 将成功采购的入库
                PurchaseDetailEntity now_item = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(now_item.getSkuId(),now_item.getWareId(),now_item.getSkuNum());
            }
            entities.add(entity);
        }
        log.info(entities.toString());
        purchaseDetailService.updateBatchById(entities);
        // 改变采购单状态
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag? WareConstant.PurchaseStatusEnum.FINISH.getCode() :
                WareConstant.PurchaseStatusEnum.HASERROR.getCode());
        baseMapper.updateById(purchaseEntity);

    }
}