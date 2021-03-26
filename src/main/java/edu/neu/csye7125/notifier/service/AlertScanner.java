package edu.neu.csye7125.notifier.service;

import edu.neu.csye7125.notifier.dao.ElasticsearchDao;
import edu.neu.csye7125.notifier.entity.EmailRecord;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.metrics.annotation.Timed;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@EnableScheduling
@Slf4j
public class AlertScanner {

    @Autowired
    @Qualifier("mainJdbcTemplate")
    private JdbcTemplate mainJdbcTemplate;

    @Autowired
    private ElasticsearchDao elasticsearchDao;

    @Autowired
    private EmailRecordService emailRecordService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private MeterRegistry meterRegistry;

    @Scheduled(fixedDelayString = "${notifier.scanPeriod}")
    public void scheduleAlertScanner() {
        log.info("Start Scanning Active Alerts");

        // initiate counters
        Counter totalCounter = meterRegistry.counter("alert.scanner.total");
        Counter successCounter = meterRegistry.counter("alert.scanner.success");
        // increment the total counter
        totalCounter.increment();

        Timestamp current = new Timestamp(System.currentTimeMillis());
        String queryGetActiveAlerts = "SELECT category, keyword, userId FROM alert WHERE expiry >= " +
                "'" + current + "'";

        Timer timer = meterRegistry.timer("db.getActiveAlerts");
        Long start = System.currentTimeMillis();

        // get active alerts from main database's alert table
        List<Map<String, Object>> activeAlerts = mainJdbcTemplate.queryForList(queryGetActiveAlerts);

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);

        String queryGetUserEmailByUserId = "SELECT emailAddress FROM user WHERE id = ";

        for (Map alert: activeAlerts) {
            String category = alert.get("category").toString().toLowerCase();
            String keyword = alert.get("keyword").toString().toLowerCase();
            String userId = alert.get("userId").toString();

            // get all matched hacker news by category and keyword from elasticsearch
            List<Map<String, String>> matched = elasticsearchDao.search(category, keyword);

            timer = meterRegistry.timer("db.getUserEmailByUserId");
            start = System.currentTimeMillis();

            // get user email by user id from main database's user table
            String email = mainJdbcTemplate.queryForObject(
                    queryGetUserEmailByUserId + "'" + userId + "'", String.class);

            end = System.currentTimeMillis();
            timer.record(end - start, TimeUnit.MILLISECONDS);

            // iterate through all matched entries
            for (Map<String, String> entry: matched) {
                String id = entry.get("id");
                String title = entry.get("title");

                log.info("id: " + id + ", title: " + title);

                try {
                    // check if already sent email
                    EmailRecord existingEmailRecord = emailRecordService.getByUserIdAndStoryId(userId, id);

                    if (existingEmailRecord == null) {
                        // send email
                        emailService.send(email, id, title);
                        // save a email record
                        EmailRecord emailRecord = EmailRecord.builder()
                                .emailAddress(email)
                                .userId(userId)
                                .storyId(id)
                                .storyTitle(title)
                                .sentAt(new Timestamp(System.currentTimeMillis()))
                                .build();
                        emailRecordService.save(emailRecord);
                    }
                } catch (Exception e) {
                    log.error(e.getMessage());
                }
            }

            // increment the success counter
            successCounter.increment();
            log.info("Finish Scanning Active Alerts");
        }
    }

}
