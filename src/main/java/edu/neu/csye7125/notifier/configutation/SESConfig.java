package edu.neu.csye7125.notifier.configutation;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@Slf4j
public class SESConfig {

    @Autowired
    private Environment env;

    @Bean
    public AmazonSimpleEmailService simpleEmailService() {
        AmazonSimpleEmailService client = null;
        try {
            client = AmazonSimpleEmailServiceClientBuilder
                    .standard()
                    .withCredentials(
                            new AWSStaticCredentialsProvider(
                                    new BasicAWSCredentials(
                                            env.getProperty("aws.accessKeyId"),
                                            env.getProperty("aws.secretKey"))))
                    .withRegion(Regions.US_EAST_1)
                    .build();
        } catch (Exception ex) {
            log.error("The email was not sent. Error message: " + ex.getMessage());
        }
        return client;
    }

}
