package my.workshop.json;

import my.workshop.json.example.Example1Test;
import my.workshop.json.util.JsonObjectReaderTest;
import my.workshop.json.util.JsonObjectWriterTest;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.platform.suite.api.SelectClasses;
import org.junit.runner.RunWith;

@RunWith(JUnitPlatform.class)
@SelectClasses({
        JsonValueTest.class,
        JsonObjectTest.class,
        JsonObjectReaderTest.class,
        JsonObjectWriterTest.class,
        Example1Test.class
})
public class AllUnitTest {
}
