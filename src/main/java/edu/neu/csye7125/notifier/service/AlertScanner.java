package edu.neu.csye7125.notifier.service;

import edu.neu.csye7125.notifier.dao.ElasticsearchDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

@Service
@EnableScheduling
@Slf4j
public class AlertScanner {

    @Autowired
    @Qualifier("mainJdbcTemplate")
    private JdbcTemplate mainJdbcTemplate;

    @Autowired
    private ElasticsearchDao elasticsearchDao;

    @Scheduled(fixedDelay = 1000 * 10)  // delay in millisecond, total is 5 (1000 * 60 * 5) minutes
    public void scheduleFixedDelayTask() {
        log.info("Fixed delay task - " + System.currentTimeMillis() / 1000);
        Timestamp current = new Timestamp(System.currentTimeMillis());
        String query = "SELECT category, keyword, userId FROM alert WHERE expiry >= '" + current + "'";
        log.info(query);
        log.info("Category, Keyword, UserId: {}", mainJdbcTemplate.queryForList(query));
        List<Map<String, Object>> pairs = mainJdbcTemplate.queryForList(query);
        for (Map pair: pairs) {
            elasticsearchDao.search(pair.get("category").toString().toLowerCase(),
                    pair.get("keyword").toString().toLowerCase());
        }
        log.info("Count: {}", mainJdbcTemplate.queryForObject("SELECT COUNT(*) FROM Alert", Long.class));
    }

}
