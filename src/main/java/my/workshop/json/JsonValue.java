package my.workshop.json;

public interface JsonValue {

    JsonValueType getType();

    default boolean isNull() {
        return JsonValueType.NULL == getType();
    }

    default boolean isObject() {
        return JsonValueType.OBJECT == getType();
    }

    default boolean isNumber() {
        return JsonValueType.NUMBER == getType();
    }

    default boolean isBoolean() {
        return JsonValueType.BOOLEAN == getType();
    }

    default boolean isArray() {
        return JsonValueType.ARRAY == getType();
    }

    default boolean isString() {
        return JsonValueType.STRING == getType();
    }

    String getString();

    JsonObject getObject();

    int getNumberAsInt();

    long getNumberAsLong();

    float getNumberAsFloat();

    double getNumberAsDouble();

    String getNumberAsString();

    Boolean getBoolean();

    JsonValue[] getArray();

    void setNull();

    void setObject(JsonObject value);

    void setString(String value);

    void setNumber(String value);

    void setNumber(long value);

    void setNumber(double value);

    void setBoolean(Boolean value);

    void setArray(JsonValue... value);

}
