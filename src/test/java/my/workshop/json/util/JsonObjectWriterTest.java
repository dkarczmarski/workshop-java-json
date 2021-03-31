package my.workshop.json.util;

import my.workshop.json.JsonObject;
import org.junit.jupiter.api.Test;

import static my.workshop.json.JsonFactory.*;
import static my.workshop.json.util.JsonObjectBuilder.jsonObjectBuilder;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonObjectWriterTest {

    @Test
    public void writeString() {
        String s = "{\"fieldNull\":null,\"fieldNumber1\":123,\"fieldNumber2\":123.456,\"fieldNumber3\":12e2,\"fieldString\":\"mystring\",\"fieldBoolean\":true,\"fieldArray\":[true,null],\"fieldObject\":{\"fieldString\":\"value123\"}}";

        JsonObject jo = jsonObjectBuilder()
                .setNull("fieldNull")
                .setNumber("fieldNumber1", 123L)
                .setNumber("fieldNumber2", 123.456)
                .setNumber("fieldNumber3", "12e2")
                .setString("fieldString", "mystring")
                .setBoolean("fieldBoolean", true)
                .setArray("fieldArray", jsonBoolean(true), jsonNull())
                .setObject("fieldObject", jsonObjectBuilder()
                        .setString("fieldString", "value123")
                        .get())
                .get();

        assertEquals(s, JsonObjectWriter.serialize(jo));

        System.out.println(JsonObjectWriter.serialize(jo));
    }

}