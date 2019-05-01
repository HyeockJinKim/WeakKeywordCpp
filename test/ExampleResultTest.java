import org.junit.jupiter.api.Test;
import processing.ReadFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ExampleResultTest {
    private static final String rootPath = Paths.get(".").toString();
    private static final String examplePath = Paths.get(rootPath,"example").toString();
    private static final String outPath = Paths.get(rootPath, "test_out", "example").toString();
    private static final String infoPath = Paths.get(rootPath, "test_out", "info").toString();

    private static String getExpectedFilePath(String filePath) {
        return filePath.replace(".c", "-expected.c")
                .replace(".md", "-expected.md")
                .replace(".h", "-expected.h");
    }

    private void TestResult(String filename) {
        try {
            String filePath = Paths.get(examplePath, filename).toString();
            ReadFile.read(new String[]{filePath, "--basedir", examplePath, "-o", outPath, "--info", infoPath, "--debug"});
            try {
                File file = new File(Paths.get(outPath, filename).toString());
                List<String> actual = Files.readAllLines(file.toPath());
                List<String> expected = Files.readAllLines(Paths.get(getExpectedFilePath(filePath)));

                assertEquals(expected, actual);
            } catch (Exception e) {
                e.printStackTrace();
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
        TestResult("base.h");
    }

    @Test
    void TestConstructorResult() {
        TestResult("constructor.cpp");
    }

    @Test
    void TestConstructorInheritanceResult() {
        TestResult("constructor_inheritance.cpp");
    }

    @Test
    void TestConstructorParameterResult() {
        TestResult("constructor_parameter.cpp");
    }

    @Test
    void TestDestructorResult() {
        TestResult("destructor.cpp");
    }

    @Test
    void TestDestructorBaseResult() {
        TestResult("destructor_base.cpp");
    }

    @Test
    void TestDiamondResult() {
        TestResult("diamond.cpp");
    }

    @Test
    void TestExplicitResult() {
        TestResult("explicit.cpp");
    }

    @Test
    void TestExternalDefinitionResult() {
        TestResult("external_definition.cpp");
    }


    @Test
    void TestExternalDefinitionBaseResult() {
        TestResult("external_definition_base.cpp");
    }

    @Test
    void TestExternalPublicDefinitionBaseResult() {
        TestResult("external_public_definition.cpp");
    }

//    @Test
//    void TestFeildResult() {
//        TestResult("field.cpp");
//    }

    @Test
    void TestIncludeResult() {
        TestResult("include.cpp");
    }

    @Test
    void TestIncludeExternalDefinitionResult() {
        TestResult("include_external_definition.cpp");
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
    void TestNoTargetResult() {
        TestResult("no-target.c");
    }

    @Test
    void TestParamResult() {
        TestResult("params.cpp");
    }

    @Test
    void TestREADMEResult() {
        TestResult("README.md");
    }

    @Test
    void TestStaticResult() {
        TestResult("static_base.cpp");
    }

    @Test
    void TestStaticProtectedResult() {
        TestResult("static_protected.h");
    }

    @Test
    void TestVirtualNonWeakResult() {
        TestResult("virtual_nonweak.cpp");
    }
}
