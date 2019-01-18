package checker.util;

import checker.classinfo.ClassInfoVisitor;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import weakclass.CppAccessSpecifier;
import weakclass.CppClass;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

public class Info {
    private static Info ourInstance = new Info();

    public static Info getInstance() {
        return ourInstance;
    }

    private Info() {
    }

    public static Stack<String> checkUsingNamespace(Map<String, String> usingMap, String namespace) {
        return null;
    }

    public static void checkClass(TokenStreamRewriter reWriter, Set<CppClass> classSet, CPP14Parser.ClassspecifierContext ctx) {
        ClassInfoVisitor visitor = new ClassInfoVisitor(reWriter, classSet);
        visitor.visitClassspecifier(ctx);
    }

    /**
     * Converts input ctx to existing code.
     * @param ctx ParserRuleContext Value about c++ code
     * @return String about c++ code
     */
    public static String getText(ParserRuleContext ctx) {
        Interval interval = new Interval(ctx.start.getStartIndex(), ctx.stop.getStopIndex());
        return ctx.start.getInputStream().getText(interval);
    }

    public static CppAccessSpecifier getAccessSpecifier(CPP14Parser.AccessspecifierContext ctx) {
        String accessSpecifier = getText(ctx);
        switch (accessSpecifier) {
            case "private":
                return CppAccessSpecifier.PRIVATE;
            case "protected":
                return CppAccessSpecifier.PROTECTED;
            case "public":
                return CppAccessSpecifier.PUBLIC;
            default:
                return CppAccessSpecifier.DEFAULT;
        }
    }

    public static String getFullName(String name, HashMap<String, String> usingMap) {
        for (String usingName : usingMap.keySet()) {
            name = name.replace(usingName, usingMap.get(usingName));
        }
        return name;
    }
//
//    public static void getFunctionInfo(CPP14Parser.FunctiondefinitionContext ctx) {
//
//    }
//
//    private static void addFunctionInfo(CPP14Parser.FunctiondefinitionContext ctx) {
//        if (currentClass != null) {
//            if (isVirtual) { // superset의 virtual에 없을때만 추가
//                if (currentClass.superSet.stream()
//                        .noneMatch(x -> x.virtualFunctionSet.contains(currentFunction)))
//                    currentClass.virtualFunctionSet.add(currentFunction);
//            } else { //
//                if (!getFunctionName(ctx).replace("~", "").equals(currentClass.className)) {
//                    currentClass.functionMap.computeIfPresent(currentAccessSpecifier, (k,v) -> v).add(ctx);
//                }
//            }
//        }
//    }

    public static String getFunctionName(CPP14Parser.FunctiondefinitionContext ctx) {
        return getText(ctx.declarator()
                .ptrdeclarator())
                .replace("*", "")
                .split("[\\(]")[0];
    }

    public static String getTypeId(CPP14Parser.ThetypeidContext ctx) {
        return ctx.getText().replace("*", "");
    }

    public static Stack<String> makeNameSpaceStack(String namespace) {
        Stack<String> stack = new Stack<>();
        for (String name : namespace.split("::")) {
            stack.push(name);
        }

        return stack;
    }


}
