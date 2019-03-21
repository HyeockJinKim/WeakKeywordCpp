import org.junit.jupiter.api.Test;
import processing.ReadFile;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.fail;

public class ExampleCompileBenchTest {
    private static final String rootPath = Paths.get(".").toString();
    private static final String examplePath = Paths.get(rootPath,"example").toString();
    private static final String outPath = Paths.get(rootPath, "test_out").toString();
    private static final String infoPath = Paths.get(rootPath, "test_out", "info").toString();

    private void TestResult(String filename) {
        try {
            String filePath = Paths.get(examplePath, filename).toString();
            for (int i = 0; i < 1000; ++i)
                ReadFile.read(new String[]{filePath, "--basedir", examplePath, "-o", outPath, "--info", infoPath, "--debug"});
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
