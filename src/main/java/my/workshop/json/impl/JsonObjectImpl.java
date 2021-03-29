package my.workshop.json.impl;

import my.workshop.json.*;

import java.util.*;

public class JsonObjectImpl implements JsonObject {

    private final List<String> fieldNameList = new LinkedList<>();
    private final Map<String, JsonValue> fieldsMap = new HashMap<>();

    @Override
    public Iterator<String> getFieldNames() {
        return fieldNameList.iterator();
    }

    @Override
    public boolean contains(String name) {
        return fieldsMap.containsKey(name);
    }

    @Override
    public Optional<JsonValue> findValue(String name) {
        return Optional.ofNullable(fieldsMap.get(name));
    }

    @Override
    public void setField(String name, JsonValue value) {
        if (!fieldNameList.contains(name)) {
            fieldNameList.add(name);
        }
        fieldsMap.put(name, value);
    }

    @Override
    public void addField(String name, JsonValue value) {
        addFieldAt(name, value, fieldNameList.size());
    }

    int fieldIndex(String name) {
        int i = fieldNameList.indexOf(name);
        if (i == -1) {
            throw new JsonFieldNotExistsException(name);
        }

        return i;
    }

    void addFieldAt(String name, JsonValue value, int index) {
        if (fieldNameList.contains(name)) {
            throw new JsonFieldAlreadyExistsException(name);
        }

        fieldNameList.add(index, name);
        fieldsMap.put(name, value);
    }

    @Override
    public void addFieldAfter(String name, JsonValue value, String after) {
        int i = fieldIndex(after);
        addFieldAt(name, value, i + 1);
    }

    @Override
    public void addFieldBefore(String name, JsonValue value, String before) {
        int i = fieldIndex(before);
        addFieldAt(name, value, i);
    }

    @Override
    public boolean removeField(String name) {
        boolean result = fieldNameList.remove(name);
        fieldsMap.remove(name);
        return result;
    }

    @Override
    public Iterator<String> iterator() {
        return getFieldNames();
    }
}
