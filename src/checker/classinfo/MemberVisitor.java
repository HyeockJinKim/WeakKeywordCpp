package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import weakclass.CppAccessSpecifier;
import weakclass.CppFunction;
import weakclass.CppMember;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class MemberVisitor<T> extends CommonVisitor<LinkedHashSet<CppFunction>> {
    private CppAccessSpecifier currentAccessSpecifier;
    private CppFunction currentFunction;
    private LinkedHashSet<CppFunction> functionSet;
    private LinkedHashSet<CppMember> memberSet;

    private boolean isFunction;

    MemberVisitor() {
        this.currentAccessSpecifier = CppAccessSpecifier.DEFAULT;
        this.functionSet = new LinkedHashSet<>();
        this.memberSet = new LinkedHashSet<>();
    }

    /**
     * access specifier check
     */
    @Override
    public LinkedHashSet<CppFunction> visitAccessspecifier(CPP14Parser.AccessspecifierContext ctx) {
        currentAccessSpecifier = Info.getAccessSpecifier(ctx);
        return super.visitAccessspecifier(ctx);
    }

    @Override
    public LinkedHashSet<CppFunction> visitFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx) {
        if (ctx.Virtual() != null)
            currentFunction.setVirtual();
        super.visitFunctionspecifier(ctx);
        return null;
    }

    @Override
    public LinkedHashSet<CppFunction> visitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        currentFunction.setContent(ctx);
        super.visitMemberdeclaration(ctx);

        if (!isFunction) {
            CppMember currentMember = new CppMember(currentAccessSpecifier);
            currentMember.setContent(ctx);
            memberSet.add(currentMember);
        }

        return null;
    }

    @Override
    public LinkedHashSet<CppFunction> visitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        currentFunction = new CppFunction(currentAccessSpecifier);
        super.visitMemberspecification(ctx);
        return functionSet;
    }

    @Override
    public LinkedHashSet<CppFunction> visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        ParamVisitor<ArrayList<String>> visitor = new ParamVisitor<>();
        currentFunction.setParameters(visitor.visitParametersandqualifiers(ctx));
        currentFunction.setName(Info.getFunctionName(ctx.getParent()));
        functionSet.add(currentFunction);
        isFunction = true;
        return null;
    }

    @Override
    public LinkedHashSet<CppFunction> visitFunctionbody(CPP14Parser.FunctionbodyContext ctx) {
        return null;
    }

    public LinkedHashSet<CppMember> getMemberSet() {
        return memberSet;
    }
}
