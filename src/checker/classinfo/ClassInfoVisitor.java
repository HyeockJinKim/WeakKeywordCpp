package checker.classinfo;

import checker.CommonVisitor;
import checker.util.Info;
import checker.util.Rewrite;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;
import weakclass.CppFunction;
import weakclass.CppMember;

import java.util.Set;
import java.util.Stack;

/**
 * Visit Class's Information
 * @param <T>
 */
public class ClassInfoVisitor<T> extends CommonVisitor<Void> {
    private Set<CppClass> classSet;
    private CppClass currentClass;

    public ClassInfoVisitor(TokenStreamRewriter reWriter, Set<CppClass> classSet) {
        super();
        this.classSet = classSet;
        this.reWriter = reWriter;
        currentClass = null;
    }

    @Override
    public Void visitBaseclause(CPP14Parser.BaseclauseContext ctx) {
        SuperClassVisitor visitor = new SuperClassVisitor(classSet, currentClass);
        visitor.visitBaseclause(ctx);

        return null;
    }

    private void visitClass(CPP14Parser.ClassspecifierContext ctx) {
        currentClass = new CppClass(Info.getText(ctx.classhead().classheadname()), (Stack<String>) namespace.clone());
        super.visitClassspecifier(ctx);
        Rewrite.reWriteClass(reWriter, ctx, currentClass);
        classSet.add(currentClass);
    }

    @Override
    public Void visitClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        if (currentClass != null) { // Visit Nested Class
            namespace.push(currentClass.getName());
            Info.checkClass(reWriter, classSet, ctx);
            namespace.pop();
        } else // Visit Class
            visitClass(ctx);

        return null;
    }

    @Override
    public Void visitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        MemberVisitor<CppFunction> visitor = new MemberVisitor<>();
        Set<CppFunction> memberFunctionSet = visitor.visitMemberspecification(ctx);
        for (CppFunction cppFunction : memberFunctionSet) {
            currentClass.updateFunction(cppFunction);
        }
        Set<CppMember> memberSet = visitor.getMemberSet();
        for (CppMember cppMember : memberSet) {
            currentClass.updateMember(cppMember);
        }

        return null;
    }

//    @Override
//    public Void visitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
//        MemberVisitor<CppFunction> visitor = new MemberVisitor<>(currentAccessSpecifier);
//        CppFunction currentFunction = visitor.visitMemberdeclaration(ctx);
//        currentClass.updateFunction(currentFunction);
//
//        return null;
//    }

}
