package processing.util;

import checker.cast.StaticCastVisitor;
import checker.classinfo.ClassVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import weakclass.CppClass;

import java.io.IOException;
import java.util.LinkedHashSet;
import java.util.Optional;

public class Parsing {
    private static Parsing ourInstance = new Parsing();
    public static Parsing getInstance() {
        return ourInstance;
    }

    /**
     * Parse file for saving class information
     * @param filePath File path for parsing
     */
    public static Optional<String> parseClass(String filePath, LinkedHashSet<CppClass> classSet) {
        try {
            CPP14Lexer lexer = new CPP14Lexer(CharStreams.fromFileName(filePath));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            CPP14Parser parser = new CPP14Parser(tokens);
            ParserRuleContext tree = parser.translationunit();

            ClassVisitor visitor = new ClassVisitor(tokens, classSet);
            visitor.visit(tree);

            if (classSet != null)
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
    public static Optional<String> parseStaticCast(String filePath, LinkedHashSet<CppClass> classSet) {
        try {
            CPP14Lexer lexer = new CPP14Lexer(CharStreams.fromFileName(IO.getOutPath(filePath)));
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

}
