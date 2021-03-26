package my.workshop.test.util;

import my.workshop.json.example.Example1Test;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TestFileUtil {

    public static Stream<String> readLines(Path path) {
        Stream<String> stream;
        try {
            stream = Files.lines(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stream;
    }

    public static String readToString(Path path) {
        return readLines(path).collect(Collectors.joining("\n"));
    }

    public static URL getResourceURL(String path) {
        URL url = Example1Test.class.getClassLoader().getResource(path);
        return url;
    }

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

}
