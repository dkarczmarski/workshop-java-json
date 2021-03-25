package my.workshop.json;

import my.workshop.json.impl.JsonObjectImpl;
import my.workshop.json.impl.JsonValueImpl;

import java.util.function.BiConsumer;

public class JsonFactory {

    public static JsonObject createJsonObject() {
        return new JsonObjectImpl();
    }

    public static JsonValue createJsonValue() {
        return new JsonValueImpl();
    }

    private static <V> JsonValue createJsonValue(V value, BiConsumer<JsonValue, V> setter) {
        JsonValue o = createJsonValue();
        setter.accept(o, value);

        return o;
    }

    public static JsonValue jsonNull() {
        return createJsonValue();
    }
    
    public static JsonValue jsonString(String value) {
        return createJsonValue(value, JsonValue::setString);
    }

    public static JsonValue jsonBoolean(Boolean value) {
        return createJsonValue(value, JsonValue::setBoolean);
    }

    public static JsonValue jsonArray(JsonValue... values) {
        return createJsonValue(values, JsonValue::setArray);
    }

    public static JsonValue jsonNumber(String value) {
        return createJsonValue(value, JsonValue::setNumber);
    }

    public static JsonValue jsonNumber(long value) {
        return createJsonValue(value, JsonValue::setNumber);
    }

    public static JsonValue jsonNumber(double value) {
        return createJsonValue(value, JsonValue::setNumber);
    }

    public static JsonValue jsonObject(JsonObject value) {
        return createJsonValue(value, JsonValue::setObject);
    }

}
