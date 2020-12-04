package com.baeldung.boot.jackson.app;

import com.baeldung.boot.jackson.config.CoffeeConstants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class AbstractCoffeeIntegrationTest {

    @Autowired
    protected TestRestTemplate restTemplate;

    @Test
    public void whenGetCoffee_thenSerializedWithDateAndNonNull() {
        String formattedDate = DateTimeFormatter.ofPattern(CoffeeConstants.dateTimeFormat)
                .format(LocalDateTime.now());

        String brand = "Lavazza";

        String url = "/coffee?brand=" + brand;
        String response = restTemplate.getForObject(url, String.class);
        assertThat(response).isEqualTo(
                "{\"brand\":\"" + brand + "\",\"date\":\"" + formattedDate + "\"}");
    }
}
