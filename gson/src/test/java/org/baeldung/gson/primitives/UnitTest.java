package org.baeldung.gson.primitives;

import com.google.gson.*;
import org.baeldung.gson.primitives.models.*;
import org.junit.Test;

import java.lang.reflect.Type;

import static junit.framework.TestCase.*;

public class UnitTest {
    @Test public void toJsonAllPrimitives() {
        PrimitiveBundle primitiveBundle = new PrimitiveBundle();

        // @formatter:off
        primitiveBundle.byteValue    = (byte) 0x00001111;
        primitiveBundle.shortValue   = (short) 3;
        primitiveBundle.intValue     = 3;
        primitiveBundle.longValue    = 3;
        primitiveBundle.floatValue   = 3.5f;
        primitiveBundle.doubleValue  = 3.5;
        primitiveBundle.booleanValue = true;
        primitiveBundle.charValue    = 'a';
        // @formatter:on

        Gson gson = new Gson();

        String expected = "{\"byteValue\":17,\"shortValue\":3,\"intValue\":3," + "\"longValue\":3,\"floatValue\":3.5" + ",\"doubleValue\":3.5" + ",\"booleanValue\":true,\"charValue\":\"a\"}";

        assertEquals(expected, gson.toJson(primitiveBundle));
    }

    @Test public void fromJsonAllPrimitives() {
        String json = "{\"byteValue\": 17, \"shortValue\": 3, \"intValue\": 3, " + "\"longValue\": 3, \"floatValue\": 3.5" + ", \"doubleValue\": 3.5" + ", \"booleanValue\": true, \"charValue\": \"a\"}";

        Gson gson = new Gson();
        PrimitiveBundle model = gson.fromJson(json, PrimitiveBundle.class);

        // @formatter:off
        assertEquals(17,  model.byteValue);
        assertEquals(3,   model.shortValue);
        assertEquals(3,   model.intValue);
        assertEquals(3,   model.longValue);
        assertEquals(3.5, model.floatValue, 0.0001);
        assertEquals(3.5, model.doubleValue, 0.0001);
        assertTrue(       model.booleanValue);
        assertEquals('a', model.charValue);
        // @formatter:on
    }

    @Test public void toJsonByteToBitString() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(GsonBitString.class, new GsonBitStringSerializer());

        Gson gson = builder.create();
        GsonBitString model = new GsonBitString();
        model.value = (byte) 0b1111;

        assertEquals("{\"value\":\"1111\"}", gson.toJson(model));
    }

    @Test public void fromJsonByteFromBitString() {
        String json = "{\"value\": \"1111\"}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(GsonBitString.class, new GsonBitStringDeserializer());

        Gson gson = gsonBuilder.create();

        GsonBitString model = gson.fromJson(json, GsonBitString.class);

        assertEquals(15, model.value);
    }

    @Test public void fromJsonPrecissionMismatch() {
        String json = "{\"value\": 12.123456789123456}";
        Gson gson = new Gson();
        GsonFloat model = gson.fromJson(json, GsonFloat.class);
        assertEquals(12.123457f, model.value, 0.000001);
    }

    @Test public void fromJsonOverflow() {
        Gson gson = new Gson();
        String json = "{\"value\": \"300\"}";
        GsonBitString model = gson.fromJson(json, GsonBitString.class);

        assertEquals(44, model.value);
    }

    @Test public void fromJsonNonCompatibleNumberTypes() {
        Gson gson = new Gson();
        String json = "{\"value\": 2.3}";
        try {
            gson.fromJson(json, GsonBitString.class);
        } catch (Exception ex) {
            assertTrue(ex instanceof JsonSyntaxException);
            assertTrue(ex.getCause() instanceof NumberFormatException);
            return;
        }

        fail();
    }

    @Test public void fromJsonUnicodeChar() {
        Gson gson = new Gson();
        String json = "{\"value\": \"\\u00AE\"}";
        GsonChar model = gson.fromJson(json, GsonChar.class);

        assertEquals('\u00AE', model.value);
    }

    @Test public void fromJsonNull() {
        Gson gson = new Gson();
        String json = "{\"value\": null}";
        GsonBitString model = gson.fromJson(json, GsonBitString.class);

        assertEquals(1, model.value);
    }

    @Test(expected = JsonSyntaxException.class) public void fromJsonEmptyString() {
        Gson gson = new Gson();
        String json = "{\"value\": \"\"}";
        gson.fromJson(json, GsonBitString.class);
    }

    @Test public void fromJsonValidValueWithinString() {
        Gson gson = new Gson();
        String json = "{\"value\": \"15\"}";
        GsonBitString model = gson.fromJson(json, GsonBitString.class);

        assertEquals(15, model.value);
    }

    @Test(expected = JsonSyntaxException.class) public void fromJsonInvalidValueWithinString() {
        Gson gson = new Gson();
        String json = "{\"value\": \"15x\"}";
        gson.fromJson(json, GsonBitString.class);
    }

    @Test(expected = JsonSyntaxException.class) public void fromJsonInvalidValueNotInAString() {
        Gson gson = new Gson();
        String json = "{\"value\": s15s}";
        gson.fromJson(json, GsonBitString.class);
    }

    @Test public void fromJsonBooleanFrom2ValueInteger() {
        String json = "{\"value\": 1}";
        Gson gson = new Gson();

        try {
            gson.fromJson(json, GsonBoolean.class);
        } catch (Exception ex) {
            assertTrue(ex instanceof JsonSyntaxException);
            assertTrue(ex.getCause() instanceof IllegalStateException);
            return;
        }

        fail();
    }

    @Test public void fromJsonBooleanFrom2ValueIntegerSolution() {
        String json = "{\"value\": 1}";
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(GsonBoolean.class, new GsonBoolean2ValueIntegerDeserializer());

        Gson gson = builder.create();

        GsonBoolean model = gson.fromJson(json, GsonBoolean.class);

        assertTrue(model.value);
    }

    @Test public void fromJsonBooleanFromYes() {
        String json = "{\"value\": yes}";
        Gson gson = new Gson();

        GsonBoolean model = gson.fromJson(json, GsonBoolean.class);

        // pay attention here that we are deserializing yes.
        assertFalse(model.value);
    }

    @Test public void fromJsonBooleanFromInvalidValue() {
        String json = "{\"value\": \"15x\"}";
        Gson gson = new Gson();

        GsonBoolean model = gson.fromJson(json, GsonBoolean.class);

        assertFalse(model.value);
    }

    // @formatter:off
    static class GsonBitStringDeserializer implements JsonDeserializer<GsonBitString> {
        @Override public GsonBitString deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            GsonBitString gsonBitString = new GsonBitString();
            gsonBitString.value = (byte) Integer.parseInt(
                                                  jsonElement.getAsJsonObject()
                                                             .getAsJsonPrimitive("value")
                                                             .getAsString()
                                                  , 2);
            return gsonBitString;
        }
    }

    static class GsonBitStringSerializer implements JsonSerializer<GsonBitString> {
        @Override public JsonElement serialize(
            GsonBitString model,
            Type type,
            JsonSerializationContext jsonSerializationContext) {

            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("value", Integer.toBinaryString(model.value));
            return jsonObject;
        }
    }

    static class GsonBoolean2ValueIntegerDeserializer implements JsonDeserializer<GsonBoolean> {
        @Override public GsonBoolean deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            GsonBoolean model = new GsonBoolean();
            int value = jsonElement.getAsJsonObject().getAsJsonPrimitive("value").getAsInt();
            if (value == 0) {
                model.value = false;
            } else if (value == 1) {
                model.value = true;
            } else {
                throw new JsonParseException("Unexpected value. Trying to deserialize "
                    + "a boolean from an integer different than 0 and 1.");
            }

            return model;
        }
    }
    // @formatter:on
}
