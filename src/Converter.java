
import checker.WeakCheckVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.*;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
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

    public static Optional<String> parse(String filePath) {
        try {
            CPP14Lexer lexer = new CPP14Lexer(new ANTLRFileStream(filePath));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();
            WeakCheckVisitor visitor = new WeakCheckVisitor(tokens);
            visitor.visit(tree);
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
