package edu.neu.csye7125.notifier.configutation;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.RestClients;
import org.springframework.data.elasticsearch.config.AbstractElasticsearchConfiguration;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;


@Configuration
public class ElasticsearchConfig extends AbstractElasticsearchConfiguration {

    @Autowired
    private Environment env;

    @Override
    @Bean
    public RestHighLevelClient elasticsearchClient() {
        final ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(env.getProperty("elasticsearch.host"))
                .build();
        return RestClients.create(clientConfiguration).rest();
    }

    @Bean(name = {"elasticsearchOperations", "elasticsearchTemplate"})
    public ElasticsearchRestTemplate elasticsearchTemplate() {
        return new ElasticsearchRestTemplate(elasticsearchClient());
    }

}
