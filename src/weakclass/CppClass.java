package weakclass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class CppClass {
    public String className;
    public String baseClause;
    public boolean isWeak;
    public Map<CppClass, Boolean> superClassMap;// Map Weak, Nweak
    public Map<CppFunction, CppClass> virtualFunctionMap; // Map
    public Map<String, ArrayList<CppFunction>> functionMap; // accessspecifier, function

    public CppClass(String className) {
        this.className = className;
        isWeak = false;
        this.superClassMap = new HashMap<>();
        this.virtualFunctionMap = new HashMap<>();
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
