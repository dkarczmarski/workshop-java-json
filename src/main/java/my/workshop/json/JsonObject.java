package my.workshop.json;

import java.util.Iterator;
import java.util.Optional;

public interface JsonObject extends Iterable<String> {

    Iterator<String> getFieldNames();

    boolean contains(String name);

    Optional<JsonValue> findValue(String name);

    default JsonValue getValue(String name) {
        return findValue(name).orElseThrow(() -> new JsonFieldNotExistsException(name));
    }

    default JsonValueType getValueType(String name) {
        return getValue(name).getType();
    }

    void setField(String name, JsonValue value);

    void addField(String name, JsonValue value);

    void addFieldAfter(String name, JsonValue value, String after);

    void addFieldBefore(String name, JsonValue value, String before);

    boolean removeField(String name);

    default String getString(String name) {
        return getValue(name).getString();
    }

    default JsonValue[] getArray(String name) {
        return getValue(name).getArray();
    }

    default JsonObject getObject(String name) {
        return getValue(name).getObject();
    }

    default int getNumberAsInt(String name) {
        return getValue(name).getNumberAsInt();
    }

    default long getNumberAsLong(String name) {
        return getValue(name).getNumberAsLong();
    }

    default float getNumberAsFloat(String name) {
        return getValue(name).getNumberAsFloat();
    }

    default double getNumberAsDouble(String name) {
        return getValue(name).getNumberAsDouble();
    }

}
