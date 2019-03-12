package classinfo;

import weakclass.CppClass;
import weakclass.CppFunction;

import java.util.HashSet;
import java.util.Set;

public class Serializer {
    private Serializer() {
    }

    private static void serializeFunction(StringBuilder sb, CppFunction cppFunction) {
        sb.append("-\n")
                .append(cppFunction.getName())
                .append("\n");
        sb.append(":\n")
                .append(cppFunction.getAccessSpecifier())
                .append("\n");
        sb.append("@\n");
        for (String parameter : cppFunction.getParameters()) {
            sb.append(parameter)
                    .append(",");
        }
        sb.append("\n");
    }

    /**
     *
     * Mean # is className
     * Mean - is virtual Function's Name
     * Mean : is virtual Function's accessSpecifier
     * Mean @ is virtual function's param
     */
    private static void serializeClass(StringBuilder sb, CppClass cppClass) {
        sb.append("#\n")
                .append(cppClass.getName())
                .append("\n");
        sb.append("*\n")
                .append(cppClass.getNamespace())
                .append("\n");
        Set<CppFunction> virtualFunctionSet = cppClass.getAllVirtualFunctionSet();
        for (CppFunction virtual : virtualFunctionSet) {
            serializeFunction(sb, virtual);
        }
    }

    public static String serializeClassSet(HashSet<CppClass> classSet) {
        StringBuilder sb = new StringBuilder();
        for (CppClass cppClass : classSet) {
            serializeClass(sb, cppClass);
        }
        return sb.toString();
    }
}
