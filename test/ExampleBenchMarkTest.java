import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExampleBenchMarkTest {

    private static String execCommand(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec(cmd);
            BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));

            String line;

            StringBuffer bf = new StringBuffer();
            while ((line = br.readLine()) != null) {
                bf.append(line);
                bf.append("\n");
            }
            return bf.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void compileGpp(String cpp, String run) {
        execCommand("g++ -o ./test_out/exec/"+run+" "+cpp);
    }

    private static void runCpp(String run) {
        for (int i = 0; i < 10000; ++i)
            execCommand("./test_out/exec/"+run);
    }

    private static void compileAll(String filename) {
        compileGpp("./example/"+filename+".cpp", filename+"_origin");
        compileGpp("./test_out/example/"+filename+".cpp", filename+"_change");
    }

//    @Test
//    void compileAllFile() {
//        new File("./test_out/exec").mkdirs();
//        compileAll("access_specifier_base");
//        compileAll("base");
//        compileAll("constructor");
//        compileAll("diamond");
//        compileAll("external_definition");
//        compileAll("include");
//        compileAll("namespace");
//        compileAll("nonweak");
//        compileAll("params");
//        compileAll("static_base");
//        compileAll("virtual_nonweak");
//    }

    private static void TestOriginPerformance(String file) {
        runCpp(file+"_origin");
    }

    private static void TestChangePerformance(String file) {
        runCpp(file+"_change");
    }

//    @Test
//    void TestBaseOriginPerformance() {
//        TestOriginPerformance("base");
//    }
//
//    @Test
//    void TestBaseChangePerformance() {
//        TestChangePerformance("base");
//    }

    @Test
    void TestAccessSpecifierOriginPerformance() {
        TestOriginPerformance("access_specifier_base");
    }

    @Test
    void TestAccessSpecifierChangePerformance() {
        TestChangePerformance("access_specifier_base");
    }

    @Test
    void TestConstructorOriginPerformance() {
        TestOriginPerformance("constructor");
    }

    @Test
    void TestConstructorChangePerformance() {
        TestChangePerformance("constructor");
    }

    @Test
    void TestDiamondOriginPerformance() {
        TestOriginPerformance("diamond");
    }

    @Test
    void TestDiamondChangePerformance() {
        TestChangePerformance("diamond");
    }

    @Test
    void TestExternalOriginPerformance() {
        TestOriginPerformance("external_definition");
    }

    @Test
    void TestExternalChangePerformance() {
        TestChangePerformance("external_definition");
    }

    @Test
    void TestIncludeOriginPerformance() {
        TestOriginPerformance("include");
    }

    @Test
    void TestIncludeChangePerformance() {
        TestChangePerformance("include");
    }

    @Test
    void TestNamespaceOriginPerformance() {
        TestOriginPerformance("namespace");
    }

    @Test
    void TestNamespaceChangePerformance() {
        TestChangePerformance("namespace");
    }

    @Test
    void TestNonWeakOriginPerformance() {
        TestOriginPerformance("nonweak");
    }

    @Test
    void TestNonWeakChangePerformance() {
        TestChangePerformance("nonweak");
    }

    @Test
    void TestParamsOriginPerformance() {
        TestOriginPerformance("params");
    }

    @Test
    void TestParamsChangePerformance() {
        TestChangePerformance("params");
    }

    @Test
    void TestStaticOriginPerformance() {
        TestOriginPerformance("static_base");
    }

    @Test
    void TestStaticChangePerformance() {
        TestChangePerformance("static_base");
    }

    @Test
    void TestVirtualOriginPerformance() {
        TestOriginPerformance("virtual_nonweak");
    }

    @Test
    void TestVirtualChangePerformance() {
        TestChangePerformance("virtual_nonweak");
    }
}
