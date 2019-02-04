package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;

import java.util.LinkedHashSet;

public class ClassVisitor<T> extends CommonVisitor<Void> {
    private LinkedHashSet<CppClass> classSet;
    /**
     * Constructor for ClassVisitor
     * @param tokens Token stream for parsing
     * @param classSet Class information
     */
    public ClassVisitor(CommonTokenStream tokens, LinkedHashSet<CppClass> classSet) {
        super();
        reWriter = new TokenStreamRewriter(tokens);
        this.classSet = classSet;
    }

    /**
     *
     * @param ctx
     */
    @Override
    public Void visitClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        // Ignore Nested Class !!
        Info.checkClass(reWriter, classSet, ctx);
        return null;
    }

    public LinkedHashSet<CppClass> getClassSet() {
        return this.classSet;
    }
}
