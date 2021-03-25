package my.workshop.json.impl;

import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;
import my.workshop.json.JsonValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static my.workshop.json.JsonFactory.jsonString;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonValueImplTest {

    @Test
    void newNullTypeObject() {
        JsonValue jv = new JsonValueImpl();

        Assertions.assertEquals(JsonValueType.NULL, jv.getType());
    }

    @Test
    void setString() {
        JsonValue jv = new JsonValueImpl();
        jv.setString("value123");

        Assertions.assertEquals(JsonValueType.STRING, jv.getType());
        Assertions.assertEquals("value123", jv.getString());
    }

    @Test
    void setBoolean() {
        JsonValue jv = new JsonValueImpl();
        jv.setBoolean(true);

        Assertions.assertEquals(JsonValueType.BOOLEAN, jv.getType());
        Assertions.assertEquals(true, jv.getBoolean());
    }

    @Test
    void setNumber() {
        JsonValue jv = new JsonValueImpl();
        
        jv.setNumber("111");
        Assertions.assertEquals(JsonValueType.NUMBER, jv.getType());
        Assertions.assertEquals("111", jv.getNumberAsString());
        Assertions.assertEquals(111, jv.getNumberAsInt());
        Assertions.assertEquals(111, jv.getNumberAsLong());
        Assertions.assertEquals(111, jv.getNumberAsFloat());
        Assertions.assertEquals(111, jv.getNumberAsDouble());

        jv.setNumber(222L);
        Assertions.assertEquals(JsonValueType.NUMBER, jv.getType());
        Assertions.assertEquals("222", jv.getNumberAsString());
        Assertions.assertEquals(222, jv.getNumberAsInt());
        Assertions.assertEquals(222, jv.getNumberAsLong());
        Assertions.assertEquals(222, jv.getNumberAsFloat());
        Assertions.assertEquals(222, jv.getNumberAsDouble());

        jv.setNumber(333D);
        Assertions.assertEquals(JsonValueType.NUMBER, jv.getType());
        Assertions.assertEquals("333.0", jv.getNumberAsString());
        Assertions.assertEquals(333, jv.getNumberAsInt());
        Assertions.assertEquals(333, jv.getNumberAsLong());
        Assertions.assertEquals(333, jv.getNumberAsFloat());
        Assertions.assertEquals(333, jv.getNumberAsDouble());

        jv.setNumber("400e-2");
        Assertions.assertEquals(JsonValueType.NUMBER, jv.getType());
        Assertions.assertEquals("400e-2", jv.getNumberAsString());
        Assertions.assertEquals(4, jv.getNumberAsInt());
        Assertions.assertEquals(4, jv.getNumberAsLong());
        Assertions.assertEquals(4, jv.getNumberAsFloat());
        Assertions.assertEquals(4, jv.getNumberAsDouble());
    }

    @Test
    void setArray() {
        JsonValue jv = new JsonValueImpl();
        jv.setArray(jsonString("v1"), jsonString("v2"));

        Assertions.assertEquals(JsonValueType.ARRAY, jv.getType());
        Assertions.assertEquals(2, jv.getArray().length);
        Assertions.assertEquals("v1", jv.getArray()[0].getString());
        Assertions.assertEquals("v2", jv.getArray()[1].getString());
    }

    @Test
    void setObject() {
        JsonObject jo = new JsonObjectImpl();
        JsonValue jv = new JsonValueImpl();
        jv.setObject(jo);

        assertEquals(JsonValueType.OBJECT, jv.getType());
        assertEquals(jo, jv.getObject());
    }

    @Test
    void changeType() {
        JsonValue jv = new JsonValueImpl();
        assertEquals(JsonValueType.NULL, jv.getType());

        jv.setBoolean(false);
        assertEquals(JsonValueType.BOOLEAN, jv.getType());

        jv.setString("abc");
        assertEquals(JsonValueType.STRING, jv.getType());
    }
}