package com.example.elasticsearch.service.impl;

import com.example.elasticsearch.mappings.SkuEsModel;
import com.example.elasticsearch.repo.SkuEsRepo;
import com.example.elasticsearch.service.skuService;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class skuServiceImpl implements skuService {

    @Autowired
    SkuEsRepo skuEsRepo;

    @Autowired
    RestHighLevelClient client;


    @Override
    @Transactional
    public void saveSkuEsModels(List<SkuEsModel> skuEsModelList) {
        skuEsRepo.saveAll(skuEsModelList);
    }
}
