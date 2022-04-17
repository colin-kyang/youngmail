package com.example.elasticsearch.repo;



import com.example.elasticsearch.mappings.SkuEsModel;
import org.springframework.data.repository.CrudRepository;

public interface SkuEsRepo extends CrudRepository<SkuEsModel,Long> {
}
