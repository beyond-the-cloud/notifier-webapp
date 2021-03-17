package edu.neu.csye7125.notifier.configutation;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.util.Properties;

@Slf4j
@Configuration
public class MySQLConfig {

    @Autowired
    private Environment env;

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

    /**
     * Get Hibernate properties from application.properties file
     * @return
     */
    private Properties getHibernateProperties() {
        // set hibernate properties
        Properties props = new Properties();
        props.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
        props.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
        props.setProperty("hibernate.hbm2ddl.auto", env.getProperty("hibernate.hbm2ddl.auto"));
        props.setProperty("hibernate.jdbc.time_zone", env.getProperty("hibernate.jdbc.time_zone"));
        return props;
    }

    /**
     * Set up session factory for Notifier DB
     * @return
     */
    @Bean(name = "notifierSessionFactory")
    public LocalSessionFactoryBean sessionFactory(){
        // create session factory
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        // set the properties
        sessionFactory.setDataSource(notifierDataSource());
        sessionFactory.setPackagesToScan(env.getProperty("hibernate.packagesToScan"));
        sessionFactory.setHibernateProperties(getHibernateProperties());
        return sessionFactory;
    }

    /**
     * Set up transaction manager for Notifier DB
     * @param sessionFactory
     * @return
     */
    @Bean(name = "notifierHibernateTransactionManager")
    public HibernateTransactionManager transactionManager(SessionFactory sessionFactory) {
        // setup transaction manager based on session factory
        HibernateTransactionManager txManager = new HibernateTransactionManager();
        txManager.setSessionFactory(sessionFactory);
        return txManager;
    }

}
