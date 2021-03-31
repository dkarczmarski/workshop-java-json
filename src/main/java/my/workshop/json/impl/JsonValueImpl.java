package my.workshop.json.impl;

import my.workshop.json.JsonException;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;
import my.workshop.json.JsonValueType;

public class JsonValueImpl implements JsonValue {

    private JsonValueType type;

    private String stringValue;

    private Boolean booleanValue;

    private String numericValue;

    private JsonValue[] arrayValue;

    private JsonObject objectValue;

    public JsonValueImpl() {
        setNull();
    }

    private void checkType(JsonValueType expectedType) {
        if (type != expectedType) {
            throw new JsonException(String.format("Bad value type: actual= %s, expected= %s", type, expectedType));
        }
    }

    private static Double numeric(String s) {
        return Double.valueOf(s);
    }

    @Override
    public JsonValueType getType() {
        return type;
    }

    @Override
    public String getString() {
        checkType(JsonValueType.STRING);
        return stringValue;
    }

    @Override
    public JsonObject getObject() {
        checkType(JsonValueType.OBJECT);
        return objectValue;
    }

    @Override
    public int getNumberAsInt() {
        checkType(JsonValueType.NUMBER);
        return numeric(numericValue).intValue();
    }

    @Override
    public long getNumberAsLong() {
        checkType(JsonValueType.NUMBER);
        return numeric(numericValue).longValue();
    }

    @Override
    public float getNumberAsFloat() {
        checkType(JsonValueType.NUMBER);
        return numeric(numericValue).floatValue();
    }

    @Override
    public double getNumberAsDouble() {
        checkType(JsonValueType.NUMBER);
        return numeric(numericValue).doubleValue();
    }

    @Override
    public String getNumberAsString() {
        checkType(JsonValueType.NUMBER);
        return numericValue;
    }

    @Override
    public Boolean getBoolean() {
        checkType(JsonValueType.BOOLEAN);
        return booleanValue;
    }

    @Override
    public JsonValue[] getArray() {
        checkType(JsonValueType.ARRAY);
        return arrayValue;
    }

    private void resetType(JsonValueType type) {
        this.type = type;
        stringValue = null;
        booleanValue = null;
        numericValue = null;
        arrayValue = null;
        objectValue = null;
    }

    @Override
    public void setNull() {
        resetType(JsonValueType.NULL);
   }

    @Override
    public void setObject(JsonObject value) {
        resetType(JsonValueType.OBJECT);
        objectValue = value;
    }

    @Override
    public void setString(String value) {
        resetType(JsonValueType.STRING);
        stringValue = value;
    }

    @Override
    public void setNumber(String value) {
        Double.valueOf(value);
        resetType(JsonValueType.NUMBER);
        numericValue = value;
    }

    @Override
    public void setNumber(long value) {
        resetType(JsonValueType.NUMBER);
        numericValue = Long.valueOf(value).toString();
    }

    @Override
    public void setNumber(double value) {
        resetType(JsonValueType.NUMBER);
        numericValue = Double.valueOf(value).toString();
    }

    @Override
    public void setBoolean(Boolean value) {
        resetType(JsonValueType.BOOLEAN);
        booleanValue = value;
    }

    @Override
    public void setArray(JsonValue... value) {
        resetType(JsonValueType.ARRAY);
        arrayValue = value;
    }
}
