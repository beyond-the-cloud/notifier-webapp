package edu.neu.csye7125.notifier.dao;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.Operator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Repository;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Repository
@Slf4j
public class ElasticsearchDaoImpl implements ElasticsearchDao {

    @Autowired
    @Qualifier("elasticsearchClient")
    private RestHighLevelClient client;

    @Autowired
    @Qualifier("elasticsearchTemplate")
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public String search(String category, String keyword) {
        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("title", keyword).operator(Operator.AND))
                .build();
        SearchHits<Hit> hits = elasticsearchRestTemplate.search(
                searchQuery, Hit.class, IndexCoordinates.of(category + "_stories"));
        for (SearchHit hit: hits) {
            log.info(hit.getContent().toString());
            // get user email by user id
            // call email sending function
        }
        return null;
    }

    @Data
    @AllArgsConstructor
    private class Hit {
        private String id;
        private String title;
    }
}
