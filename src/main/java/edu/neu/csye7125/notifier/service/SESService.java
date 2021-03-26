package edu.neu.csye7125.notifier.service;

import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.model.*;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SESService implements EmailService {

    private String FROM = "ibh7cw@gmail.com";

    private String SUBJECT = "A NEW ARTICLE HAS JUST BEEN PUBLISHED!";

    private String HTMLBODY = "<h1>A New Article Published</h1>" + "<p> Title: %s </p>";

    private String TEXTBODY = "A New Article Published. Title: %s";

    @Autowired
    private AmazonSimpleEmailService client;

    @Autowired
    private MeterRegistry meterRegistry;

    @Override
    public void send(String to, String id, String title) {
        Timer timer = meterRegistry.timer("sesService.send");
        Long start = System.currentTimeMillis();

        SendEmailRequest request = new SendEmailRequest()
                .withDestination(
                        new Destination().withToAddresses(to))
                .withMessage(new Message()
                        .withBody(new Body()
                                .withHtml(new Content()
                                        .withCharset("UTF-8")
                                        .withData(String.format(HTMLBODY, id + " " + title)))
                                .withText(new Content()
                                        .withCharset("UTF-8")
                                        .withData(String.format(TEXTBODY, id + " " + title))))
                        .withSubject(new Content()
                                .withCharset("UTF-8").withData(SUBJECT)))
                .withSource(FROM);
        log.info("Email Sending...");
        client.sendEmail(request);
        log.info("Email Sent!");

        Long end = System.currentTimeMillis();
        timer.record(end - start, TimeUnit.MILLISECONDS);
    }

}
