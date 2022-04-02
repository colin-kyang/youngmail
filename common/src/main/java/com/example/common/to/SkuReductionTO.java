package com.example.common.to;

import lombok.Data;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.List;

@Data
@ToString
public class SkuReductionTO {

    private Long skuId;

    /***
     * fullCount、discount、countStatus  打折信息
     * 买几件、打几折、是否参数其他优惠
     */

    //1) 计件优惠信息
    private int fullCount;

    private BigDecimal discount;

    private int countStatus;

    //2) 满减优惠信息
    private BigDecimal fullPrice;

    private BigDecimal reducePrice;

    private int priceStatus;

    //3) 会员价格信息
    private List<MemberPrice> memberPrice;
}
