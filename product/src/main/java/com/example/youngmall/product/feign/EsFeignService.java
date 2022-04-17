package com.example.youngmall.product.feign;


import com.example.common.to.es.SkuEsModel;
import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient("elastic-service")
public interface EsFeignService {
    @PostMapping("/es/search/save")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModelList);
}
