package my.workshop.json.util;

import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class JsonObjectWriter {

    private static void serialize(StringBuilder sb, JsonValue jv) {
        switch (jv.getType()) {
            case NULL:
                sb.append("null");
                break;

            case STRING:
                sb.append("\"").append(jv.getString()).append("\"");
                break;

            case NUMBER:
                sb.append(jv.getNumberAsString());
                break;

            case BOOLEAN:
                sb.append(jv.getBoolean());
                break;

            case ARRAY:
                sb.append("[")
                        .append(Arrays.stream(jv.getArray())
                                .map(JsonObjectWriter::toString)
                                .collect(Collectors.joining(",")))
                        .append("]");
                break;

            case OBJECT:
                serialize(sb, jv.getObject());
                break;

            default:
                throw new RuntimeException("Unknown JsonValueType: " + jv.getType());
        }
    }

    public static String toString(JsonValue jv) {
        StringBuilder sb = new StringBuilder();
        serialize(sb, jv);
        return sb.toString();
    }

    private static void serialize(StringBuilder sb, JsonObject o) {
        sb.append("{");

        boolean first = true;
        for (String fieldName : o) {
            if (!first) {
                sb.append(",");
            } else {
                first = false;
            }
            sb.append("\"").append(fieldName).append("\"");
            sb.append(":");
            serialize(sb, o.getValue(fieldName));
        }

        sb.append("}");
    }

    public static String serialize(JsonObject o) {
        Objects.requireNonNull(o);

        StringBuilder sb = new StringBuilder();
        serialize(sb, o);

        return sb.toString();
    }
}
