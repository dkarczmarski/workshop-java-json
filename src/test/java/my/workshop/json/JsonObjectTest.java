package my.workshop.json;

class JsonObjectTest extends AbstractJsonObjectTest {

    @Override
    JsonObject newJsonObject() {
        return JsonFactory.createJsonObject();
    }
}