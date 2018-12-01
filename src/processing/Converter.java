package processing;

import checker.ClassVisitor;
import checker.StaticCastVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.*;
import weakclass.CppClass;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class Converter {
    private HashSet<String> moduleSet;
    private String filePath;
    private boolean hasClass;
    private boolean hasStaticCast;
    private Set<CppClass> classSet;

    /**
     * Constructor for converter
     * @param filePath File path for set path
     */
    Converter(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Check if the file contains a keyword.
     * @param filename Filename for check
     */
    private void checkFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            moduleSet = new HashSet<>();
            while ((line = br.readLine()) != null) {
                checkInclude(line);
                checkClass(line);
                checkStaticCast(line); // 3 step
            } // pre-header 를 썼을 때 성능 개선?
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * If "#include" is included in line, add that path to module set
     * @param line Line in .cc file
     */
    private void checkInclude(String line) {
        if (line.contains("#include")) {
            if (line.contains("\"")) {
                String temp = line.split("\"")[1];
                String path = Paths.get(ReadFile.getBaseDir(), temp).toString();
                File file = new File(path);
                if (file.isFile()) {
                    moduleSet.add(line.split("\"")[1]);
                }
            }
        }
    }

    /**
     * Check "class" is included in line
     * @param line Line in .cc file
     */
    private void checkClass(String line) {
        if (line.contains("class")) {
            hasClass = true;
        }
    }

    /**
     * Check "static_cast" is included in line
     * @param line Line in .cc file
     */
    private void checkStaticCast(String line) {
        if (line.contains("static_cast")) {
            hasStaticCast = true;
        }
    }

    /**
     * Check module in .cc file
     */
    private void checkModule() {
        for (String module : moduleSet) {
            parseClass(Paths.get(ReadFile.getBaseDir(), module).toString());
        }
    }

    /**
     * FIXME parseClass does not require return value
     * Parse file for saving class information
     * @param filePath File path for parsing
     * @return Parsed C++ code
     */
    private Optional<String> parseClass(String filePath) {
        try {
            CPP14Lexer lexer = new CPP14Lexer(CharStreams.fromFileName(filePath));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();

            ClassVisitor visitor = new ClassVisitor(tokens, classSet);
            visitor.visit(tree);
            classSet.addAll(visitor.getClassSet());
            return Optional.ofNullable(visitor.getFullText());
        } catch (IOException e) {
            System.out.println("File Input is not correct");
        }
        return Optional.empty();
    }

    /**
     * Parse file for converting static_cast
     * @param filePath File path for parsing
     * @return Converted C++ code
     */
    private Optional<String> parseStaticCast(String filePath) {
        try {
            CPP14Lexer lexer = new CPP14Lexer(CharStreams.fromFileName(filePath));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();

            StaticCastVisitor visitor = new StaticCastVisitor(tokens, classSet);
            visitor.visit(tree);

            return Optional.ofNullable(visitor.getFullText());
        } catch (IOException e) {
            System.out.println("File Input is not correct");
        }

        return Optional.empty();
    }

    /**
     * FIXME parseClass doesn't require rewriteCppFile, but write log in Log directory
     * Convert .cc file if code has class or static_cast
     * If module wasn't parsed, parse it
     */
    void convert() {
        classSet = new HashSet<>();

        /* Check hasClass, hasStaticCast */
        checkFile(filePath);
        checkModule();

        Optional<String> result;
        if (hasClass) {
            result = parseClass(filePath);
            String json = ProcessJson.jsonifyClassSet(classSet);
            result.ifPresent(x -> rewriteCppFile(x, json));
        }
        if (hasStaticCast) {
            result = parseStaticCast(filePath);
            result.ifPresent(x -> rewriteCppFile(x, ""));
        }

    }

    /**
     * Rewrite Cpp file to make safe in v-table
     * @param file .cc file
     * @param tag FIXME Remove this
     */
    private void rewriteCppFile(String file, String tag) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(tag);
            bw.newLine();
            bw.write(file);
            System.out.println(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
