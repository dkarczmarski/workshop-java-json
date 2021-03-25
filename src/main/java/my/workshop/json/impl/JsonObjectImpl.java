package my.workshop.json.impl;

import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class JsonObjectImpl implements JsonObject {

    private final Map<String, JsonValue> fields = new HashMap<>();
    
    @Override
    public Iterator<String> getFieldNames() {
        return fields.keySet().iterator();
    }

    @Override
    public boolean contains(String fieldName) {
        return fields.containsKey(fieldName);
    }

    @Override
    public JsonValue findValue(String fieldName) {
        return fields.get(fieldName);
    }

    @Override
    public void setField(String fieldName, JsonValue value) {
        fields.put(fieldName, value);
    }

    @Override
    public Iterator<String> iterator() {
        return getFieldNames();
    }
}
