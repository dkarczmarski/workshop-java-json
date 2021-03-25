package my.workshop.json.util;

import my.workshop.json.JsonFactory;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import static my.workshop.json.JsonFactory.*;

public class JsonObjectBuilder {

    private final JsonObject jo;

    public JsonObjectBuilder(JsonObject jo) {
        this.jo = jo;
    }

    public static JsonObjectBuilder jsonObjectBuilder(JsonObject jo) {
        return new JsonObjectBuilder(jo);
    }

    public static JsonObjectBuilder jsonObjectBuilder() {
        return jsonObjectBuilder(JsonFactory.createJsonObject());
    }

    public JsonObjectBuilder setNull(String fieldName) {
        jo.setField(fieldName, jsonNull());
        return this;
    }

    public JsonObjectBuilder setBoolean(String fieldName, Boolean value) {
        jo.setField(fieldName, jsonBoolean(value));
        return this;
    }

    public JsonObjectBuilder setObject(String fieldName, JsonObject value) {
        jo.setField(fieldName, jsonObject(value));
        return this;
    }

    public JsonObjectBuilder setArray(String fieldName, JsonValue... values) {
        jo.setField(fieldName, jsonArray(values));
        return this;
    }

    public JsonObjectBuilder setNumber(String fieldName, Long value) {
        jo.setField(fieldName, jsonNumber(value));
        return this;
    }

    public JsonObjectBuilder setString(String fieldName, String value) {
        jo.setField(fieldName, jsonString(value));
        return this;
    }

    public JsonObject get() {
        return jo;
    }
}
