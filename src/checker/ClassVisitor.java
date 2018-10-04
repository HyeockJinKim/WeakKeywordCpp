package checker;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import weakclass.CppClass;
import weakclass.CppFunction;

import java.util.HashSet;
import java.util.Set;

public class ClassVisitor<T> extends CommonVisitor<T> {
    private Set<CppClass> classSet;
    private CppClass currentClass;
    private CppFunction currentFunction;

    private String currentAccessSpecifier;
    private boolean isParam;
    private boolean isVirtual;
    private boolean isBaseClause;

    public ClassVisitor(CommonTokenStream tokens, Set<CppClass> classSet) {
        super();
        reWriter = new TokenStreamRewriter(tokens);
        this.classSet = classSet;
    }

    @Override
    public T visitClassspecifier(CPP14Parser.ClassspecifierContext ctx) {
        T visits;
        // Ignore Nested Class !!
        if (currentClass != null) {
            return null;
        }
        if (ctx.classhead().classheadname() != null) { // no name check
            currentClass = new CppClass(getText(ctx.classhead().classheadname()));
            currentAccessSpecifier = "\n";
            visits = super.visitClassspecifier(ctx);
            reWriteClass(ctx); // ctx visits
            classSet.add(currentClass);
            currentClass = null;
        } else {
            visits = super.visitClassspecifier(ctx);
        }
        return visits;
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
    public T visitBaseclause(CPP14Parser.BaseclauseContext ctx) {
        isBaseClause = true;
        T result =  super.visitBaseclause(ctx);
        isBaseClause = false;
        return result;
    }

    @Override
    public T visitBasespecifier(CPP14Parser.BasespecifierContext ctx) {
        if (isBaseClause) {
            classSet.stream()
                    .filter(x -> x.className.equals(ctx.basetypespecifier().getText()))
                    .findAny()
                    .ifPresent(x -> currentClass.superSet.add(x));
        }
        return super.visitBasespecifier(ctx);
    }

    @Override
    public T visitFunctionspecifier(CPP14Parser.FunctionspecifierContext ctx) {
        if (ctx.Virtual() != null)
            isVirtual = true;
        return super.visitFunctionspecifier(ctx);
    }

    @Override
    public T visitFunctiondefinition(CPP14Parser.FunctiondefinitionContext ctx) {
        T visits;
        if (currentClass != null) {
            isVirtual = false;
            currentFunction = new CppFunction();
            currentFunction.functionName = getFunctionName(ctx);
            visits = super.visitFunctiondefinition(ctx);
            addFunctionInfo(ctx);
            currentFunction = null;
        } else {
            visits = super.visitFunctiondefinition(ctx);
        }
        return visits;
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
    public T visitMemberspecification(CPP14Parser.MemberspecificationContext ctx) {
        if (currentClass != null) {
            if (ctx.accessspecifier() != null)
                currentAccessSpecifier = "\n" + getText(ctx.accessspecifier()) + ":\n";
        }
        return super.visitMemberspecification(ctx);
    }

    @Override
    public T visitMemberdeclaration(CPP14Parser.MemberdeclarationContext ctx) {
        if (currentClass != null) {
            if (ctx.functiondefinition() == null) {
                currentClass.functionMap.computeIfPresent(currentAccessSpecifier, (k, v) -> v).add(ctx);
            }
        }
        return super.visitMemberdeclaration(ctx);
    }


    @Override
    public T visitTypespecifier(CPP14Parser.TypespecifierContext ctx) {
        if (currentClass != null) {
            if (currentFunction != null && isParam)
                currentFunction.functionParameter.add(getText(ctx));
        }
        return super.visitTypespecifier(ctx);
    }

    @Override
    public T visitParametersandqualifiers(CPP14Parser.ParametersandqualifiersContext ctx) {
        isParam = true;
        T visits = super.visitParametersandqualifiers(ctx);
        isParam = false;

        return visits;
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
