package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import checker.util.Rewrite;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;
import weakclass.CppNamespace;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;

public class FunctionVisitor<T> extends CommonVisitor<Void> {
    private HashSet<CppClass> classSet;
    private ArrayList<String> params;
    private CppClass ownerClass;

    FunctionVisitor(TokenStreamRewriter reWriter, HashSet<CppClass> classSet) {
        super();
        this.reWriter = reWriter;
        this.classSet = classSet;
//        this.ownerClass = null;
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
        Token start = ctx.declarator().start;
        Token stop = ctx.declarator().stop;

        String temp = Info.getText(ctx.declarator());
        if (temp.startsWith("::")) {
            temp = Info.getText(ctx.declspecifierseq()) + temp;
        }
        String functionFullName = temp;
        String className = Info.getClassNameOfFunction(functionFullName);
        String classFullName = Info.getFullName(namespace, className);
        String functionName = Info.getFunctionNameOfFunction(Info.getFunctionName(functionFullName));


        Optional<CppClass> cls = classSet.stream()
                .filter(x -> x.equals(classFullName))
                .findAny();

        if (cls.isPresent()) {
            CppClass clss = cls.get();
            if (clss.isWeak()) {
                ownerClass = clss;
                ownerClass.findFunction(functionName, params)
                        .ifPresent(func -> Rewrite.reWriteFunctionName(reWriter, start, stop, func, ctx.stop, functionFullName, Info.getText(ctx)));
            }
        }

        super.visitFunctiondefinition(ctx);
        return null;
    }

    @Override
    public Void visitMeminitializerid(CPP14Parser.MeminitializeridContext ctx) {
        String classFullName = Info.getFullName(namespace, Info.getText(ctx));
        if (ownerClass != null) {
            ownerClass.getSuperSet()
                    .stream()
                    .map(CppNamespace::getFullName)
                    .filter(x -> x.equals(classFullName))
                    .findAny()
                    .ifPresent(func -> Rewrite.rewriteMeminitializer(reWriter, ctx.start, ctx.stop, "_"+ownerClass.getName()));
        }

        return super.visitMeminitializerid(ctx);
    }

    @Override
    public Void visitCompoundstatement(CPP14Parser.CompoundstatementContext ctx) {
        return null;
    }
}
