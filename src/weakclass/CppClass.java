package weakclass;

import org.antlr.v4.runtime.ParserRuleContext;

import java.util.*;

public class CppClass {
    public String className;
    public Set<CppClass> superSet;
    public Set<CppFunction> virtualFunctionSet; // Map
    public Map<String, ArrayList<ParserRuleContext>> functionMap; // accessspecifier, function

    public CppClass(String className) {
        this.className = className;
        this.virtualFunctionSet = new HashSet<>();
        this.superSet = new HashSet<>();
        this.functionMap = new TreeMap<>();
        this.functionMap.put("\n",new ArrayList<>());
        this.functionMap.put("\nprivate:\n",new ArrayList<>());
        this.functionMap.put("\npublic:\n",new ArrayList<>());
        this.functionMap.put("\nprotected:\n",new ArrayList<>());
    }

    private boolean hasVirtualFunction(CppFunction cppFunction) {
        for (CppFunction virtual : virtualFunctionSet) {
            if (virtual.equals(cppFunction)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVirtual() {
        for (CppFunction virtual : virtualFunctionSet) {
            boolean isVirtual = true;
            for (CppClass superClass : superSet) {
                if (superClass.hasVirtualFunction(virtual)) {
                    isVirtual = false;
                    break;
                }
            }
            if (isVirtual) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CppClass) {
            return className.equals(((CppClass) obj).className);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(className).append("\nsuper:\n");
        for (CppClass superClass : superSet) {
            sb.append(superClass.toString());
        }
        sb.append("virtual:\n");
        for (CppFunction cppFunction : virtualFunctionSet) {
            sb.append(cppFunction.toString());
        }
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return className.length();
    }
}
