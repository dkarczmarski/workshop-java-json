package my.workshop.json.util;

import my.workshop.json.JsonDataException;
import my.workshop.json.JsonFactory;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class JsonObjectReader {

    static class JsonIterator {
        final PushbackReader r;
        int i;
        int ch;

        JsonIterator(Reader r) {
            this.r = new PushbackReader(r);
            this.i = -1;
        }

        int skipWhitespace() throws IOException {
            int bi = i;

            while ((ch = r.read()) != -1) {
                if (!Character.isWhitespace(ch)) {
                    r.unread(ch);
                    break;
                }
                i++;
            }

            return i - bi;
        }

        public boolean eof() {
            return ch == -1;
        }

        char getChar(boolean skipWhitespace, boolean peakMode) throws IOException {
            if (skipWhitespace) {
                skipWhitespace();
            }
            if (eof()) {
                throw new JsonDataException("EOF");
            }

            ch = r.read();
            if (peakMode) {
                r.unread(ch);
            } else {
                i++;
            }

            return (char) ch;
        }

        public char peekChar(boolean skipWhitespace) throws IOException {
            return getChar(skipWhitespace, true);
        }

        public char peekChar() throws IOException {
            return peekChar(false);
        }

        public char nextChar(boolean skipWhitespace) throws IOException {
            return getChar(skipWhitespace, false);
        }

        public char nextChar() throws IOException {
            return nextChar(false);
        }

        public char getBuffChar() {
            if (i == -1) {
                throw new IllegalStateException();
            }
            return (char) ch;
        }

        public int getBuffIndex() {
            return i;
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
            if (it.nextChar() != expected.charAt(i)) {
                throw new JsonDataException(iteratorInfo(it));
            }
        }
    }

    static String readQuotedString(JsonIterator it) throws IOException {
        checkChar(it.nextChar(true), "\"", it);

        StringBuilder sb = new StringBuilder();
        char ch;
        while (true) {
            ch = it.nextChar();

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
            ch = it.peekChar();
            if (!(ch == '-' || ch == '+' || ch == 'e' || ch == 'E' || ch == '.' || ('0' <= ch && ch <= '9'))) {
                break;
            }

            sb.append(it.nextChar());
        }

        return JsonFactory.jsonNumber(sb.toString());
    }

    static JsonValue readJsonValue(JsonIterator it) throws IOException {
        JsonValue result;
        char ch = it.peekChar(true);

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
        checkChar(it.nextChar(), "[", it);

        List<JsonValue> args = new ArrayList<>();
        JsonValue jv;

        char ch;
        boolean firstRound = true;
        while (true) {
            ch = it.peekChar();
            if (ch == ']') {
                break;
            }
            if (!firstRound && ch == ',') {
                it.nextChar();
                continue;
            }

            jv = readJsonValue(it);
            args.add(jv);

            if (firstRound) {
                firstRound = false;
            }
        }

        checkChar(it.nextChar(), "]", it);

        return JsonFactory.jsonArray(args.toArray(new JsonValue[args.size()]));
    }

    static JsonValue readJsonObject(JsonIterator it) throws IOException {
        JsonObject o = JsonFactory.createJsonObject();

        checkChar(it.nextChar(true), "{", it);

        boolean firstRound = true;
        char ch;
        String fieldName;
        JsonValue jsonValue;
        while (true) {
            ch = it.peekChar(true);
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
                it.nextChar();
            }

            fieldName = readQuotedString(it);
            checkChar(it.nextChar(true), ":", it);
            jsonValue = readJsonValue(it);

            o.setField(fieldName, jsonValue);
        }

        checkChar(it.nextChar(), "}", it);

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

}
