package edu.neu.csye7125.notifier.service;

import edu.neu.csye7125.notifier.dao.ElasticsearchDao;
import edu.neu.csye7125.notifier.entity.EmailRecord;
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

    @Autowired
    private EmailRecordService emailRecordService;

    @Autowired
    private EmailService emailService;

    @Scheduled(fixedDelayString = "${notifier.scanPeriod}")
    public void scheduleAlertScanner() {

        Timestamp current = new Timestamp(System.currentTimeMillis());

        log.info("Current Timestamp - {}", System.currentTimeMillis() / 1000);

        String queryGetActiveAlerts = "SELECT category, keyword, userId FROM alert WHERE expiry >= " +
                "'" + current + "'";

        log.info(queryGetActiveAlerts);
        log.info("Category, Keyword, UserId: {}", mainJdbcTemplate.queryForList(queryGetActiveAlerts));

        List<Map<String, Object>> activeAlerts = mainJdbcTemplate.queryForList(queryGetActiveAlerts);

        String queryGetUserEmailByUserId = "SELECT emailAddress FROM user WHERE id = ";

        for (Map alert: activeAlerts) {
            String category = alert.get("category").toString().toLowerCase();
            String keyword = alert.get("keyword").toString().toLowerCase();
            String userId = alert.get("userId").toString();

            List<Map<String, String>> result = elasticsearchDao.search(category, keyword);

            String email = mainJdbcTemplate.queryForObject(
                    queryGetUserEmailByUserId + "'" + userId + "'", String.class);

            log.info(result.toString());

            for (Map<String, String> entry: result) {
                String id = entry.get("id");
                String title = entry.get("title");

                log.info("id: {}, title: {}", id, title);

                try {
                    EmailRecord existingEmailRecord = emailRecordService.getByUserIdAndStoryId(userId, id);
                    if (existingEmailRecord == null) {
                        emailService.send(email, id, title);
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
        }
    }

}
