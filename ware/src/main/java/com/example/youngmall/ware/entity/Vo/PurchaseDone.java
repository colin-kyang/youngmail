package com.example.youngmall.ware.entity.Vo;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 完成采购请求
 */
@Data
@ToString
public class PurchaseDone {
    @NotNull
    private Long id; // 采购单id
    
    private List<itemVo> items;// 采购清单状态
}
