package weakclass;

import checker.util.Info;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class CppFunction extends CppMember {
    private String className;
    private ArrayList<String> parameters;
    private String name;

    private boolean isConstructor;
    private boolean isVirtual;

    public CppFunction(CppAccessSpecifier accessSpecifier) {
        super(accessSpecifier);
        this.name = "";
        this.parameters = new ArrayList<>();
        this.content = null;
    }


    void setClassName(String className) {
        if (className.equals(name))
            this.isConstructor = true;
        this.className = className;
    }

    public String getClassName() {
        return className;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object cppFunction) {
        if (cppFunction instanceof  CppFunction) {
            return name.equals(((CppFunction) cppFunction).name)
                    && parameters.equals(((CppFunction) cppFunction).parameters);
        }
        return false;
    }

    public void setVirtual() {
        isVirtual = true;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public boolean isNoStatic() {
        return isVirtual || isConstructor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("function Name: ")
                .append(name)
                .append(" (");
        for (String param : parameters) {
            sb.append(param).append(", ");
        }
        sb.append(")\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return name.length() * 31 + parameters.size();
    }
}
