package my.workshop.json;

import org.junit.jupiter.api.Test;

import java.util.Iterator;

import static my.workshop.json.util.JsonObjectBuilder.jsonObjectBuilder;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JsonObjectTest {

    @Test
    void getFieldNames() {

        JsonObject jo = jsonObjectBuilder()
                .setString("field1", "value1")
                .get();

        Iterator<String> iterator = jo.getFieldNames();
        iterator.next();
        assertThrows(UnsupportedOperationException.class, () -> iterator.remove());
    }
}