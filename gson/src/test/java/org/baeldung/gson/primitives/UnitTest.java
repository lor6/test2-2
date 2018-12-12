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

        String expected = "{\"byteValue\":17,\"shortValue\":3,\"intValue\":3,"
            + "\"longValue\":3,\"floatValue\":3.5" + ",\"doubleValue\":3.5"
            + ",\"booleanValue\":true,\"charValue\":\"a\"}";

        assertEquals(expected, gson.toJson(primitiveBundle));
    }

    @Test public void fromJsonAllPrimitives() {
        String json = "{\"byteValue\": 17, \"shortValue\": 3, \"intValue\": 3, "
            + "\"longValue\": 3, \"floatValue\": 3.5" + ", \"doubleValue\": 3.5"
            + ", \"booleanValue\": true, \"charValue\": \"a\"}";

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

    @Test public void fromJsonPrecissionMismatch() {
        String json = "{\"value\": 12.123425589123456}";
        Gson gson = new Gson();
        FloatExample model = gson.fromJson(json, FloatExample.class);
        assertEquals(12.123426f, model.value, 0.000001);
    }

    @Test public void fromJsonPrecissionMismatchForDouble() {
        String json = "{\"value\": 12.123425589123556}";
        Gson gson = new Gson();
        DoubleExample model = gson.fromJson(json, DoubleExample.class);
        assertEquals(12.123425589124f, model.value, 0.000001);
    }


    @Test public void fromJsonOverflow() {
        Gson gson = new Gson();
        String json = "{\"value\": \"300\"}";
        ByteExample model = gson.fromJson(json, ByteExample.class);

        assertEquals(44, model.value);
    }

    @Test public void fromJsonNonCompatibleNumberTypes() {
        Gson gson = new Gson();
        String json = "{\"value\": 2.3}";
        try {
            gson.fromJson(json, ByteExample.class);
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
        CharExample model = gson.fromJson(json, CharExample.class);

        assertEquals('\u00AE', model.value);
    }

    @Test public void fromJsonNull() {
        Gson gson = new Gson();
        // @formatter:off
        String json = "{\"byteValue\": null, \"shortValue\": null, "
            + "\"intValue\": null, " + "\"longValue\": null, \"floatValue\": null"
            + ", \"doubleValue\": null" + ", \"booleanValue\": null, \"charValue\": null}";
        // @formatter:on
        PrimitiveBundleInitialized model = gson.fromJson(json,
            PrimitiveBundleInitialized.class);

        assertEquals(1, model.byteValue);
        assertEquals(1, model.shortValue);
        assertEquals(1, model.intValue);
        assertEquals(1, model.longValue);
        assertEquals(1, model.floatValue, 0.0001);
        assertEquals(1, model.doubleValue, 0.0001);
        assertTrue(model.booleanValue);
        assertEquals('a', model.charValue);
    }

    @Test(expected = JsonSyntaxException.class) public void fromJsonEmptyString() {
        Gson gson = new Gson();
        // @formatter:off
        String json = "{\"byteValue\": \"\", \"shortValue\": \"\", "
            + "\"intValue\": \"\", " + "\"longValue\": \"\", \"floatValue\": \"\""
            + ", \"doubleValue\": \"\"" + ", \"booleanValue\": \"\", \"charValue\": \"\"}";
        // @formatter:on
        gson.fromJson(json, PrimitiveBundleInitialized.class);
    }

    @Test public void fromJsonValidValueWithinString() {
        Gson gson = new Gson();
        // @formatter:off
        String json = "{\"byteValue\": \"15\", \"shortValue\": \"15\", "
            + "\"intValue\": \"15\", " + "\"longValue\": \"15\", \"floatValue\": \"15.0\""
            + ", \"doubleValue\": \"15.0\"" + ", \"booleanValue\": \"false\", \"charValue\": \"z\"}";
        // @formatter:on
        PrimitiveBundleInitialized model = gson.fromJson(json,
            PrimitiveBundleInitialized.class);

        assertEquals(15, model.byteValue);
        assertEquals(15, model.shortValue);
        assertEquals(15, model.intValue);
        assertEquals(15, model.longValue);
        assertEquals(15, model.floatValue, 0.0001);
        assertEquals(15, model.doubleValue, 0.0001);
        assertFalse(model.booleanValue);
        assertEquals('z', model.charValue);
    }

    @Test public void fromJsonBooleanFrom2ValueInteger() {
        String json = "{\"value\": 1}";
        Gson gson = new Gson();

        try {
            gson.fromJson(json, BooleanExample.class);
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
        builder.registerTypeAdapter(BooleanExample.class,
            new BooleanAs2ValueIntegerDeserializer());

        Gson gson = builder.create();

        BooleanExample model = gson.fromJson(json, BooleanExample.class);

        assertTrue(model.value);
    }

    @Test public void fromJsonBooleanFromYes() {
        String json = "{\"value\": yes}";
        Gson gson = new Gson();

        BooleanExample model = gson.fromJson(json, BooleanExample.class);

        // pay attention here that we are deserializing yes.
        assertFalse(model.value);
    }

    @Test public void fromJsonBooleanFromInvalidValue() {
        String json = "{\"value\": \"15x\"}";
        Gson gson = new Gson();

        BooleanExample model = gson.fromJson(json, BooleanExample.class);

        assertFalse(model.value);
    }

    // @formatter:off
    static class BooleanAs2ValueIntegerDeserializer implements JsonDeserializer<BooleanExample> {
        @Override public BooleanExample deserialize(
            JsonElement jsonElement,
            Type type,
            JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {

            BooleanExample model = new BooleanExample();
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
