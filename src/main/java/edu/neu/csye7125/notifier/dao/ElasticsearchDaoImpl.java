package edu.neu.csye7125.notifier.dao;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

@Repository
@Slf4j
public class ElasticsearchDaoImpl implements ElasticsearchDao {

    @Autowired
    @Qualifier("elasticsearchTemplate")
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public List<Map<String, String>> search(String category, String keyword) {
        Timer timer = meterRegistry.timer("elasticsearch.search");
        Long start = System.currentTimeMillis();

        List<Map<String, String>> result = new ArrayList<>();

        NativeSearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(matchQuery("title", keyword).operator(Operator.AND))
                .build();

        SearchHits<Hit> hits = elasticsearchRestTemplate.search(
                searchQuery, Hit.class, IndexCoordinates.of(category + "stories"));

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);

        for (SearchHit searchHit: hits) {
            Hit hit = (Hit) searchHit.getContent();
            log.info(hit.toString());
            Map<String, String> entry = new HashMap<>();
            entry.put("id", hit.getId());
            entry.put("title", hit.getTitle());
            result.add(entry);
        }
        return result;
    }

    @Data
    @AllArgsConstructor
    private class Hit {
        private String id;
        private String title;
    }

}
