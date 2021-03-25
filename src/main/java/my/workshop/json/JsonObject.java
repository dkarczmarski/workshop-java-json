package my.workshop.json;

import java.util.Iterator;

public interface JsonObject extends Iterable<String> {

    Iterator<String> getFieldNames();

    boolean contains(String fieldName);

    JsonValue findValue(String fieldName);

    default JsonValue getValue(String fieldName) {
        JsonValue value = findValue(fieldName);
        if (value == null) {
            throw new JsonException("field does not exist");
        }

        return value;
    }

    default JsonValueType getValueType(String fieldName) {
        return getValue(fieldName).getType();
    }

    void setField(String fieldName, JsonValue value);

    default String getString(String fieldName) {
        return getValue(fieldName).getString();
    }

    default JsonValue[] getArray(String fieldName) {
        return getValue(fieldName).getArray();
    }

    default JsonObject getObject(String fieldName) {
        return getValue(fieldName).getObject();
    }

    default int getNumberAsInt(String fieldName) {
        return getValue(fieldName).getNumberAsInt();
    }

    default long getNumberAsLong(String fieldName) {
        return getValue(fieldName).getNumberAsLong();
    }

    default float getNumberAsFloat(String fieldName) {
        return getValue(fieldName).getNumberAsFloat();
    }

    default double getNumberAsDouble(String fieldName) {
        return getValue(fieldName).getNumberAsDouble();
    }

}
