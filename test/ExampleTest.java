import org.junit.jupiter.api.Test;
import processing.ReadFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleTest {
    private static final String rootPath = Paths.get(".").toString();
    private static final String examplePath = Paths.get(rootPath,"example").toString();
    private static final String outPath = Paths.get(rootPath, "test_out").toString();

    private String getExpectedFilePath(String filePath) {
        return filePath.replace("test_out/", "")
                .replace(".cpp", "-expected.cpp")
                .replace(".c++", "-expected.c++")
                .replace(".cc", "-expected.cc");
    }

    private void compareExpected() {
        for (File file : Objects.requireNonNull(new File(Paths.get(outPath, "example").toString()).listFiles())) {
            try {
                List<String> actual = Files.readAllLines(file.toPath());
                List<String> expected = Files.readAllLines(Paths.get(getExpectedFilePath(file.toString())));

                assertEquals(expected, actual);
            } catch (Exception e) {
                fail();
            }
        }
    }

    @Test
    public void TestExampleResult() {
        try {
            ReadFile.read(new String[]{examplePath, "-o", outPath, "--debug"});
        } catch (IOException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }

        compareExpected();
    }
}
