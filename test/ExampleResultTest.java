import org.junit.jupiter.api.Test;
import processing.ReadFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleResultTest {
    private static final String rootPath = Paths.get(".").toString();
    private static final String examplePath = Paths.get(rootPath,"example").toString();
    private static final String outPath = Paths.get(rootPath, "test_out").toString();
    private static final String infoPath = Paths.get(rootPath, "test_out", "info").toString();

    public static String getExpectedFilePath(String filePath) {
        return filePath.replace("test_out/", "")
                .replace(".cpp", "-expected.cpp")
                .replace(".c++", "-expected.c++")
                .replace(".cc", "-expected.cc");
    }

    private void TestResult(String filename) {
        try {
            String filePath = Paths.get(examplePath, filename).toString();
            ReadFile.read(new String[]{filePath, "--basedir", examplePath, "-o", outPath, "--info", infoPath, "--debug"});
            try {
                File file = new File(Paths.get(outPath, "example", filename).toString());
                List<String> actual = Files.readAllLines(file.toPath());
                List<String> expected = Files.readAllLines(Paths.get(getExpectedFilePath(file.toString())));

                assertEquals(expected, actual);
            } catch (Exception e) {
                fail();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    void TestAccessSpecifierResult() {
        TestResult("access_specifier_base.cpp");
    }

    @Test
    void TestBaseResult() {
        TestResult("base.cpp");
    }

    @Test
    void TestConstructorResult() {
        TestResult("constructor.cpp");
    }

    @Test
    void TestDiamonResult() {
        TestResult("diamond.cpp");
    }

    @Test
    void TestExternalDefinitionResult() {
        TestResult("external_definition.cpp");
    }

    @Test
    void TestIncludeResult() {
        TestResult("include.cpp");
    }
    @Test
    void TestNamespaceResult() {
        TestResult("namespace.cpp");
    }

    @Test
    void TestNonWeakResult() {
        TestResult("nonweak.cpp");
    }

    @Test
    void TestParamResult() {
        TestResult("params.cpp");
    }

    @Test
    void TestStaticResult() {
        TestResult("static_base.cpp");
    }

    @Test
    void TestVirtualNonWeakResult() {
        TestResult("virtual_nonweak.cpp");
    }
}
