package org.baeldung.properties;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackageClasses = {ConfigProperties.class,
        JsonProperties.class,
        CustomJsonProperties.class,
        AdditionalConfiguration.class})
public class ConfigPropertiesDemoApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(ConfigPropertiesDemoApplication.class).initializers(new JsonPropertyContextInitializer())
                .run();
    }

}
