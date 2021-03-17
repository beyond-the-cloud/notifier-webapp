package edu.neu.csye7125.notifier.dao;

import edu.neu.csye7125.notifier.entity.EmailRecord;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import javax.persistence.NoResultException;
import java.util.List;

@Slf4j
@Repository
public class EmailRecordDaoImpl implements EmailRecordDao {

    @Autowired
    @Qualifier("notifierSessionFactory")
    private SessionFactory sessionFactory;

    @Override
    public void save(EmailRecord emailRecord) {
        Session currentSession = sessionFactory.getCurrentSession();
        currentSession.save(emailRecord);
    }

    @Override
    public EmailRecord getById(String id) {
        Session currentSession = sessionFactory.getCurrentSession();
        return (EmailRecord) currentSession.byId(id);
    }

    @Override
    public EmailRecord getByEmailAndStoryId(String email, String storyId) {
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
        return emailRecord;
    }

    @Override
    public EmailRecord getByUserIdAndStoryId(String userId, String storyId) {
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
        return emailRecord;
    }

    @Override
    public List<EmailRecord> getByUserId(String userId) {
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
        return emailRecords;
    }

    @Override
    public List<EmailRecord> getByEmail(String email) {
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
        return emailRecords;
    }

}
