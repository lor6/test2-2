package com.baeldung.annotations.componentscanautoconfigure;

import com.baeldung.annotations.componentscanautoconfigure.teacher.Teacher;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.aop.AopAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = {"com.baeldung.annotations.componentscanautoconfigure.doctor", "com.baeldung.annotations.componentscanautoconfigure.employee"},
        basePackageClasses = Teacher.class)
@EnableAutoConfiguration(exclude={AopAutoConfiguration.class})
//@EnableAutoConfiguration(excludeName = {"org.springframework.boot.autoconfigure.aop"})
public class EmployeeApplication {

    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(EmployeeApplication.class, args);
        System.out.println("Configures Doctor: " + context.containsBeanDefinition("doctor"));
        System.out.println("Configures Employee: " + context.containsBeanDefinition("employee"));
        System.out.println("Configures Senior Employee: " + context.containsBeanDefinition("seniorEmployee"));
        System.out.println("Configures Student: " + context.containsBeanDefinition("student"));
        System.out.println("Configures Teacher: " + context.containsBeanDefinition("teacher"));
    }
}
