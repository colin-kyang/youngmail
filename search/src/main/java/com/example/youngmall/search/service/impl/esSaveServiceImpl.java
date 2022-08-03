package com.example.youngmall.search.service.impl;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.core.BulkResponse;
import co.elastic.clients.elasticsearch.core.bulk.BulkResponseItem;
import com.example.youngmall.search.constant.EsConstant;
import com.example.youngmall.search.entity.SkuEsModel;
import com.example.youngmall.search.service.esSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class esSaveServiceImpl implements esSaveService {

    @Autowired
    ElasticsearchClient client;

    @Override
    public void productStatusUp(List<SkuEsModel> skuEsModels) throws IOException {
        // 保存到es
        //1. 给es建立索引，建立好映射关系
        //2. 批量保存数据
        BulkRequest.Builder br = new BulkRequest.Builder();
        for (SkuEsModel model : skuEsModels) {
            System.out.println(model.toString());
            br.operations(op -> op
                    .index(idx -> idx
                            .index(EsConstant.PRODUCT_INDEX)
                            .document(model)));
        }
        BulkResponse result = client.bulk(br.build());
        if (result.errors()) {
            System.out.println("bulk had errors");
            for (BulkResponseItem item : result.items()) {
                if (item.error() != null) {
                    System.out.println(item.error().reason());
                }
            }
        }
    }
}
