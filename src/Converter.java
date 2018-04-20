
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
    private String filePath;
    private String folderPath;
    private String fileName;
    Converter(String[] paths) throws FileNotFoundException {
        setPath(paths[0]);
    }

    private void setPath(String paths) throws FileNotFoundException {
        Path path = Paths.get(paths);
        if (paths.contains("/")) {
            folderPath = path.getParent().toString();
        } else {
            folderPath = Paths.get(".").toString();
        }
        fileName = path.getFileName().toString();
        if (!"cpp".equals(fileName.substring(fileName.length()-3, fileName.length()))) {
            throw new FileNotFoundException();
        }
        filePath = path.toString();
    }

    public Optional<String> parse() {
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

    public void writeCppFile(String text) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(Paths.get(folderPath, "_"+fileName).toString()));
            bufferedWriter.write(text);
            bufferedWriter.close();
        } catch (IOException e) {
            System.out.println("Write failed");
        }
    }
}
