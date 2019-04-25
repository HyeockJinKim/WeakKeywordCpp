package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import checker.util.Rewrite;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;

import java.util.ArrayList;
import java.util.HashSet;

public class FunctionVisitor<T> extends CommonVisitor<Void> {
    private HashSet<CppClass> classSet;
    private ParserRuleContext context;
    private CppClass currentClass;
    private String functionName;
    private ArrayList<String> params;

    FunctionVisitor(TokenStreamRewriter reWriter, HashSet<CppClass> classSet) {
        super();
        this.reWriter = reWriter;
        this.classSet = classSet;
        this.params = new ArrayList<>();
    }

    @Override
    public Void visitDeclaratorid(CPP14Parser.DeclaratoridContext ctx) {
        String name = Info.getFunctionName(ctx);
        String className = Info.getClassNameOfFunction(namespace, name);
        if (className != null) {
            functionName = Info.getFunctionNameOfFunction(name);
            classSet.stream()
                    .filter(x -> x.equals(className))
                    .findAny()
                    .ifPresent(x -> {
                        currentClass = x;
                        context = ctx;
                    });
        }
        return null;
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
        if (currentClass != null && currentClass.isWeak()) {
            currentClass.findFunction(functionName, params)
                    .ifPresent(x -> Rewrite.reWriteFunctionName(reWriter, context, ctx, x));
        }
        return null;
    }

    @Override
    public Void visitFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
        return null;
    }
}
