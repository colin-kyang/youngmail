package com.example.youngmall.search;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TotalHits;
import co.elastic.clients.elasticsearch.core.search.TotalHitsRelation;
import com.example.youngmall.search.entity.product;
import org.elasticsearch.client.RestClient;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
class SearchApplicationTests {

    @Autowired
    private ElasticsearchClient client;

    // 保存成功
    @Test
    public void indexData() throws IOException {
        // -1 表示非卖品
        product duc = new product("小样",-1,"10001");
        IndexResponse response = client.index(i -> i
                .index("new_product")
                .id(duc.getpId())
                .document(duc)
        );
        System.out.println(response);
    }

    // 检索数据
    @Test
    public void searchDocById() throws IOException {
        GetResponse<product> response = client.get(g -> g
                        .index("new_product")
                        .id("10001"),
                product.class
        );
        if (response.found()) {
            product product = response.source();
            System.out.println(product.toString());
        } else {
            System.out.println("未检索到目标数据");
        }
    }

    // 聚合检索：构造聚合检索条件检索数据
    @Test
    public void aggsSearchCondition(){
    }

    // 复杂检索：构造检索条件
    @Test
    public void getDocByCondition() throws IOException {
        String keyWord = "小样";
        SearchResponse<product> response = client.search(s -> s
                .index("new_product")
                .query(q -> q
                        .match(t -> t
                                .field("pName")
                                .query(keyWord))),product.class);
        TotalHits totalHits = response.hits().total();
        boolean isExactResult = totalHits.relation() == TotalHitsRelation.Eq;

        if(isExactResult) {
            System.out.println("There are " + totalHits.value() + " results.");
        } else {
            System.out.println("There are more than " + totalHits.value() + " results.");
        }

        List<Hit<product>> hits = response.hits().hits();
        for (Hit<product> hit: hits) {
            product product = hit.source();
            System.out.println("Found product " + product.getpId() + ", score " + hit.score());
        }
    }

    @Test
    public void complexSearchTest() throws IOException {
        String keyWord = "小样";

        // search by keyWord
        Query byName = MatchQuery.of(m -> m
                .field("pName")
                .query(keyWord))._toQuery();

        // search by id
        Query byId = MatchQuery.of(m -> m
                .field("pId")
                .query("10001"))._toQuery();

        // 范围 RangeQuery.of

        SearchResponse<product> response = client.search(s -> s
                .index("new_product")
                .query(p -> p
                        .bool(b -> b
                                .must(byName)
                                .must(byId))),product.class);

        List<Hit<product>> hits = response.hits().hits();
        for(Hit<product> hit : hits) {
            product p = hit.source();
            System.out.println(p.toString());
            System.out.println("score :" + hit.score());
        }
    }

    @Test
    public void aggTest() {

    }

}
