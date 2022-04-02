package com.example.youngmall.ware.entity;

import lombok.Data;

import java.util.List;

@Data
public class MergeVo {

    //合并需求id list
    private List<Long> items;

    //整单id
    private Long purchaseId;
}
