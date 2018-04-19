package weakclass;

import java.util.*;

public class CppClass {
    public String className;
    public Set<CppFunction> virtualFunctionSet; // Map
    public Map<String, StringBuilder> functionMap; // accessspecifier, function

    public CppClass(String className) {
        this.className = className;
        this.virtualFunctionSet = new HashSet<>();
        this.functionMap = new TreeMap<>();
        this.functionMap.put("\n",new StringBuilder());
        this.functionMap.put("\nprivate:\n",new StringBuilder());
        this.functionMap.put("\npublic:\n",new StringBuilder());
        this.functionMap.put("\nprotected:\n",new StringBuilder());
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
