package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;

import java.util.HashSet;

public class ClassVisitor<T> extends CommonVisitor<Void> {
    private HashSet<CppClass> classSet;
    /**
     * Constructor for ClassVisitor
     * @param tokens Token stream for parsing
     * @param classSet Class information
     */
    public ClassVisitor(CommonTokenStream tokens, HashSet<CppClass> classSet) {
        super();
        reWriter = new TokenStreamRewriter(tokens);
        this.classSet = classSet;
    }

    /**
     * Class Converter
     * @param ctx
     */
    @Override
    public Void visitClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        // Ignore Nested Class !!
        Info.checkClass(reWriter, classSet, ctx);
        return null;
    }

    @Override
    public Void visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        FunctionVisitor visitor = new FunctionVisitor(reWriter, classSet);
        visitor.visitFunctiondefinition(ctx);
        return null;
    }

    public HashSet<CppClass> getClassSet() {
        return this.classSet;
    }
}
