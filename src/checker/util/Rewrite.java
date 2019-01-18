package checker.util;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppAccessSpecifier;
import weakclass.CppClass;
import weakclass.CppFunction;

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

    private static String copyMember(TokenStreamRewriter reWriter, ParserRuleContext ctx) {
        reWriter.replace(ctx.start, ctx.stop, "");
        return Info.getText(ctx) + "\n";
    }

    public static void reWriteMember(CppClass currentClass, StringBuilder sb, TokenStreamRewriter reWriter) {
        for (CppAccessSpecifier cppAccessSpecifier : CppAccessSpecifier.values()) {
            Set<CppFunction> functionSet = currentClass.getFunctionSet(cppAccessSpecifier);
            if (!functionSet.isEmpty()) {
                sb.append(cppAccessSpecifier.getName());
                if (cppAccessSpecifier == CppAccessSpecifier.PRIVATE) {
                    functionSet.stream()
                            .map(x -> copyMember(reWriter, x.getContext()))
                            .forEach(sb::append);
                } else {
                    functionSet.stream()
                            .map(CppFunction::getContent)
                            .forEach(sb::append);
                }
            }
        }
    }
    private static void reWriteClassHead(StringBuilder sb, CPP14Parser.ClassspecifierContext ctx) {
        sb.append(Info.getText(ctx.classhead().classkey()))
                .append(" _")
                .append(Info.getText(ctx.classhead().classheadname()))
                .append(" ")
                .append(Info.getText(ctx.classhead().baseclause()))
                .append(" {");
    }

    private static void reWriteTempClass(StringBuilder sb, CPP14Parser.ClassspecifierContext ctx, TokenStreamRewriter reWriter) {
        sb.append("\n};\n\n");
        reWriter.replace(ctx.classhead().baseclause().start,
                ctx.classhead().baseclause().stop,
                ": public _" + Info.getText(ctx.classhead().classheadname()));
        reWriter.insertBefore(ctx.start, sb.toString());
    }

    public static void reWriteClass(TokenStreamRewriter reWriter, CPP14Parser.ClassspecifierContext ctx, CppClass currentClass) {
        if (ctx != null) {
            if (ctx.classhead() != null) {
                if (currentClass.isWeak()) {
                    StringBuilder tempClass = new StringBuilder();
                    reWriteClassHead(tempClass, ctx);
                    reWriteMember(currentClass, tempClass, reWriter);
                    reWriteTempClass(tempClass, ctx, reWriter);
                }
            }
        }
    }
}
