package com.baeldung.spring.datasources.todos;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Configuration
public class TodoDatasourceConfiguration {

    @Bean
    @ConfigurationProperties("spring.datasource.todos")
    public DataSourceProperties todosDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    public DataSource todosDataSource() {
        return todosDataSourceProperties()
          .initializeDataSourceBuilder()
          .build();
    }

}
