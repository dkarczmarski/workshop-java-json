package my.workshop.test.util;

import my.workshop.json.example.Example1Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFileUtil {

    public static String getResourceAsString(String path) {
        byte[] bytes;
        try {
            URI uri = Example1Test.class.getClassLoader().getResource(path).toURI();
            bytes = Files.readAllBytes(Paths.get(uri));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
        return new String(bytes);
    }

    public static InputStream getResourceAsInputSteam(String path) {
        InputStream is = Example1Test.class.getClassLoader().getResourceAsStream(path);

        return is;
    }

}
