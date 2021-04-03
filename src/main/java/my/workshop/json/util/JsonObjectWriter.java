package my.workshop.json.util;

import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Objects;

public class JsonObjectWriter {

    private static void write(JsonValue[] a, Writer w) throws IOException {
        w.append("[");
        for (int i = 0, n = a.length; i < n; ++i) {
            if (i > 0) {
                w.write(",");
            }
            write(a[i], w);
        }
        w.append("]");
    }

    private static void write(JsonValue jv, Writer w) throws IOException {
        switch (jv.getType()) {
            case NULL:
                w.append("null");
                break;

            case STRING:
                w.append("\"").append(jv.getString()).append("\"");
                break;

            case NUMBER:
                w.append(jv.getNumberAsString());
                break;

            case BOOLEAN:
                w.append(String.valueOf(jv.getBoolean()));
                break;

            case ARRAY:
                write(jv.getArray(), w);
                break;

            case OBJECT:
                write(jv.getObject(), w);
                break;

            default:
                throw new RuntimeException("Unknown JsonValueType: " + jv.getType());
        }
    }

    public static void write(JsonObject jo, Writer w) throws IOException {
        w.append("{");

        boolean first = true;
        for (String fieldName : jo) {
            if (!first) {
                w.append(",");
            } else {
                first = false;
            }
            w.append("\"").append(fieldName).append("\"");
            w.append(":");
            write(jo.getValue(fieldName), w);
        }

        w.append("}");
    }

    public static String serialize(JsonObject o) {
        Objects.requireNonNull(o);

        Writer w = new StringWriter();
        try {
            write(o, w);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return w.toString();
    }

}
