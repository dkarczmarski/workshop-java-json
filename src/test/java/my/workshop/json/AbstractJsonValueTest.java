package my.workshop.json;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static my.workshop.json.JsonFactory.jsonNull;
import static my.workshop.json.JsonFactory.jsonString;
import static org.junit.jupiter.api.Assertions.*;

public abstract class AbstractJsonValueTest {

    abstract JsonObject newJsonObject();

    abstract JsonValue newJsonValue();

    @Test
    void newNullTypeObject() {
        JsonValue jv = newJsonValue();

        Assertions.assertEquals(JsonValueType.NULL, jv.getType());
    }

    @Test
    void setString() {
        JsonValue jv = newJsonValue();
        jv.setString("value123");

        Assertions.assertEquals(JsonValueType.STRING, jv.getType());
        Assertions.assertEquals("value123", jv.getString());
    }

    @Test
    void setBoolean() {
        JsonValue jv = newJsonValue();
        jv.setBoolean(true);

        Assertions.assertEquals(JsonValueType.BOOLEAN, jv.getType());
        Assertions.assertEquals(true, jv.getBoolean());
    }

    @Test
    void setNumber() {
        JsonValue jv = newJsonValue();

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
        JsonValue jv = newJsonValue();
        jv.setArray(jsonString("v1"), jsonString("v2"));

        Assertions.assertEquals(JsonValueType.ARRAY, jv.getType());
        Assertions.assertEquals(2, jv.getArray().length);
        Assertions.assertEquals("v1", jv.getArray()[0].getString());
        Assertions.assertEquals("v2", jv.getArray()[1].getString());
    }

    @Test
    void setObject() {
        JsonObject jo = newJsonObject();
        JsonValue jv = newJsonValue();
        jv.setObject(jo);

        assertEquals(JsonValueType.OBJECT, jv.getType());
        assertEquals(jo, jv.getObject());
    }

    @Test
    void changeType() {
        JsonValue jv = newJsonValue();
        assertEquals(JsonValueType.NULL, jv.getType());
        assertThrows(JsonException.class, () -> jv.getBoolean());
        assertThrows(JsonException.class, () -> jv.getString());

        jv.setBoolean(false);
        assertEquals(JsonValueType.BOOLEAN, jv.getType());
        assertDoesNotThrow(() -> jv.getBoolean());
        assertThrows(JsonException.class, () -> jv.getString());

        jv.setString("abc");
        assertEquals(JsonValueType.STRING, jv.getType());
        assertThrows(JsonException.class, () -> jv.getBoolean());
        assertDoesNotThrow(() -> jv.getString());
    }

    @Test
    void setNull() {
        JsonValue jv = newJsonValue();

        jv.setNull();
        assertValueType(jv, JsonValueType.NULL);
    }

    @Test
    void testSetObject() {
        JsonValue jv = newJsonValue();

        jv.setObject(newJsonObject());
        assertValueType(jv, JsonValueType.OBJECT);
    }

    @Test
    void testSetString() {
        JsonValue jv = newJsonValue();

        jv.setString("value");
        assertValueType(jv, JsonValueType.STRING);
    }

    @Test
    void testSetNumber() {
        JsonValue jv = newJsonValue();

        jv.setNumber("123");
        assertValueType(jv, JsonValueType.NUMBER);

        jv.setNumber(123);
        assertValueType(jv, JsonValueType.NUMBER);

        jv.setNumber(123.0);
        assertValueType(jv, JsonValueType.NUMBER);
    }

    @Test
    void testSetBoolean() {
        JsonValue jv = newJsonValue();

        jv.setBoolean(true);
        assertValueType(jv, JsonValueType.BOOLEAN);
    }

    @Test
    void testSetArray() {
        JsonValue jv = newJsonValue();

        jv.setArray(jsonNull());
        assertValueType(jv, JsonValueType.ARRAY);
    }

    static void assertValueType(JsonValue jv, JsonValueType expectedType) {
        assertEquals(expectedType, jv.getType());

        for (JsonValueType type : JsonValueType.values()) {
            if (type.equals(JsonValueType.NULL)) {
                continue;
            }
            JsonValueTestSupprt.ValueTypeDesc desc = JsonValueTestSupprt.getDesc(type);
            if (type.equals(expectedType)) {
                assertDoesNotThrow(() -> desc.getValueSupplier().apply(jv), type.toString());
            } else {
                assertThrows(JsonException.class, () -> desc.getValueSupplier().apply(jv), type.toString());
            }
        }
    }

}
