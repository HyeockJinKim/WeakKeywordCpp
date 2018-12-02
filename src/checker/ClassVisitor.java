package checker;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;
import weakclass.CppFunction;

import java.util.Set;

public class ClassVisitor<T> extends CommonVisitor<Void> {
    private Set<CppClass> classSet;
    private CppClass currentClass;
    private CppFunction currentFunction;

    private String currentAccessSpecifier;
    private boolean isParam;
    private boolean isVirtual;
    private boolean isBaseClause;

    /**
     * Constructor for ClassVisitor
     * @param tokens Token stream for parsing
     * @param classSet Class information
     */
    public ClassVisitor(CommonTokenStream tokens, Set<CppClass> classSet) {
        super();
        reWriter = new TokenStreamRewriter(tokens);
        this.classSet = classSet;
    }

    /**
     *
     * @param ctx
     */
    @Override
    public Void visitClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        // Ignore Nested Class !!
        if (currentClass != null) {
        }
        if (ctx.classhead().classheadname() != null) { // no name check
            currentClass = new CppClass(getText(ctx.classhead().classheadname()));
            currentAccessSpecifier = "\n";
            super.visitClassspecifier(ctx);
            reWriteClass(ctx); // ctx visits
            classSet.add(currentClass);
            currentClass = null;
        } else {
            super.visitClassspecifier(ctx);
        }
        return null;
    }

    private void reWriteClass(CPP14Parser.ClassspecifierContext ctx) {
        if (ctx != null) {
            if (ctx.classhead() != null) {
                if (ctx.classhead().baseclause() != null
                        && currentClass.isVirtual()) {
                    StringBuilder tempClass = new StringBuilder();
                    tempClass.append(getText(ctx.classhead().classkey()))
                            .append(" _")
                            .append(getText(ctx.classhead().classheadname()))
                            .append(" ")
                            .append(getText(ctx.classhead().baseclause()))
                            .append(" {");
                    currentClass.functionMap.keySet().stream()
                            .filter(x -> currentClass.functionMap.get(x).size() > 0)
                            .peek(tempClass::append)
                            .peek(x -> currentAccessSpecifier = x)
                            .map(currentClass.functionMap::get)
                            .forEach(x -> x.forEach(context -> tempClass.append(copyMember(context))));
                    tempClass.append("\n};\n\n");
                    reWriter.replace(ctx.classhead().baseclause().start,
                            ctx.classhead().baseclause().stop,
                            ": public _" + getText(ctx.classhead().classheadname()));
                    reWriter.insertBefore(ctx.start, tempClass.toString());
                }
            }
        }
    }

    @Override
    public Void visitBaseclause(CPP14Parser.BaseclauseContext ctx) {
        isBaseClause = true;
        super.visitBaseclause(ctx);
        isBaseClause = false;
        return null;
    }

    @Override
    public Void visitBasespecifier(CPP14Parser.BasespecifierContext ctx) {
        if (isBaseClause) {
            classSet.stream()
                    .filter(x -> x.className.equals(ctx.basetypespecifier().getText()))
                    .findAny()
                    .ifPresent(x -> currentClass.superSet.add(x));
        }
        super.visitBasespecifier(ctx);
        return null;
    }

    @Override
    public Void visitFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx) {
        if (ctx.Virtual() != null)
            isVirtual = true;
        super.visitFunctionspecifier(ctx);
        return null;
    }

    @Override
    public Void visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        if (currentClass != null) {
            isVirtual = false;
            currentFunction = new CppFunction();
            currentFunction.functionName = getFunctionName(ctx);
            super.visitFunctiondefinition(ctx);
            addFunctionInfo(ctx);
            currentFunction = null;
        } else {
            super.visitFunctiondefinition(ctx);
        }

        return null;
    }

    private void addFunctionInfo(CPP14Parser.FunctiondefinitionContext ctx) {
        if (currentClass != null) {
            if (isVirtual) {
                if (currentClass.superSet.stream()
                        .noneMatch(x -> x.virtualFunctionSet.contains(currentFunction)))
                    currentClass.virtualFunctionSet.add(currentFunction);
            } else {
                if (!getFunctionName(ctx).replace("~", "").equals(currentClass.className)) {
                    currentClass.functionMap.computeIfPresent(currentAccessSpecifier, (k,v) -> v).add(ctx);
                }
            }
        }
    }


    /**
     * access specifier check
     */
    @Override
    public Void visitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        if (currentClass != null) {
            if (ctx.accessspecifier() != null)
                currentAccessSpecifier = "\n" + getText(ctx.accessspecifier()) + ":\n";
        }
        super.visitMemberspecification(ctx);
        return null;
    }

    @Override
    public Void visitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        if (currentClass != null) {
            if (ctx.functiondefinition() == null) {
                currentClass.functionMap.computeIfPresent(currentAccessSpecifier, (k, v) -> v).add(ctx);
            }
        }
        super.visitMemberdeclaration(ctx);
        return null;
    }


    @Override
    public Void visitTypespecifier(CPP14Parser.TypespecifierContext ctx) {
        if (currentClass != null) {
            if (currentFunction != null && isParam)
                currentFunction.functionParameter.add(getText(ctx));
        }
        super.visitTypespecifier(ctx);
        return null;
    }

    @Override
    public Void visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        isParam = true;
        super.visitParametersandqualifiers(ctx);
        isParam = false;

        return null;
    }



    private String copyMember(ParserRuleContext ctx) {
        if (!"\nprivate:\n".equals(currentAccessSpecifier))
            reWriter.replace(ctx.start, ctx.stop, "");
        return getText(ctx) + "\n";
    }

    private String getFunctionName(CPP14Parser.FunctiondefinitionContext ctx) {
        return getText(ctx.declarator()
                .ptrdeclarator())
                .replace("*", "")
                .split("[\\(]")[0];
    }

    public Set<CppClass> getClassSet() {
        return this.classSet;
    }
}
