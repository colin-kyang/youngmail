package com.example.youngmall.product.entity.vo;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;

/**
 * 保存商品积分信息
 */
@Data
@ToString
public class SpuBoundsTo {
    private Long spuId;
    private BigDecimal growBounds;
    private BigDecimal buyBounds;
}
