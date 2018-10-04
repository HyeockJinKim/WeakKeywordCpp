package processing;

import JSON.JSONArray;
import JSON.JSONDictionary;
import JSON.JSONObject;
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

    Converter(String filePath) {
        this.filePath = filePath;
    }

    private boolean checkFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            moduleSet = new HashSet<>();
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
        return true;
    }

    private void moduleCheck() {
        for (String module : moduleSet) {
            parseClass(Paths.get(ReadFile.getBaseDir(), module).toString());
        }
    }

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

    void convert() {
        classSet = new HashSet<>();
        if (checkFile(filePath)) {
            if (moduleSet.size() > 0)
                moduleCheck();

            Optional<String> result = parseClass(filePath);
            String json = ProcessJson.jsonifyClassSet(classSet);
            result.ifPresent(x -> writeCppFile(x, json));

            if (hasStaticCast) {
                result = parseStaticCast(filePath);
                result.ifPresent(x -> writeCppFile(x, ""));
            }
        }
    }

    private void writeCppFile(String file, String tag) {
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
