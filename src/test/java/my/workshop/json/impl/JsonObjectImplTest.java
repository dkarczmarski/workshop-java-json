package my.workshop.json.impl;

import my.workshop.json.JsonException;
import my.workshop.json.JsonFieldNotExistsException;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Iterator;
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
        assertFalse(jo.findValue(fieldName).isPresent());

        jo.setField(fieldName, jsonNull());

        assertTrue(jo.contains(fieldName));
        assertTrue(jo.findValue(fieldName).isPresent());
    }

    @Test
    void getFieldNames() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName1 = "field1";
        final String fieldName2 = "field2";
        final String fieldName3 = "field3";

        jo.addField(fieldName1, jsonNull());
        jo.addField(fieldName2, jsonNull());
        jo.addField(fieldName3, jsonNull());

        Iterator<String> fieldNames = jo.getFieldNames();
        assertEquals(fieldName1, fieldNames.next());
        assertEquals(fieldName2, fieldNames.next());
        assertEquals(fieldName3, fieldNames.next());
    }

    @Test
    void findValue() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "field1";

        assertFalse(jo.findValue(fieldName).isPresent());

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

    @Test
    void addField() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName1 = "field1";
        final String fieldName2 = "field2";
        final String fieldName3 = "field3";

        jo.addField(fieldName1, jsonNull());
        jo.addField(fieldName2, jsonNull());
        jo.addField(fieldName3, jsonNull());

        Iterator<String> fieldNames = jo.getFieldNames();
        assertEquals(fieldName1, fieldNames.next());
        assertEquals(fieldName2, fieldNames.next());
        assertEquals(fieldName3, fieldNames.next());
    }

    @Test
    void addFieldAfter() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName1 = "field1";
        final String fieldName2 = "field2";
        final String fieldName3 = "field3";

        jo.addField(fieldName1, jsonNull());
        jo.addField(fieldName2, jsonNull());
        jo.addField(fieldName3, jsonNull());

        final String fieldName4 = "field4";
        jo.addFieldAfter(fieldName4, jsonNull(), fieldName1);

        Iterator<String> fieldNames = jo.getFieldNames();
        assertEquals(fieldName1, fieldNames.next());
        assertEquals(fieldName4, fieldNames.next());
        assertEquals(fieldName2, fieldNames.next());
        assertEquals(fieldName3, fieldNames.next());
    }

    @Test
    void addFieldBefore() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName1 = "field1";
        final String fieldName2 = "field2";
        final String fieldName3 = "field3";

        jo.addField(fieldName1, jsonNull());
        jo.addField(fieldName2, jsonNull());
        jo.addField(fieldName3, jsonNull());

        final String fieldName4 = "field4";
        jo.addFieldBefore(fieldName4, jsonNull(), fieldName1);

        final String fieldName5 = "field5";
        jo.addFieldBefore(fieldName5, jsonNull(), fieldName3);

        Iterator<String> fieldNames = jo.getFieldNames();
        assertEquals(fieldName4, fieldNames.next());
        assertEquals(fieldName1, fieldNames.next());
        assertEquals(fieldName2, fieldNames.next());
        assertEquals(fieldName5, fieldNames.next());
        assertEquals(fieldName3, fieldNames.next());
    }

    @Test
    void removeField() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "myfield";

        assertFalse(jo.contains(fieldName));
        jo.addField(fieldName, jsonNull());
        assertTrue(jo.contains(fieldName));

        assertTrue(jo.removeField(fieldName));
        assertFalse(jo.contains(fieldName));
        assertFalse(jo.removeField(fieldName));
    }

    @Test
    void setField() {
        JsonObject jo = new JsonObjectImpl();
        final String fieldName = "field1";
        final String fieldValue1 = "v1";
        final int fieldValue2 = 123;

        assertFalse(jo.contains(fieldName));

        jo.setField(fieldName, jsonString(fieldValue1));
        assertTrue(jo.contains(fieldName));
        assertEquals(JsonValueType.STRING, jo.getValueType(fieldName));
        assertEquals(fieldValue1, jo.getString(fieldName));

        jo.setField(fieldName, jsonNumber(fieldValue2));
        assertTrue(jo.contains(fieldName));
        assertEquals(JsonValueType.NUMBER, jo.getValueType(fieldName));
        assertEquals(fieldValue2, jo.getNumberAsInt(fieldName));
    }
}