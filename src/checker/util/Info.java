package checker.util;

import checker.classinfo.ClassInfoVisitor;
import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.TokenStreamRewriter;
import org.antlr.v4.runtime.misc.Interval;
import weakclass.CppAccessSpecifier;
import weakclass.CppClass;

import java.util.*;

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

    public static void checkClass(TokenStreamRewriter reWriter, HashSet<CppClass> classSet, CPP14Parser.ClassspecifierContext ctx) {
        ClassInfoVisitor visitor = new ClassInfoVisitor(reWriter, classSet);
        visitor.visitClassspecifier(ctx);
    }

    public static String getFullName(Stack<String> namespace, String name) {
        StringBuilder sb = new StringBuilder();
        for (String s : namespace) {
            sb.append(s).append("::");
        }
        sb.append(name);
        return sb.toString();
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
    public static String getTempClassNameOfFunction(String className) {
        String[] temp = className.split("::");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length-2; ++i) {
            sb.append(temp[i]).append("::");
        }
        sb.append("_").append(temp[temp.length-2]);
        sb.append("::").append(temp[temp.length-1]);
        return sb.toString();
    }

    public static String getTempClassNameOfClass(String className) {
        String[] temp = className.split("::");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < temp.length-1; ++i) {
            sb.append(temp[i]).append("::");
        }
        sb.append("_").append(temp[temp.length-1]);
        return sb.toString();
    }

    public static String getClassNameOfFunction(String functionName) {
        String[] names = functionName.split("::");
        if (names.length > 1) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < names.length - 2; ++i) {
                sb.append(names[i]).append("::");
            }
            sb.append(names[names.length-2]);
            return sb.toString();
        }
        return null;
    }

    public static String getFunctionNameOfFunction(String functionName) {
        String[] names = functionName.split("::");
        return names[names.length-1];
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

    public static String getFunctionName(ParserRuleContext ctx) {
        return getText(ctx)
                .split("[(]")[0];
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
