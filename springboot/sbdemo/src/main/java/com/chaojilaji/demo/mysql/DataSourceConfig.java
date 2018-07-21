package com.chaojilaji.demo.mysql;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import java.sql.SQLException;

@Configuration
public class DataSourceConfig {
    @Value("${spring.jdbc.driverClassName}")
    String driverClassName;
    @Value("${spring.url}")
    String url;
    @Value("${spring.username}")
    String username;
    @Value("${spring.password}")
    String password;
    @Value("${spring.connectionTestQuery}")
    String connectionTestQuery;
    @Value("${spring.connectionTimeout}")
    String connectionTimeout;
    @Value("${spring.idleTimeout}")
    String idleTimeout;
    @Value("${spring.maxLifetime}")
    String maxLifetime;
    @Value("${spring.maximumPoolSize}")
    String maximumPoolSize;
    @Value("${spring.minimumIdle}")
    String minimumIdle;
    @Value("${spring.poolName}")
    String poolName;


    /**
     * 创建数据源
     *
     * @return
     * @throws SQLException
     */
    @Bean
    public HikariDataSource dataSource() throws SQLException {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(driverClassName);
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(username);
        hikariConfig.setPassword(password);
        hikariConfig.setConnectionTestQuery(connectionTestQuery);
        hikariConfig.setConnectionTimeout(Long.parseLong(connectionTimeout));
        hikariConfig.setIdleTimeout(Long.parseLong(idleTimeout));
        hikariConfig.setMaxLifetime(Long.parseLong(maxLifetime));
        hikariConfig.setMaximumPoolSize(Integer.parseInt(maximumPoolSize));
        hikariConfig.setMinimumIdle(Integer.parseInt(minimumIdle));
        hikariConfig.setPoolName(poolName);
        return new HikariDataSource(hikariConfig);
    }
    /**
     *
     * @return
     * @throws SQLException
     */
    @Bean(name = "transactionManager")
    public DataSourceTransactionManager dataSourceTransactionManager() throws SQLException {
        DataSourceTransactionManager dataSourceTransactionManager = new DataSourceTransactionManager();
        dataSourceTransactionManager.setDataSource(dataSource());
        dataSourceTransactionManager.setRollbackOnCommitFailure(true);
        return dataSourceTransactionManager;
    }


}