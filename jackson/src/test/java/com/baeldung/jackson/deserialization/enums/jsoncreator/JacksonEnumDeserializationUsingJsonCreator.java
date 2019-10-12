package com.baeldung.jackson.deserialization.enums.jsoncreator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import java.io.IOException;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JacksonEnumDeserializationUsingJsonCreator {

    @Test
    public final void givenEnumWithJsonCreator_whenDeserializingJson_thenCorrectRepresentation() throws JsonParseException, IOException {
        String json = "{\"distance\": {\"unit\":\"miles\",\"meters\":1609.34}}";

        City city = new ObjectMapper().readValue(json, City.class);
        assertEquals(Distance.MILE, city.getDistance());
    }
    
}
