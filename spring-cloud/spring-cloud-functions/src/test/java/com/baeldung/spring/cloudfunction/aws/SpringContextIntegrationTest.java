package com.baeldung.spring.cloudfunction.aws;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CloudFunctionAwsApplication.class)
public class SpringContextIntegrationTest {
    
    @Test
    public void whenSpringContextIsBootstrapped_thenNoExceptions() {
    }
}