package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;
import weakclass.CppAccessSpecifier;
import weakclass.CppFunction;

import java.util.ArrayList;
import java.util.HashSet;

public class MemberVisitor<T> extends CommonVisitor<HashSet<CppFunction>> {
    private CppAccessSpecifier currentAccessSpecifier;
    private CppFunction currentFunction;
    private HashSet<CppFunction> functionSet;

    MemberVisitor() {
        this.currentAccessSpecifier = CppAccessSpecifier.DEFAULT;
        this.functionSet = new HashSet<>();
    }

    /**
     * access specifier check
     */
    @Override
    public HashSet<CppFunction> visitAccessspecifier(CPP14Parser.AccessspecifierContext ctx) {
        currentAccessSpecifier = Info.getAccessSpecifier(ctx);

        return super.visitAccessspecifier(ctx);
    }

    @Override
    public HashSet<CppFunction> visitFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx) {
        if (ctx.Virtual() != null)
            currentFunction.setVirtual();
        super.visitFunctionspecifier(ctx);
        return null;
    }


    @Override
    public HashSet<CppFunction> visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        currentFunction = new CppFunction(currentAccessSpecifier);
        currentFunction.setName(Info.getFunctionName(ctx));
        currentFunction.setContent(ctx);
        super.visitFunctiondefinition(ctx);
        functionSet.add(currentFunction);

        return null;
    }

    @Override
    public HashSet<CppFunction> visitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        super.visitMemberspecification(ctx);
        return functionSet;
    }

    @Override
    public HashSet<CppFunction> visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        ParamVisitor<ArrayList<String>> visitor = new ParamVisitor<>();
        currentFunction.setParameters(visitor.visitParametersandqualifiers(ctx));
        return null;
    }

}
