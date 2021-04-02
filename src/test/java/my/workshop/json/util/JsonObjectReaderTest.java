package my.workshop.json.util;

import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;
import my.workshop.json.JsonValueType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonObjectReaderTest {

    @Test
    public void deserializeEmptyObject() {
        String s = "{}";
        JsonObject jo = JsonObjectReader.read(s);

        Assertions.assertFalse(jo.getFieldNames().hasNext());

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    static boolean checkFields(JsonObject jo, String... fields) {
        Iterator<String> fieldNames = jo.getFieldNames();

        for (int i = 0, n = fields.length; i < n; ++i) {
            if (!fieldNames.hasNext()) {
                return false;
            }

            if (!fields[i].equals(fieldNames.next())) {
                return false;
            }
        }

        return !fieldNames.hasNext();
    }

    @Test
    public void deserializeObjectWithNull() {
        String s = "{ \"nullField\": null }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.NULL, jo.getValue("nullField").getType());
        assertTrue(checkFields(jo, "nullField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithFalse() {
        String s = "{ \"falseField\": false }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.BOOLEAN, jo.getValue("falseField").getType());
        assertEquals(false, jo.getValue("falseField").getBoolean());
        assertTrue(checkFields(jo, "falseField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithTrue() {
        String s = "{ \"trueField\": true }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.BOOLEAN, jo.getValue("trueField").getType());
        assertEquals(true, jo.getValue("trueField").getBoolean());
        assertTrue(checkFields(jo, "trueField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithString() {
        String s = "{ \"stringField\": \"value123\" }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.STRING, jo.getValue("stringField").getType());
        assertEquals("value123", jo.getValue("stringField").getString());
        assertTrue(checkFields(jo, "stringField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithNumericInt() {
        String s = "{ \"numericIntField\": 123 }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.NUMBER, jo.getValue("numericIntField").getType());
        assertEquals("123", jo.getValue("numericIntField").getNumberAsString());
        assertEquals(123, jo.getValue("numericIntField").getNumberAsInt());
        assertTrue(checkFields(jo, "numericIntField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithNumericFloat() {
        String s = "{ \"numericFloatField\": 123.456 }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.NUMBER, jo.getValue("numericFloatField").getType());
        assertEquals("123.456", jo.getValue("numericFloatField").getNumberAsString());
        assertEquals(123.456f, jo.getValue("numericFloatField").getNumberAsFloat());
        assertTrue(checkFields(jo, "numericFloatField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithObject() {
        String s = "{ \"objectField\": { \"stringField\": \"value123\"} }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.OBJECT, jo.getValue("objectField").getType());
        assertTrue(checkFields(jo, "objectField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithEmptyArray() {
        String s = "{ \"arrayField\": [] }";
        JsonObject jo = JsonObjectReader.read(s);

        assertEquals(JsonValueType.ARRAY, jo.getValue("arrayField").getType());
        assertTrue(checkFields(jo, "arrayField"));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithArray() {
        String s = "{ \"arrayField\": [ null, true, false, \"string123\"] }";
        JsonObject jo = JsonObjectReader.read(s);

        JsonValue jv = jo.getValue("arrayField");

        assertEquals(JsonValueType.ARRAY, jv.getType());
        assertTrue(checkFields(jo, "arrayField"));
        assertEquals(JsonValueType.NULL, jv.getArray()[0].getType());
        assertEquals(JsonValueType.BOOLEAN, jv.getArray()[1].getType());
        assertEquals(JsonValueType.BOOLEAN, jv.getArray()[2].getType());
        assertEquals(JsonValueType.STRING, jv.getArray()[3].getType());

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    static JsonObjectReader.JsonIterator jsonIterator(String s) {
        return new JsonObjectReader.JsonIterator(new StringReader(s));
    }

    @Test
    void readJsonString() throws IOException {
        String s = "\"mystring\"";
        assertEquals("mystring", JsonObjectReader.readQuotedString(jsonIterator(s)));
    }

    @Test
    void jsonIterator_skipWhitespace() throws IOException {
        String s = " \t \r \n a b";
        JsonObjectReader.JsonIterator ji = jsonIterator(s);
        assertEquals(-1, ji.getBuffIndex());
        assertEquals('a', ji.skipWhitespace());
        assertEquals(6, ji.getBuffIndex());
        assertEquals('a', ji.skipWhitespace());
        assertEquals(6, ji.getBuffIndex());
    }

    @Test
    void jsonIterator_nextChar() throws IOException {
        String s = "a b";
        JsonObjectReader.JsonIterator ji = jsonIterator(s);
        assertEquals(-1, ji.getBuffIndex());
        assertEquals('a', ji.getChar(false, true));
        assertEquals(0, ji.getBuffIndex());
        assertEquals(' ', ji.getChar(false, true));
        assertEquals(1, ji.getBuffIndex());
    }

    @Test
    void jsonIterator_peekChar() throws IOException {
        String s = "a b";
        JsonObjectReader.JsonIterator ji = jsonIterator(s);
        assertEquals(-1, ji.getBuffIndex());
        assertEquals('a', ji.getChar(false, false));
        assertEquals(-1, ji.getBuffIndex());
        assertEquals('a', ji.getChar(false, false));
        assertEquals(-1, ji.getBuffIndex());
    }
}