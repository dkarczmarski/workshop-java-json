package my.workshop.json.util;

import my.workshop.json.JsonObject;
import org.junit.jupiter.api.Test;

import static my.workshop.json.JsonFactory.*;
import static my.workshop.json.util.JsonObjectBuilder.jsonObjectBuilder;

public class JsonObjectWriterTest {

    @Test
    public void firstTest2() {
        JsonObject jo = jsonObjectBuilder()
                .setNull("fieldNull")
                .setNumber("fieldNumber", 123L)
                .setString("fieldString", "mystring")
                .setBoolean("fieldBoolean", true)
                .setArray("fieldArray", jsonBoolean(true), jsonNull())
                .setObject("fieldObject", jsonObjectBuilder()
                        .setString("fieldXxx", "value123")
                        .get())
                .get();

        System.out.println(JsonObjectWriter.serialize(jo));
    }

}