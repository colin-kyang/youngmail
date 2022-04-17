package com.example.common.to;

import lombok.Data;
import lombok.ToString;

/**
 * sku商品销售属性 - 库存表
 */
@Data
@ToString
public class SkuHasStockTo {
    private Long skuId;
    private Boolean hasStock;
}
