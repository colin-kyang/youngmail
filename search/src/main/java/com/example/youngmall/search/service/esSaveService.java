package com.example.youngmall.search.service;

import com.example.youngmall.search.entity.SkuEsModel;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

public interface esSaveService {

    void productStatusUp(List<SkuEsModel> skuEsModels) throws IOException;

}
