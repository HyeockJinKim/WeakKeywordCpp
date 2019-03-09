package processing;

import processing.util.Checker;
import processing.util.IO;
import processing.util.Parsing;
import weakclass.CppClass;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Optional;


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
        HashSet<CppClass> classSet = new HashSet<>();
        /* Check hasClass, hasStaticCast */
        Checker checker = new Checker(filePath);
        checker.checkFile();
        classSet.addAll(checker.getClassSet());
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
