package my.workshop.json.util;

import my.workshop.json.JsonObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static my.workshop.json.util.JsonObjectReader.jsonIterator;

public class JsonObjectReaderTest {

    @Test
    public void deserializeEmptyObject() {
        String s = "{}";
        JsonObject jo = JsonObjectReader.read(s);

        Assertions.assertFalse(jo.getFieldNames().hasNext());

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithNull() {
        String s = "{ \"nullField\": null }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithFalse() {
        String s = "{ \"falseField\": false }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithTrue() {
        String s = "{ \"trueField\": true }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithString() {
        String s = "{ \"stringField\": \"value123\" }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithNumericInt() {
        String s = "{ \"numericIntField\": 123 }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithNumericFloat() {
        String s = "{ \"numericFloatField\": 123.456 }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjectWithObject() {
        String s = "{ \"objectField\": { \"stringField\": \"value123\"} }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjevctWithEmptyArray() {
        String  s = "{ \"arrayField\": [] }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void deserializeObjevctWithArray() {
        String  s = "{ \"arrayField\": [ null, true, false, \"string123\"] }";
        JsonObject jo = JsonObjectReader.read(s);

        System.out.println(JsonObjectWriter.serialize(jo));
    }

    @Test
    public void test1() {
        String s = "10.0E+2";
        System.out.println(Double.valueOf(s).toString());
    }

    @Test
    void readJsonString() {
        String s = "\"mystring\"";
        String s1 = JsonObjectReader.readQuotedString(jsonIterator(s));
    }

    @Test
    void jsonIterator_skipWhitespace() {
        String s = " \t \r \n a b";
        JsonObjectReader.JsonIterator ji = jsonIterator(s);
        Assertions.assertEquals(s, ji.getData());
        Assertions.assertEquals(-1, ji.getDataIndex());
        Assertions.assertEquals(7, ji.skipWhitespace());
        Assertions.assertEquals(6, ji.getDataIndex());
        Assertions.assertEquals(0, ji.skipWhitespace());
        Assertions.assertEquals(6, ji.getDataIndex());
    }

    @Test
    void jsonIterator_nextChar() {
        String s = "a b";
        JsonObjectReader.JsonIterator ji = jsonIterator(s);
        Assertions.assertEquals(s, ji.getData());
        Assertions.assertEquals(-1, ji.getDataIndex());
        Assertions.assertEquals('a', ji.nextChar());
        Assertions.assertEquals(0, ji.getDataIndex());
        Assertions.assertEquals(' ', ji.nextChar());
        Assertions.assertEquals(1, ji.getDataIndex());
    }

    @Test
    void jsonIterator_peekChar() {
        String s = "a b";
        JsonObjectReader.JsonIterator ji = jsonIterator(s);
        Assertions.assertEquals(s, ji.getData());
        Assertions.assertEquals(-1, ji.getDataIndex());
        Assertions.assertEquals('a', ji.peekChar());
        Assertions.assertEquals(-1, ji.getDataIndex());
        Assertions.assertEquals('a', ji.peekChar());
        Assertions.assertEquals(-1, ji.getDataIndex());
    }
}