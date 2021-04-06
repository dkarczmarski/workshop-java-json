package my.workshop.json.util;

import my.workshop.json.JsonDataException;
import my.workshop.json.JsonFactory;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonObjectReader {

    private static class JsonReader {

        final PushbackReader r;
        int buff;
        int i;

        JsonReader(Reader r) {
            this.r = new PushbackReader(r);
            this.i = -1;
        }

        int read(boolean moveNext) throws IOException {
            buff = r.read();

            if (moveNext) {
                i++;
            } else {
                if (buff == -1) {
                    return -1;
                }

                r.unread(buff);
            }

            return buff;
        }

        boolean eof() {
            return buff == -1;
        }

        int getIndex() {
            return i;
        }

        int getBuff() {
            return buff;
        }
    }

    static class JsonIterator {

        final JsonReader r;

        JsonIterator(Reader r) {
            this.r = new JsonReader(r);
        }

        int skipWhitespace() throws IOException {
            int ch;
            while ((ch = r.read(false)) != -1) {
                if (!Character.isWhitespace(ch)) {
                    break;
                }
                r.read(true);
            }

            return ch;
        }

        boolean eof() {
            return r.eof();
        }

        char getChar(boolean skipWhitespace, boolean moveNext) throws IOException {
            if (skipWhitespace) {
                if (skipWhitespace() == -1) {
                    if (moveNext) {
                        throw new JsonDataException("EOF");
                    }

                    return '\0';
                }
            }

            return (char) r.read(moveNext);
        }

        char getBuffChar() {
            return (char) r.getBuff();
        }

        int getBuffIndex() {
            return r.getIndex();
        }
    }

    static String iteratorInfo(JsonIterator it) {
        int i = it.getBuffIndex();
        return String.format("char= %c, index= %d", it.getBuffChar(), i);
    }

    static boolean checkChar(char ch, String domain) {
        return domain.contains(String.valueOf(ch));
    }

    static void checkChar(char ch, String domain, JsonIterator it) {
        if (!checkChar(ch, domain)) {
            throw new JsonDataException(iteratorInfo(it));
        }
    }

    static void checkString(JsonIterator it, String expected) throws IOException {
        for (int i = 0, n = expected.length(); i < n; ++i) {
            if (it.getChar(false, true) != expected.charAt(i)) {
                throw new JsonDataException(iteratorInfo(it));
            }
        }
    }

    static String readQuotedString(JsonIterator it) throws IOException {
        checkChar(it.getChar(true, true), "\"", it);

        StringBuilder sb = new StringBuilder();
        char ch;
        while (true) {
            ch = it.getChar(false, true);
            if (ch == '\"') {
                break;
            }

            sb.append(ch);
        }

        return sb.toString();
    }

    static JsonValue readJsonString(JsonIterator it) throws IOException {
        return JsonFactory.jsonString(readQuotedString(it));
    }

    static JsonValue readJsonNull(JsonIterator it) throws IOException {
        checkString(it, "null");
        return JsonFactory.jsonNull();
    }

    static JsonValue readJsonTrue(JsonIterator it) throws IOException {
        checkString(it, "true");
        return JsonFactory.jsonBoolean(true);
    }

    static JsonValue readJsonFalse(JsonIterator it) throws IOException {
        checkString(it, "false");
        return JsonFactory.jsonBoolean(false);
    }

    static JsonValue readJsonNumber(JsonIterator it) throws IOException {
        StringBuilder sb = new StringBuilder();
        char ch;
        while (true) {
            ch = it.getChar(false, false);
            if (!(ch == '-' || ch == '+' || ch == 'e' || ch == 'E' || ch == '.' || ('0' <= ch && ch <= '9'))) {
                break;
            }

            sb.append(it.getChar(false, true));
        }

        return JsonFactory.jsonNumber(sb.toString());
    }

    static JsonValue readJsonValue(JsonIterator it) throws IOException {
        JsonValue result;
        char ch = it.getChar(true, false);

        if (ch == 'n') {
            result = readJsonNull(it);
        } else if (ch == 't') {
            result = readJsonTrue(it);
        } else if (ch == 'f') {
            result = readJsonFalse(it);
        } else if (ch == '\"') {
            result = readJsonString(it);
        } else if (ch == '-' || ('0' <= ch && ch <= '9')) {
            result = readJsonNumber(it);
        } else if (ch == '[') {
            result = readJsonArray(it);
        } else if (ch == '{') {
            result = readJsonObject(it);
        } else {
            throw new JsonDataException(iteratorInfo(it));
        }

        return result;
    }

    static JsonValue readJsonArray(JsonIterator it) throws IOException {
        checkChar(it.getChar(false, true), "[", it);

        List<JsonValue> args = new ArrayList<>();
        JsonValue jv;

        char ch;
        boolean firstRound = true;
        while (true) {
            ch = it.getChar(false, false);
            if (ch == ']') {
                break;
            }
            if (!firstRound && ch == ',') {
                it.getChar(false, true);
                continue;
            }

            jv = readJsonValue(it);
            args.add(jv);

            if (firstRound) {
                firstRound = false;
            }
        }

        checkChar(it.getChar(false, true), "]", it);

        return JsonFactory.jsonArray(args.toArray(new JsonValue[args.size()]));
    }

    static JsonValue readJsonObject(JsonIterator it) throws IOException {
        JsonObject o = JsonFactory.createJsonObject();

        checkChar(it.getChar(true, true), "{", it);

        boolean firstRound = true;
        char ch;
        String fieldName;
        JsonValue jsonValue;
        while (true) {
            ch = it.getChar(true, false);
            if (firstRound) {
                checkChar(ch, "\"}", it);
                firstRound = false;
            } else {
                checkChar(ch, "},", it);
            }

            if (ch == '}') {
                break;
            }
            if (ch == ',') {
                it.getChar(false, true);
            }

            fieldName = readQuotedString(it);
            checkChar(it.getChar(true, true), ":", it);
            jsonValue = readJsonValue(it);

            o.setField(fieldName, jsonValue);
        }

        checkChar(it.getChar(false, true), "}", it);

        return JsonFactory.jsonObject(o);
    }

    public static JsonObject read(String s) {
        try {
            return read(new StringReader(s));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static JsonObject read(Reader r) throws IOException {
        JsonIterator it = new JsonIterator(r);
        JsonValue jv = readJsonObject(it);

        it.skipWhitespace();
        if (!it.eof()) {
            throw new JsonDataException(iteratorInfo(it));
        }

        return jv.getObject();
    }

    public static JsonObject read(InputStream is, String inputCharsetName) throws IOException {
        InputStreamReader r = new InputStreamReader(is, inputCharsetName);

        return read(r);
    }

}
