package com.SadnaORM;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;

public class DBConfig {
    @Configuration
    @Component
    @ComponentScan("com.SadnaORM")
    class MySQLConfig{
        @Bean("mysqldb")
        @ConfigurationProperties(prefix="spring.datasource")
        public DataSource primaryDataSource() {
            return DataSourceBuilder.create().build();
        }

    }

    @Configuration
    @Primary
    @Component
    @ComponentScans({
            @ComponentScan(basePackages = "com.SadnaORM.UserImpl.UserControllers"),
            @ComponentScan(basePackages = "com.SadnaORM.ShopImpl.ShopControllers")
    })
    class HibernateConfig{
        @Bean("hibernatedb")
        @ConfigurationProperties(prefix="spring.second-datasource")
        public DataSource primaryDataSource() {
            return DataSourceBuilder.create().build();
        }

    }
}
