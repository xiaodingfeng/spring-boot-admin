package com.ciicgat.circlefk.common.config;

import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;

import javax.sql.DataSource;

@Configuration
public class DataSourceAutoConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataSourceAutoConfiguration.class);

    @Autowired
    private Environment env;

    @Primary
    @Bean
    public DataSource dataSource() {
        //可以在此处调用相关接口获取数据库的配置信息进行 DataSource 的配置
        HikariDataSource dataSource = new HikariDataSource();
        if (env.getActiveProfiles()[0].equals("dev")) {
            dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
            dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
            dataSource.setPassword(env.getProperty("spring.datasource.password"));
        } else {
            // 自己通过接口调redis ip端口
            // 或者可以存在服务器，读取文件
            dataSource.setDriverClassName(env.getProperty("spring.datasource.driver-class-name"));
            dataSource.setJdbcUrl(env.getProperty("spring.datasource.url"));
            dataSource.setUsername(env.getProperty("spring.datasource.username"));
            dataSource.setPassword(env.getProperty("spring.datasource.password"));
        }

        LOGGER.info("jdbc:  url: {} ",dataSource.getJdbcUrl());
        return dataSource;
    }
}



