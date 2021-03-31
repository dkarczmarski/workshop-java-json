package my.workshop.json;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

public class JsonValueTestSupprt {

    public static class ValueTypeDesc {
        final Function<JsonValue, Object> valueSupplier;
        final Function<JsonValue, Boolean> isTypeSupplier;

        ValueTypeDesc(Function<JsonValue, Object> valueSupplier, Function<JsonValue, Boolean> isTypeSupplier) {
            this.valueSupplier = valueSupplier;
            this.isTypeSupplier = isTypeSupplier;
        }

        public Function<JsonValue, Object> getValueSupplier() {
            return valueSupplier;
        }

        public Function<JsonValue, Boolean> getIsTypeSupplier() {
            return isTypeSupplier;
        }
    }

    static private final Map<JsonValueType, ValueTypeDesc> map = new HashMap<>();

    static {
        map.put(JsonValueType.ARRAY, new ValueTypeDesc(JsonValue::getArray, JsonValue::isArray));
        map.put(JsonValueType.BOOLEAN, new ValueTypeDesc(JsonValue::getBoolean, JsonValue::isBoolean));
        map.put(JsonValueType.NULL, new ValueTypeDesc(o -> null, JsonValue::isNull));
        map.put(JsonValueType.NUMBER, new ValueTypeDesc(JsonValue::getNumberAsString, JsonValue::isNumber));
        map.put(JsonValueType.OBJECT, new ValueTypeDesc(JsonValue::getObject, JsonValue::isObject));
        map.put(JsonValueType.STRING, new ValueTypeDesc(JsonValue::getString, JsonValue::isString));
    }

    public static ValueTypeDesc getDesc(JsonValueType type) {
        return Optional.ofNullable(map.get(type)).orElseThrow(() -> new RuntimeException(type.name()));
    }

}
