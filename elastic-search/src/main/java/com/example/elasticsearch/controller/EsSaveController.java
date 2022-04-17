package com.example.elasticsearch.controller;

import com.example.common.utils.R;
import com.example.elasticsearch.mappings.SkuEsModel;
import com.example.elasticsearch.service.skuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/es/search")
@RestController
@Slf4j
public class EsSaveController {
    @Autowired
    com.example.elasticsearch.service.skuService skuService;

    /**
     * 上架商品
     * @return
     */
    @PostMapping("/save")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList){
        log.info("检查收到的数据");
        for(SkuEsModel e: skuEsModelList){
            log.info(e.toString());
        }
        skuService.saveSkuEsModels(skuEsModelList);
        return R.ok();
    }
}
