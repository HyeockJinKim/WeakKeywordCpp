package weakclass;

import java.util.ArrayList;

public class CppFunction {
    public String functionName;
    public ArrayList<String> functionParameter;

    public CppFunction() {
        functionParameter = new ArrayList<>();
    }

    @Override
    public boolean equals(Object cppFunction) {
        if (cppFunction instanceof  CppFunction) {
            return functionName.equals(((CppFunction) cppFunction).functionName)
                    && functionParameter.equals(((CppFunction) cppFunction).functionParameter);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function Name: ")
                .append(functionName)
                .append(" (");
        for (String param : functionParameter) {
            sb.append(param).append(", ");
        }
        sb.append(")\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return functionName.length() * 31 + functionParameter.size();
    }
}
