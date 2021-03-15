package edu.neu.csye7125.notifier.configutation;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;

@Slf4j
@Configuration
public class MySQLConfig {

    /**
     * Main DB datasource properties
     * @return
     */
    @Bean
    @ConfigurationProperties("main.datasource")
    public DataSourceProperties mainDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Main DB datasource
     * @return
     */
    @Bean
    public DataSource mainDataSource() {
        DataSourceProperties dataSourceProperties = mainDataSourceProperties();
        log.info("main datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Main DB transaction manager
     * @return
     */
    @Bean
    @Resource
    public PlatformTransactionManager mainTransactionManager(DataSource mainDataSource) {
        return new DataSourceTransactionManager(mainDataSource);
    }

    /**
     * Main DB jdbc template
     * @return
     */
    @Bean(name = "mainJdbcTemplate")
    public JdbcTemplate mainJdbcTemplate(DataSource mainDataSource) {
        return new JdbcTemplate(mainDataSource);
    }

    /**
     * Notifier DB datasource properties
     * @return
     */
    @Bean
    @ConfigurationProperties("notifier.datasource")
    public DataSourceProperties notifierDataSourceProperties() {
        return new DataSourceProperties();
    }

    /**
     * Notifier DB datasource
     * @return
     */
    @Bean
    public DataSource notifierDataSource() {
        DataSourceProperties dataSourceProperties = notifierDataSourceProperties();
        log.info("notifier datasource: {}", dataSourceProperties.getUrl());
        return dataSourceProperties.initializeDataSourceBuilder().build();
    }

    /**
     * Notifier DB transaction manager
     * @return
     */
    @Bean
    @Resource
    public PlatformTransactionManager notifierTransactionManager(DataSource notifierDataSource) {
        return new DataSourceTransactionManager(notifierDataSource);
    }

    /**
     * Notifier DB jdbc template
     * @return
     */
    @Bean(name = "notifierJdbcTemplate")
    public JdbcTemplate notifierJdbcTemplate(DataSource notifierDataSource) {
        return new JdbcTemplate(notifierDataSource);
    }

}
