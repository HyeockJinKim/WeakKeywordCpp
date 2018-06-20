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

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CppClass) {
            return className.equals(((CppClass) obj).className);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return className.length();
    }
}
