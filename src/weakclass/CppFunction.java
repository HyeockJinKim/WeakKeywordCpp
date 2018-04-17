package weakclass;

import java.util.ArrayList;

public class CppFunction {
    public String functionName;
    public ArrayList<String> functionParameter;

    CppFunction() {
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
    public int hashCode() {
        return functionName.length() * 31 + functionParameter.size();
    }
}
