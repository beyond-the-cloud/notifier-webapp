package edu.neu.csye7125.notifier.service;

import edu.neu.csye7125.notifier.dao.EmailRecordDao;
import edu.neu.csye7125.notifier.entity.EmailRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(transactionManager = "notifierHibernateTransactionManager")
public class EmailRecordServiceImpl implements EmailRecordService {

    @Autowired
    private EmailRecordDao emailRecordDao;

    @Override
    public void save(EmailRecord emailRecord) {
        emailRecordDao.save(emailRecord);
    }

    @Override
    public EmailRecord getById(String id) {
        return emailRecordDao.getById(id);
    }

    @Override
    public EmailRecord getByEmailAndStoryId(String email, String storyId) {
        return emailRecordDao.getByEmailAndStoryId(email, storyId);
    }

    @Override
    public EmailRecord getByUserIdAndStoryId(String userId, String storyId) {
        return emailRecordDao.getByUserIdAndStoryId(userId, storyId);
    }

    @Override
    public List<EmailRecord> getByUserId(String userId) {
        return emailRecordDao.getByUserId(userId);
    }

    @Override
    public List<EmailRecord> getByEmail(String email) {
        return emailRecordDao.getByEmail(email);
    }

}
