package processing;

import checker.WeakCheckVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;


public class Converter {
    private HashSet<String> moduleSet = new HashSet<>();
    private String filePath;

    Converter(String filePath) {
        this.filePath = filePath;
    }

    private static String getFolderPath(String filePath) throws FileNotFoundException {
        Path path = Paths.get(filePath);
        String folderPath;
        if (filePath.contains("/")) {
            folderPath = path.getParent().toString();
        } else {
            folderPath = Paths.get(".").toString();
        }
        return folderPath;
    }

    private int checkFile(String filename) throws IOException {
        int count = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                ++count;
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
        }
        return count;
    }

    Optional<String> parse() {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)){
            int fileLine = checkFile(filePath);
            if (fileLine > 30000) {
                return Optional.empty();
            }
            ANTLRInputStream inputStream = new ANTLRInputStream(fileInputStream);
            CPP14Lexer lexer = new CPP14Lexer(inputStream);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();
            WeakCheckVisitor visitor = new WeakCheckVisitor(tokens);
            visitor.visit(tree);
//            System.out.println(visitor.getFullText());
            return Optional.ofNullable(visitor.getFullText());
        } catch (IOException e) {
            System.out.println("File Input is not correct");
        }
        return Optional.empty();
    }

    public void writeCppFile(String text) {
        try {
            String folderPath = getFolderPath(filePath);
            String fileName = Paths.get(filePath).getFileName().toString();
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Paths.get(folderPath, "_"+fileName).toString()));
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Write failed");
        }
    }
}
