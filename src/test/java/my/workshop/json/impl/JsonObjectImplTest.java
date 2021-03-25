package my.workshop.json.impl;

import my.workshop.json.JsonException;
import my.workshop.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static my.workshop.json.JsonFactory.jsonNull;
import static org.junit.jupiter.api.Assertions.*;

public class JsonObjectImplTest {

    @Test
    void contains() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "field1";

        assertFalse(jo.contains(fieldName));
        assertNull(jo.findValue(fieldName));

        jo.setField(fieldName, jsonNull());

        assertTrue(jo.contains(fieldName));
        assertNotNull(jo.findValue(fieldName));
    }

    @Test
    void getFieldNames() {

    }

    @Test
    void findValue() {
    }

    @Test
    void getValue() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";

        Assertions.assertFalse(jo.contains(fieldName));
        Assertions.assertThrows(JsonException.class, () -> jo.getValue(fieldName));

        jo.setField(fieldName, jsonNull());

        Assertions.assertTrue(jo.contains(fieldName));
        Assertions.assertNotNull(jo.getValueType(fieldName));
    }

    @Test
    void getValueType() {
    }

    @Test
    void getString() {
    }

    @Test
    void getArray() {
    }

    @Test
    void getObject() {
    }

    @Test
    void getNumberAsInt() {
    }

    @Test
    void getNumberAsLong() {
    }

    @Test
    void getNumberAsFloat() {
    }

    @Test
    void getNumberAsDouble() {
    }

}