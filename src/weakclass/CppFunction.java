package weakclass;

import checker.util.Info;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class CppFunction {
    private ArrayList<String> parameters;
    private CppAccessSpecifier accessSpecifier;
    private ParserRuleContext content;
    private String name;

    private boolean isVirtual;

    public CppFunction(CppAccessSpecifier accessSpecifier) {
        this.name = null;
        this.parameters = new ArrayList<>();
        this.accessSpecifier = accessSpecifier;
        this.content = null;
    }

    public CppAccessSpecifier getAccessSpecifier() {
        return accessSpecifier;
    }

    public String getContent() {
        return Info.getText(content) + "\n";
    }

    public ParserRuleContext getContext() {
        return content;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public void setContent(ParserRuleContext content) {
        this.content = content;
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
