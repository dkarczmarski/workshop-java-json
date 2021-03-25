package my.workshop.json.example;

import my.workshop.json.JsonObject;
import my.workshop.json.util.JsonObjectReader;
import org.junit.jupiter.api.Test;

import static my.workshop.test.util.TestFileUtil.getResourceAsString;

public class Example1Test {

    @Test
    void test1() {
        String s = getResourceAsString("example1.json");

        JsonObject jo = JsonObjectReader.read(s);

        jo.getValue("field1").getNumberAsInt();
        jo.getNumberAsInt("field1");
    }

}
