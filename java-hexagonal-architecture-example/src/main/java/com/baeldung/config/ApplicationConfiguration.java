package com.baeldung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baeldung.domain.EmployeeRepositoryPort;
import com.baeldung.domain.EmployeeService;
import com.baeldung.domain.EmployeeServicePort;
import com.baeldung.infrastructure.InMemoryEmployeeRepositoryAdapter;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public EmployeeServicePort employeeServicePort(EmployeeRepositoryPort employeeRepositoryPort) {
        return new EmployeeService(employeeRepositoryPort);
    }

    @Bean
    public EmployeeRepositoryPort employeeRepositoryPort() {
        return new InMemoryEmployeeRepositoryAdapter();
    }
}
