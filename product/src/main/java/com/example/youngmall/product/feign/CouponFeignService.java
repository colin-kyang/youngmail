package com.example.youngmall.product.feign;

import com.example.common.to.SkuReductionTO;
import com.example.common.utils.R;
import com.example.youngmall.product.entity.vo.SpuBoundsTo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient("yongmall-coupon")
public interface CouponFeignService {
    /**
     * 保存积分信息
     * @param vo
     * @return
     */
    @PostMapping(value="coupon/spubounds/save")
    public R save(@RequestBody SpuBoundsTo vo);

    @PostMapping(value="coupon/skufullreduction/savereduction")
    public R saveFullReduction(@RequestBody SkuReductionTO skuReductionTO);
}
