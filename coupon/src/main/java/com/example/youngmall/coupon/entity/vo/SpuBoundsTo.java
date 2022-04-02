package com.example.youngmall.coupon.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 接受来自 product 服务的spuBounds 记录请求
 */
@Data
@ToString
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
}
