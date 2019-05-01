package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import checker.util.Rewrite;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;

import java.util.ArrayList;
import java.util.HashSet;

public class FunctionVisitor<T> extends CommonVisitor<Void> {
    private HashSet<CppClass> classSet;
    private ArrayList<String> params;

    FunctionVisitor(TokenStreamRewriter reWriter, HashSet<CppClass> classSet) {
        super();
        this.reWriter = reWriter;
        this.classSet = classSet;
        this.params = new ArrayList<>();
    }

    @Override
    public Void visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        ParamVisitor<ArrayList<String>> visitor = new ParamVisitor<>();
        params = visitor.visitParametersandqualifiers(ctx);
        return null;
    }

    @Override
    public Void visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        super.visitFunctiondefinition(ctx);
        Token start = ctx.declarator().start;
        Token stop = ctx.declarator().stop;

        String functionFullName = Info.getText(ctx.declarator());
        String className = Info.getClassNameOfFunction(functionFullName);
        String classFullName = Info.getFullName(namespace, className);
        String functionName = Info.getFunctionNameOfFunction(Info.getFunctionName(functionFullName));

        classSet.stream()
                .filter(x -> x.equals(classFullName))
                .findAny()
                .ifPresent(cls -> {
                    if (cls.isWeak()) {
                        cls.findFunction(functionName, params)
                                .ifPresent(func -> Rewrite.reWriteFunctionName(reWriter, start, stop, func, ctx.stop, functionFullName, Info.getText(ctx)));
                    }
                });

        return null;
    }

    @Override
    public Void visitFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
        return null;
    }
}
