import org.junit.jupiter.api.Test;

public class ExampleChangeCompileBenchTest {

    private static void compileGpp(String cpp, String run) {
        Utils.execCommand("g++ -o ./test_out/exec/"+run+" "+cpp);
    }

    private static void compileAll(String filename) {
        compileGpp("./test_out/example/"+filename+".cpp", filename+"_changed");
    }
    private static void TestChangedPerformance(String file) {
        for (int i = 0; i < 1000; ++i)
            compileAll(file);
    }

    @Test
    void TestBaseChangedPerformance() {
        TestChangedPerformance("base");
    }

    @Test
    void TestAccessSpecifierChangedPerformance() {
        TestChangedPerformance("access_specifier_base");
    }

    @Test
    void TestConstructorChangedPerformance() {
        TestChangedPerformance("constructor");
    }

    @Test
    void TestDiamondChangedPerformance() {
        TestChangedPerformance("diamond");
    }

    @Test
    void TestExternalChangedPerformance() {
        TestChangedPerformance("external_definition");
    }

    @Test
    void TestIncludeChangedPerformance() {
        TestChangedPerformance("include");
    }


    @Test
    void TestNamespaceChangedPerformance() {
        TestChangedPerformance("namespace");
    }

    @Test
    void TestNonWeakChangedPerformance() {
        TestChangedPerformance("nonweak");
    }

    @Test
    void TestParamsChangedPerformance() {
        TestChangedPerformance("params");
    }

    @Test
    void TestStaticChangedPerformance() {
        TestChangedPerformance("static_base");
    }

    @Test
    void TestVirtualChangedPerformance() {
        TestChangedPerformance("virtual_nonweak");
    }
}
