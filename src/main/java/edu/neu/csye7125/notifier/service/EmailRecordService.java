package edu.neu.csye7125.notifier.service;

import edu.neu.csye7125.notifier.entity.EmailRecord;

import java.util.List;

public interface EmailRecordService {

    void save(EmailRecord emailRecord);

    EmailRecord getById(String id);

    EmailRecord getByEmailAndStoryId(String email, String storyId);

    EmailRecord getByUserIdAndStoryId(String userId, String storyId);

    List<EmailRecord> getByUserId(String userId);

    List<EmailRecord> getByEmail(String email);

}
