package com.example.youngmall.member.feign;

import com.example.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

//这里应该填写调用微服务的注册名（签名）
@FeignClient("yongmall-coupon")
public interface CouponFeignService {
    //这里应该填写 目的微服务需要调用方法的签名，注意url 要写全
    @RequestMapping("/product/coupon/member/list")
    public R membercoupons();

}
