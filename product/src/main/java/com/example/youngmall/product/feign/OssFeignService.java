package com.example.youngmall.product.feign;

import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
//注意 feign 不支持下划线_
@FeignClient("third-party")
public interface OssFeignService {
    @RequestMapping(value="thirdParty/service/FilePath",method = RequestMethod.POST )
    public R upload(@RequestParam("file") String filePath);
}
