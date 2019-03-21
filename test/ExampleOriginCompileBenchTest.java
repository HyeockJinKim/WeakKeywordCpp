import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExampleOriginCompileBenchTest {

    private static void compileGpp(String cpp, String run) {
        Utils.execCommand("g++ -o ./test_out/exec/"+run+" "+cpp);
    }

    private static void compileAll(String filename) {
        compileGpp("./example/"+filename+".cpp", filename+"_origin");
    }
    private static void TestOriginPerformance(String file) {
        for (int i = 0; i < 1000; ++i)
            compileAll(file);
    }

    @Test
    void TestBaseOriginPerformance() {
        TestOriginPerformance("base");
    }

    @Test
    void TestAccessSpecifierOriginPerformance() {
        TestOriginPerformance("access_specifier_base");
    }

    @Test
    void TestConstructorOriginPerformance() {
        TestOriginPerformance("constructor");
    }

    @Test
    void TestDiamondOriginPerformance() {
        TestOriginPerformance("diamond");
    }

    @Test
    void TestExternalOriginPerformance() {
        TestOriginPerformance("external_definition");
    }

    @Test
    void TestIncludeOriginPerformance() {
        TestOriginPerformance("include");
    }


    @Test
    void TestNamespaceOriginPerformance() {
        TestOriginPerformance("namespace");
    }

    @Test
    void TestNonWeakOriginPerformance() {
        TestOriginPerformance("nonweak");
    }

    @Test
    void TestParamsOriginPerformance() {
        TestOriginPerformance("params");
    }

    @Test
    void TestStaticOriginPerformance() {
        TestOriginPerformance("static_base");
    }

    @Test
    void TestVirtualOriginPerformance() {
        TestOriginPerformance("virtual_nonweak");
    }
}
