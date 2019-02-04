package checker.util;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppAccessSpecifier;
import weakclass.CppClass;
import weakclass.CppFunction;
import weakclass.CppMember;

import java.util.Set;

public class Rewrite {
    private static Rewrite ourInstance = new Rewrite();

    public static Rewrite getInstance() {
        return ourInstance;
    }

    private Rewrite() {
    }

    public static void castTempClass(TokenStreamRewriter reWriter, CPP14Parser.PostfixexpressionContext ctx) {
        reWriter.insertBefore(ctx.thetypeid().start, "_");
    }

    public static void castLimited(TokenStreamRewriter reWriter, CPP14Parser.IdexpressionContext ctx) {
        reWriter.insertBefore(ctx.stop, "_");
    }

    private static void reWriteTempClassHead(StringBuilder sb, CPP14Parser.ClassspecifierContext ctx) {
        sb.append(Info.getText(ctx.classhead().classkey()))
                .append(" _")
                .append(Info.getText(ctx.classhead().classheadname()))
                .append(" ")
                .append(Info.getText(ctx.classhead().baseclause()))
                .append(" {");
    }

    private static void reWriteTempClassMember(CppClass currentClass, StringBuilder sb) {
        for (CppAccessSpecifier cppAccessSpecifier : CppAccessSpecifier.values()) {
            Set<CppFunction> functionSet = currentClass.getFunctionSet(cppAccessSpecifier);
            Set<CppMember> memberSet = currentClass.getMemberSet(cppAccessSpecifier);
            if (!functionSet.isEmpty() || !memberSet.isEmpty()) {
                sb.append(cppAccessSpecifier.getName());
                reWriteMember(memberSet, functionSet, sb);
            }
        }
        sb.append("};\n\n");
    }

    private static void reWriteBaseClassHead(StringBuilder sb, CPP14Parser.ClassspecifierContext ctx) {
        sb.append(Info.getText(ctx.classhead().classkey()))
                .append(" ")
                .append(Info.getText(ctx.classhead().classheadname()))
                .append(" : public _")
                .append(Info.getText(ctx.classhead().classheadname()))
                .append(" {");
    }

    private static void reWriteBaseClassMember(CppClass currentClass, StringBuilder sb) {

        Set<CppFunction> functionSet = currentClass.getFunctionSet(CppAccessSpecifier.PRIVATE);
        Set<CppMember> memberSet = currentClass.getMemberSet(CppAccessSpecifier.PRIVATE);
        Set<CppFunction> virtualSet = currentClass.getVirtualFunctionSet(CppAccessSpecifier.PRIVATE);

        if (!functionSet.isEmpty() || !memberSet.isEmpty()) {
            sb.append(CppAccessSpecifier.PRIVATE.getName());
            reWriteMember(memberSet, functionSet, sb);
            virtualSet.stream()
                    .map(CppFunction::getContent)
                    .forEach(sb::append);
        }

        for (CppAccessSpecifier cppAccessSpecifier : CppAccessSpecifier.nonPrivate()) {
            virtualSet = currentClass.getVirtualFunctionSet(cppAccessSpecifier);
            if (!virtualSet.isEmpty()) {
                sb.append(cppAccessSpecifier.getName());

                virtualSet.stream()
                        .map(CppFunction::getContent)
                        .forEach(sb::append);
            }
        }
        sb.append("}");
    }

    private static void reWriteMember(Set<CppMember> memberSet, Set<CppFunction> functionSet, StringBuilder sb) {
        memberSet.stream()
                .map(CppMember::getContent)
                .forEach(sb::append);

        functionSet.stream()
                .map(CppFunction::getContent)
                .forEach(sb::append);
    }

    public static void reWriteClass(TokenStreamRewriter reWriter, CPP14Parser.ClassspecifierContext ctx, CppClass currentClass) {
        if (ctx != null) {
            if (ctx.classhead() != null) {
                if (currentClass.isWeak()) {
                    StringBuilder tempClass = new StringBuilder();
                    reWriteTempClassHead(tempClass, ctx);
                    reWriteTempClassMember(currentClass, tempClass);

                    reWriteBaseClassHead(tempClass, ctx);
                    reWriteBaseClassMember(currentClass, tempClass);

                    reWriter.replace(ctx.start, ctx.stop, tempClass.toString());
                }
            }
        }
    }
}
