package processing;

import checker.ClassVisitor;
import checker.StaticCastVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.*;
import weakclass.CppClass;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class Converter {
    private HashSet<String> moduleSet = new HashSet<>();
    private String filePath;
    private boolean hasClass;
    private boolean hasStaticCast;
    private Set<CppClass> classSet;

    Converter(String filePath) {
        this.filePath = filePath;
    }

    private void checkFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
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
                if (line.contains("class")) {
                    hasClass = true;
                }
                if (line.contains("static_cast")) {
                    hasStaticCast = true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Optional<String> parseClass() {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ANTLRInputStream inputStream = new ANTLRInputStream(fileInputStream);
            CPP14Lexer lexer = new CPP14Lexer(inputStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();

            ClassVisitor visitor = new ClassVisitor(tokens);
            visitor.visit(tree);
            classSet = visitor.getClassSet();

            return Optional.ofNullable(visitor.getFullText());
        } catch (IOException e) {
            System.out.println("File Input is not correct");
        }
        return Optional.empty();
    }

    private Optional<String> parseStaticCast() {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)) {
            ANTLRInputStream inputStream = new ANTLRInputStream(fileInputStream);
            CPP14Lexer lexer = new CPP14Lexer(inputStream);
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

    void convert() {
        classSet = new HashSet<>();
        checkFile(filePath);

        if (hasClass) {
            Optional<String> result = parseClass();
            result.ifPresent(this::writeCppFile);
        }

        if (hasStaticCast) {
            Optional<String> result = parseStaticCast();
            result.ifPresent(this::writeCppFile);
        }
    }

    private void writeCppFile(String file) {
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(filePath))) {
            bw.write(file);
            System.out.println(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
