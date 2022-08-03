package com.example.youngmall.product.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Catalog2Vo {
    private String catalog1Id; // 1级父分类id
    private List<catalog3Vo> catalog3List; // 3级分类列表
    private String id;
    private String name;


    /**
     * 三级分类vo
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class catalog3Vo {
        private String catalog2Id; // 父分类，2级id
        private String id;
        private String name;
    }

}
