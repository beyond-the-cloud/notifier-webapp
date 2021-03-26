package edu.neu.csye7125.notifier.dao;

import edu.neu.csye7125.notifier.entity.EmailRecord;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
@Repository
public class EmailRecordDaoImpl implements EmailRecordDao {

    @Autowired
    @Qualifier("notifierSessionFactory")
    private SessionFactory sessionFactory;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void save(EmailRecord emailRecord) {
        Timer timer = meterRegistry.timer("emailRecord.save");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(emailRecord);

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
    }

    @Override
    public EmailRecord getById(String id) {
        Timer timer = meterRegistry.timer("emailRecord.getById");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
        return (EmailRecord) currentSession.byId(id);
    }

    @Override
    public EmailRecord getByEmailAndStoryId(String email, String storyId) {
        Timer timer = meterRegistry.timer("emailRecord.getByEmailAndStoryId");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        Query<EmailRecord> query = currentSession.createQuery(
                "from EmailRecord where emailAddress=:uEmailAddress and storyId=:uStoryId",
                EmailRecord.class);
        query.setParameter("uEmailAddress", email);
        query.setParameter("uStoryId", storyId);

        EmailRecord emailRecord = null;
        try {
            emailRecord = query.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
        return emailRecord;
    }

    @Override
    public EmailRecord getByUserIdAndStoryId(String userId, String storyId) {
        Timer timer = meterRegistry.timer("emailRecord.getByUserIdAndStoryId");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        Query<EmailRecord> query = currentSession.createQuery(
                "from EmailRecord where userId=:uUserId and storyId=:uStoryId",
                EmailRecord.class);
        query.setParameter("uUserId", userId);
        query.setParameter("uStoryId", storyId);

        EmailRecord emailRecord = null;
        try {
            emailRecord = query.getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
        return emailRecord;
    }

    @Override
    public List<EmailRecord> getByUserId(String userId) {
        Timer timer = meterRegistry.timer("emailRecord.getByUserId");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        Query<EmailRecord> query = currentSession.createQuery(
                "from EmailRecord where userId=:uUserId", EmailRecord.class);
        query.setParameter("uUserId", userId);

        List<EmailRecord> emailRecords = null;
        try {
            emailRecords = query.getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
        return emailRecords;
    }

    @Override
    public List<EmailRecord> getByEmail(String email) {
        Timer timer = meterRegistry.timer("emailRecord.getByEmail");
        Long start = System.currentTimeMillis();

        Session currentSession = sessionFactory.getCurrentSession();
        Query<EmailRecord> query = currentSession.createQuery(
                "from EmailRecord where emailAddress=:uEmailAddress", EmailRecord.class);
        query.setParameter("uEmailAddress", email);

        List<EmailRecord> emailRecords = null;
        try {
            emailRecords = query.getResultList();
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
        return emailRecords;
    }

}
