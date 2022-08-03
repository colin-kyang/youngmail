package com.example.youngmall.search.controller;

import com.example.common.exception.BizCodeEnum;
import com.example.youngmall.search.entity.SkuEsModel;
import com.example.youngmall.search.service.esSaveService;
import io.renren.common.utils.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/search/save")
public class esSaveController {

    @Autowired
    esSaveService service;

    @PostMapping("/product")
    public R prouductStatusUp(@RequestBody List<SkuEsModel> skuEsModels) throws IOException {
        try {
            service.productStatusUp(skuEsModels);
            return R.ok();
        } catch (Exception e) {
            log.error("es端商品上架失败{}",e);
            return R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
        }
    }
}
