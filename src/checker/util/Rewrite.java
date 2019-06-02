package checker.util;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppAccessSpecifier;
import weakclass.CppClass;
import weakclass.CppFunction;
import weakclass.CppMember;

import java.util.Set;
import java.util.Stack;

public class Rewrite {
    private static Rewrite ourInstance = new Rewrite();

    private Rewrite() {}

    /**
     * In order to rewrite static_cast, Use the following functions.
     */

    public static void castTempClass(TokenStreamRewriter reWriter, CPP14Parser.PostfixexpressionContext ctx, CppClass currentClass) {
        reWriter.replace(ctx.thetypeid().start, ctx.thetypeid().stop, currentClass.getTempClassName()+(Info.getText(ctx.thetypeid()).contains("*")?"*":""));
    }

    public static void castLimited(TokenStreamRewriter reWriter, CPP14Parser.IdexpressionContext ctx, CppClass currentClass) {
        reWriter.replace(ctx.start, ctx.stop, currentClass.getTempClassName()+(Info.getText(ctx).contains("*")?"*":""));
    }

    /**
     * In order to rewrite function, Use the following function.
     */

    public static void reWriteFunctionName(TokenStreamRewriter reWriter, Token start, Token stop, CppFunction function,
                                           Token fStop, String functionName, String functions) {
        reWriter.replace(start, stop, Info.getTempClassNameOfFunction(functionName));
        if (function.isPrivate() || function.isOnlyVirtual()) {
            reWriter.insertAfter(fStop, "\n\n");
            reWriter.insertAfter(fStop, functions);
        }
    }

    public static void rewriteMeminitializer(TokenStreamRewriter reWriter, Token start, Token stop, String functionName) {
        reWriter.replace(start, stop, functionName);
    }

    /**
     * In order to rewrite class, Use the following functions.
     */

    private static void reWriteTempClassHead(StringBuilder sb, CPP14Parser.ClassspecifierContext ctx, CppClass currentClass, String namespace) {
        sb.append(Info.getText(ctx.classhead().classkey()))
                .append(namespace)
                .append("_")
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
            } else if (cppAccessSpecifier == CppAccessSpecifier.PRIVATE) {
                sb.append(cppAccessSpecifier.getName());
            }
            if (cppAccessSpecifier == CppAccessSpecifier.PRIVATE) {
                Set<CppFunction> virtualSet = currentClass.getOnlyVirtualFunctionSet();
                virtualSet.stream()
                        .map(CppFunction::getContent)
                        .map(x -> x.replace("virtual ", ""))
                        .forEach(sb::append);
            }
        }
        sb.append("};\n\n");
    }

    private static void reWriteBaseClassHead(StringBuilder sb, CPP14Parser.ClassspecifierContext ctx, String namepsace) {
        sb.append(Info.getText(ctx.classhead().classkey()))
                .append(" ")
                .append(Info.getText(ctx.classhead().classheadname()))
                .append(" : public")
                .append(namepsace)
                .append("_")
                .append(Info.getText(ctx.classhead().classheadname()))
                .append(" {");
    }

    private static void reWriteBaseClassMember(CppClass currentClass, StringBuilder sb) {
        for (CppAccessSpecifier cppAccessSpecifier : CppAccessSpecifier.privates()) {
            Set<CppFunction> functionSet = currentClass.getFunctionSet(cppAccessSpecifier);
            Set<CppMember> memberSet = currentClass.getMemberSet(cppAccessSpecifier);
            Set<CppFunction> virtualSet = currentClass.getVirtualFunctionSet(cppAccessSpecifier);
            if (!functionSet.isEmpty() || !memberSet.isEmpty()) {
                sb.append(cppAccessSpecifier.getName());
                reWriteMember(memberSet, functionSet, sb);
                virtualSet.stream()
                        .map(CppFunction::getContent)
                        .forEach(sb::append);
            }
        }

        for (CppAccessSpecifier cppAccessSpecifier : CppAccessSpecifier.nonPrivate()) {
            Set<CppFunction> virtualSet = currentClass.getVirtualFunctionSet(cppAccessSpecifier);
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

    private static String reWriteMemInitializer(TokenStreamRewriter reWriter, CPP14Parser.MemberdeclarationContext memberDeclaration,
                                               CPP14Parser.MeminitializeridContext memInitializer, String className) {
        reWriter.replace(memInitializer.start, memInitializer.stop, "_"+className);
        return "    " + reWriter.getText(memberDeclaration.getSourceInterval())+"\n";
    }

    public static void reWriteClass(TokenStreamRewriter reWriter, CPP14Parser.ClassspecifierContext ctx, CppClass currentClass, Stack<String> namespaceStack) {
        if (ctx != null) {
            if (ctx.classhead() != null) {
                if (currentClass.isWeak()) {

                    currentClass.makeConstructor();
                    currentClass.linkConstructor().forEach(x -> {
                        String content = Rewrite.reWriteMemInitializer(reWriter, x.getMemberDeclaration(), x.getMemInitializer(), currentClass.getName());
                        x.setContent(content);
                    });
                    StringBuilder nameString = new StringBuilder();
                    for (String s : namespaceStack) {
                        nameString.append(s)
                                .append("::");
                    }
                    String name = " "+currentClass.getNamespace().replace(nameString.toString(), "");
                    StringBuilder tempClass = new StringBuilder();
                    reWriteTempClassHead(tempClass, ctx, currentClass, name);

                    reWriteTempClassMember(currentClass, tempClass);

                    reWriteBaseClassHead(tempClass, ctx, name);
                    reWriteBaseClassMember(currentClass, tempClass);

                    reWriter.replace(ctx.start, ctx.stop, tempClass.toString());
                }
            }
        }
    }
}
