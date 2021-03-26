package my.workshop.json.impl;

import my.workshop.json.JsonException;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static my.workshop.json.JsonFactory.*;
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
        JsonObject jo = new JsonObjectImpl();
        final String fieldName1 = "field1";
        final String fieldName2 = "field2";
        final String fieldName3 = "field3";

        jo.setField(fieldName1, jsonNull());
        jo.setField(fieldName2, jsonNull());
        jo.setField(fieldName3, jsonNull());

        Set<String> set = StreamSupport.stream(jo.spliterator(), false).collect(Collectors.toSet());
        assertEquals(3, set.size());
        assertTrue(set.contains(fieldName1));
        assertTrue(set.contains(fieldName2));
        assertTrue(set.contains(fieldName3));
    }

    @Test
    void findValue() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "field1";

        assertNull(jo.findValue(fieldName));

        jo.setField(fieldName, jsonString("v1"));
        assertNotNull(jo.findValue(fieldName));
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
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";

        jo.setField(fieldName, jsonString("v1"));
        assertEquals(JsonValueType.STRING, jo.getValueType(fieldName));
    }

    @Test
    void getString() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";
        final String fieldValue = "myvalue123";

        jo.setField(fieldName, jsonString(fieldValue));

        assertEquals(fieldValue, jo.getString(fieldName));
    }

    @Test
    void getArray() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";

        jo.setField(fieldName, jsonArray(jsonString("v1"), jsonString("v2")));

        assertEquals("v1", jo.getArray(fieldName)[0].getString());
        assertEquals("v2", jo.getArray(fieldName)[1].getString());
    }

    @Test
    void getObject() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";

        JsonObject inner = new JsonObjectImpl();
        jo.setField(fieldName, jsonObject(inner));

        assertEquals(inner, jo.getObject(fieldName));
    }

    @Test
    void getNumberAsInt() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";
        final String fieldValue = "1234";

        jo.setField(fieldName, jsonNumber(fieldValue));

        assertEquals(1234, jo.getNumberAsInt(fieldName));
    }

    @Test
    void getNumberAsLong() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";
        final String fieldValue = "1234";

        jo.setField(fieldName, jsonNumber(fieldValue));

        assertEquals(1234L, jo.getNumberAsLong(fieldName));
    }

    @Test
    void getNumberAsFloat() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";
        final String fieldValue = "1234.5";

        jo.setField(fieldName, jsonNumber(fieldValue));

        assertEquals(1234.5, jo.getNumberAsFloat(fieldName));
    }

    @Test
    void getNumberAsDouble() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";
        final String fieldValue = "1234.5";

        jo.setField(fieldName, jsonNumber(fieldValue));

        assertEquals(1234.5d, jo.getNumberAsDouble(fieldName));
    }

}