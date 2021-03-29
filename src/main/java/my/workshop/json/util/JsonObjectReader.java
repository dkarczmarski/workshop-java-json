package my.workshop.json.util;

import my.workshop.json.JsonDataException;
import my.workshop.json.JsonFactory;
import my.workshop.json.JsonObject;
import my.workshop.json.JsonValue;

import java.util.ArrayList;
import java.util.List;

public class JsonObjectReader {

    static class JsonIterator {
        final String s;
        final int n;
        int i;

        JsonIterator(String s) {
            this.s = s;
            this.n = s.length();
            this.i = -1;
        }

        int skipWhitespace() {
            int bi = i;
            if (n > 0) {
                char ch;
                while (i < (n - 1)) {
                    ch = s.charAt(i + 1);
                    if (!Character.isWhitespace(ch)) {
                        break;
                    }
                    i++;
                }
            }
            return i - bi;
        }

        public boolean eof() {
            return (n - 1) == i;
        }

        char getChar(boolean skipWhitespace, boolean peakMode) {
            if (skipWhitespace) {
                skipWhitespace();
            }
            if (eof()) {
                throw new JsonDataException("EOF");
            }

            char ch = s.charAt(i + 1);
            if (!peakMode) {
                i++;
            }

            return ch;
        }

        public char peekChar(boolean skipWhitespace) {
            return getChar(skipWhitespace, true);
        }

        public char peekChar() {
            return peekChar(false);
        }

        public char nextChar(boolean skipWhitespace) {
            return getChar(skipWhitespace, false);
        }

        public char nextChar() {
            return nextChar(false);
        }

        public String getData() {
            return s;
        }

        public int getDataIndex() {
            return i;
        }
    }

    static String iteratorInfo(JsonIterator it) {
        int i = it.getDataIndex();
        return String.format("char= %c, index= %d", it.getData().charAt(i), i);
    }

    static boolean checkChar(char ch, String domain) {
        return domain.contains(String.valueOf(ch));
    }

    static void checkChar(char ch, String domain, JsonIterator it) {
        if (!checkChar(ch, domain)) {
            throw new JsonDataException(iteratorInfo(it));
        }
    }

    static void checkString(JsonIterator it, String expected) {
        for (int i = 0, n = expected.length(); i < n; ++i) {
            if (it.nextChar() != expected.charAt(i)) {
                throw new JsonDataException(iteratorInfo(it));
            }
        }
    }

    static String readQuotedString(JsonIterator it) {
        checkChar(it.nextChar(true), "\"", it);

        char ch;
        int beginIndex = -1;
        while (true) {
            ch = it.nextChar();
            if (beginIndex == -1) {
                beginIndex = it.getDataIndex();
            }

            if (ch == '\"') {
                break;
            }
        }

        return it.getData().substring(beginIndex, it.getDataIndex());
    }

    static JsonValue readJsonString(JsonIterator it) {
        return JsonFactory.jsonString(readQuotedString(it));
    }

    static JsonValue readJsonNull(JsonIterator it) {
        checkString(it, "null");
        return JsonFactory.jsonNull();
    }

    static JsonValue readJsonTrue(JsonIterator it) {
        checkString(it, "true");
        return JsonFactory.jsonBoolean(true);
    }

    static JsonValue readJsonFalse(JsonIterator it) {
        checkString(it, "false");
        return JsonFactory.jsonBoolean(false);
    }

    static JsonValue readJsonNumber(JsonIterator it) {
        char ch;
        int beginIndex = -1;
        while (true) {
            ch = it.peekChar();
            if (!(ch == '-' || ch == '+' || ch == 'e' || ch == 'E' || ch == '.' || ('0' <= ch && ch <= '9'))) {
                break;
            }

            it.nextChar();
            if (beginIndex == -1) {
                beginIndex = it.getDataIndex();
            }
        }

        String s = it.getData().substring(beginIndex, it.getDataIndex() + 1);
        return JsonFactory.jsonNumber(s);
    }

    static JsonValue readJsonValue(JsonIterator it) {
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

    static JsonValue readJsonArray(JsonIterator it) {
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

    static JsonValue readJsonObject(JsonIterator it) {
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

    static JsonIterator jsonIterator(String s) {
        return new JsonIterator(s);
    }

    public static JsonObject read(String s) {
        JsonIterator it = jsonIterator(s);
        JsonValue jv = readJsonObject(it);

        it.skipWhitespace();
        if (!it.eof()) {
            throw new JsonDataException(iteratorInfo(it));
        }

        return jv.getObject();
    }

}
