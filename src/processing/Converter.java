package processing;

import checker.WeakCheckVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.*;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;


public class Converter {
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

    private static int countLine(String filename) throws IOException {
        int count = 0;
        try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(filename))) {
            byte[] c = new byte[1024];
            int charNum;
            while ((charNum = br.read(c)) != -1) {
                for (int i = 0; i < charNum; ++i) {
                    if (c[i] == '\n')
                        ++count;
                }
            }
        }
        return count;
    }

    public static Optional<String> parse(String filePath) {
        try (FileInputStream fileInputStream = new FileInputStream(filePath)){
            int fileLine = countLine(filePath);
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

    public static void writeCppFile(String filePath, String text) {
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
