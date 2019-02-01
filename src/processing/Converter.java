package processing;

import checker.classinfo.ClassVisitor;
import checker.cast.StaticCastVisitor;
import grammar.antlr.CPP14Lexer;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.*;
import processing.util.Checker;
import processing.util.IO;
import processing.util.Parsing;
import weakclass.CppClass;

import java.io.*;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;


public class Converter {
    private String filePath;

    /**
     * Constructor for converter
     * @param filePath File path for set path
     */
    public Converter(String filePath) {
        this.filePath = filePath;
    }


    /**
     * Convert .cc file if code has class or static_cast
     * If module wasn't parsed, parse it
     */
    public boolean convert() {
        Set<CppClass> classSet = new HashSet<>();

        /* Check hasClass, hasStaticCast */
        Checker checker = new Checker(filePath);
        checker.checkFile();

        if (checker.isHasClass()) {
            Optional<String> result = Parsing.parseClass(filePath, classSet);
            result.ifPresent(x -> IO.rewriteCppFile(filePath, x));
        }
        IO.writeClassInfo(filePath, classSet);
        if (checker.isHasStaticCast()) {
            Optional<String> result = Parsing.parseStaticCast(Paths.get(filePath).toString(), classSet);
            result.ifPresent(x -> IO.rewriteCppFile(filePath, x));
        }

        return checker.isHasStaticCast();
    }
}
