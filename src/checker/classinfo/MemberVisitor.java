package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.tree.ParseTree;
import weakclass.CppAccessSpecifier;
import weakclass.CppFunction;
import weakclass.CppMember;
import weakclass.CppNonVirtual;

import java.util.ArrayList;
import java.util.HashSet;

public class MemberVisitor<T> extends CommonVisitor<HashSet<CppFunction>> {
    private CppAccessSpecifier currentAccessSpecifier;
    private CppFunction currentFunction;
    private HashSet<CppFunction> functionSet;
    private HashSet<CppMember> memberSet;
    private boolean isFunction;

    MemberVisitor() {
        this.currentAccessSpecifier = CppAccessSpecifier.DEFAULT;
        this.functionSet = new HashSet<>();
        this.memberSet = new HashSet<>();
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
    public HashSet<CppFunction> visitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        currentFunction.setContent(ctx);
        isFunction = false;
        super.visitMemberdeclaration(ctx);
        if (!isFunction) {
            CppNonVirtual currentMember = new CppNonVirtual(currentAccessSpecifier);
            currentMember.setContent(ctx);
            memberSet.add(currentMember);
        }

        return null;
    }

    @Override
    public HashSet<CppFunction> visitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        currentFunction = new CppFunction(currentAccessSpecifier);
        super.visitMemberspecification(ctx);
        return functionSet;
    }

    @Override
    public HashSet<CppFunction> visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        String name = Info.getFunctionName(Info.getText(ctx.getParent()));
        ParamVisitor<ArrayList<String>> visitor = new ParamVisitor<>();
        ArrayList<String> paramList = visitor.visitParametersandqualifiers(ctx);
        ArrayList<String> paramNameList = visitor.getParamNameList();

        currentFunction.setParameters(paramList);
        currentFunction.setParamNames(paramNameList);
        currentFunction.setName(name);
        functionSet.add(currentFunction);
        isFunction = true;
        return null;
    }


    @Override
    public HashSet<CppFunction> visitMeminitializerid(CPP14Parser.MeminitializeridContext ctx) {
        currentFunction.setMemInitializer(ctx);

        return super.visitMeminitializerid(ctx);
    }

    @Override
    public HashSet<CppFunction> visitFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
        for (ParseTree child : ctx.children) {
            if (child != ctx.compoundstatement())
                visit(child);
        }
        return null;
    }

    public HashSet<CppMember> getMemberSet() {
        return memberSet;
    }
}
