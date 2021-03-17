package edu.neu.csye7125.notifier.service;

public interface EmailService {

    void send(String to, String id, String title);

}
