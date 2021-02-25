package com.pegasus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import tk.mybatis.spring.annotation.MapperScan;

import javax.sql.DataSource;

/**
 * Created by enHui.Chen on 2019/8/30.
 */
@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {
    @Bean
    public PlatformTransactionManager transactionManager(DataSource myDataSource) {
        return new DataSourceTransactionManager(myDataSource);
    }
}
