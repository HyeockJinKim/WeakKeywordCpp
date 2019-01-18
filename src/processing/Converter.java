package processing;

import checker.classinfo.ClassVisitor;
import checker.cast.StaticCastVisitor;
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
    private Set<CppClass> classSet;
    private String filePath;
    private boolean hasClass;
    private boolean hasStaticCast;
    private HashSet<String> moduleSet;

    private static final String logDir = "log";

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
            if (new File(getClassInfoFilePath(module)).exists())
                addClassInfo(module);
            else {
                // TODO parseClass require its module's class information
                parseClass(Paths.get(ReadFile.getBaseDir(), module).toString());

            }
        }
    }

    /**
     * Parse file for saving class information
     * @param filePath File path for parsing
     */
    private void parseClass(String filePath) {
        try {
            CPP14Lexer lexer = new CPP14Lexer(CharStreams.fromFileName(filePath));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();

            ClassVisitor visitor = new ClassVisitor(tokens, classSet);
            visitor.visit(tree);
            System.out.println(visitor.getFullText());
            classSet.addAll(visitor.getClassSet());
        } catch (IOException e) {
            System.out.println("File Input is not correct");
        }
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
     * Convert .cc file if code has class or static_cast
     * If module wasn't parsed, parse it
     */
    boolean convert() {
        classSet = new HashSet<>();

        /* Check hasClass, hasStaticCast */
        checkFile(filePath);
        checkModule();

        if (hasClass) {
            parseClass(filePath);
//            String json = ProcessJson.jsonifyClassSet(classSet);
//            writeClassInfo(filePath, json);
        }
        if (hasStaticCast) {
            Optional<String> result = parseStaticCast(filePath);
            result.ifPresent(this::rewriteCppFile);
        }

        return hasStaticCast;
    }

    /**
     * Add class's information to classSet
     * @param module Module to get class information
     */
    private void addClassInfo(String module) {
        String classInfoFile = getClassInfoFilePath(module);
        try (BufferedReader br = new BufferedReader(new FileReader(classInfoFile))) {
//            classSet.addAll(ProcessJson.readJson(br.readLine()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get class's information file's path
     * @param filePath File path to get class information
     * @return Class information file's path
     */
    private String getClassInfoFilePath(String filePath) {
        return Paths.get(ReadFile.getBaseDir(), logDir, filePath+".info").toString();
    }

    /**
     * Write class's information as JSON
     * @param filePath FilePath for logging
     * @param classInfo Class information stored in JSON
     */
    private void writeClassInfo(String filePath, String classInfo) {
        new File(getClassInfoFilePath(filePath)).getParentFile().mkdirs();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(getClassInfoFilePath(filePath)))) {
            bw.write(classInfo);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Rewrite Cpp file to make safe in v-table
     * @param file .cc file
     */
    private void rewriteCppFile(String file) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(file);
            System.out.println(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
