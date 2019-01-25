package checker.util;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
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

                memberSet.stream()
                        .map(CppMember::getContent)
                        .forEach(sb::append);

                functionSet.stream()
                        .map(CppFunction::getContent)
                        .forEach(sb::append);
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
        for (CppAccessSpecifier cppAccessSpecifier : CppAccessSpecifier.values()) {
            Set<CppFunction> functionSet = currentClass.getFunctionSet(cppAccessSpecifier);
            Set<CppMember> memberSet = currentClass.getMemberSet(cppAccessSpecifier);
            Set<CppFunction> virtualSet = currentClass.getVirtualFunctionSet(cppAccessSpecifier);
            if (!functionSet.isEmpty() || !memberSet.isEmpty()) {
                if (cppAccessSpecifier == CppAccessSpecifier.PRIVATE) {
                    sb.append(cppAccessSpecifier.getName());

                    memberSet.stream()
                            .map(CppMember::getContent)
                            .forEach(sb::append);

                    functionSet.stream()
                            .map(CppFunction::getContent)
                            .forEach(sb::append);
                }
            }

            if (!virtualSet.isEmpty()) {
                sb.append(cppAccessSpecifier.getName());

                virtualSet.stream()
                        .map(CppFunction::getContent)
                        .forEach(sb::append);
            }
        }
        sb.append("}");
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
