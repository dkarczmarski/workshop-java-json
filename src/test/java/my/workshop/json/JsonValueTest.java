package my.workshop.json;

class JsonValueTest extends AbstractJsonValueTest {

    @Override
    JsonObject newJsonObject() {
        return JsonFactory.createJsonObject();
    }

    @Override
    JsonValue newJsonValue() {
        return JsonFactory.createJsonValue();
    }

}