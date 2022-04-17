package com.example.elasticsearch.service;

import com.example.elasticsearch.mappings.SkuEsModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface skuService {
    public void saveSkuEsModels(List<SkuEsModel> skuEsModelList);
}
