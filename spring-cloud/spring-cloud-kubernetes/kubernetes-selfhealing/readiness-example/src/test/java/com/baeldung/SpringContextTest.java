package com.baeldung;

import com.baeldung.readiness.Application;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = Application.class)
public class SpringContextTest {

	@Test
	public void contextLoads() {
	}

}
