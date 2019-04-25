package weakclass;

import grammar.antlr.CPP14Parser;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.ArrayList;

public class CppFunction extends CppMember {
    private String className;
    private ArrayList<String> parameters;
    private ArrayList<String> paramNames;
    private String name;

    private boolean isConstructor;
    private boolean isDestructor;
    private boolean isVirtual;
    private transient CPP14Parser.MeminitializeridContext memInitializer;
    private transient CPP14Parser.MemberdeclarationContext memberDeclaration;

    public CppFunction(CppAccessSpecifier accessSpecifier) {
        super(accessSpecifier);
        this.name = "";
        this.parameters = new ArrayList<>();
        this.paramNames = new ArrayList<>();
        this.content = null;
        this.memInitializer = null;
        this.memberDeclaration = null;
    }


    void setClassName(String className, String name) {
        if (this.name.equals(name))
            this.isConstructor = true;

        this.className = className;
    }

    String getClassName() {
        return className;
    }

    public void setName(String name) {
        if (name.contains("~"))
            this.isDestructor = true;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getParams() {
        StringBuilder sb = new StringBuilder("(");
        int len = parameters.size()-1;
        System.out.println(parameters);
        System.out.println(paramNames);
        for (int i = 0; i < len; ++i) {
            sb.append(parameters.get(i))
                    .append(" ")
                    .append(paramNames.get(i))
                    .append(", ");
        }
        sb.append(parameters.get(len))
                .append(" ")
                .append(paramNames.get(len))
                .append(")");
        return sb.toString();
    }


    @Override
    public void setContent(CPP14Parser.MemberdeclarationContext content) {
        super.setContent(content);
        this.memberDeclaration = content;
    }

    public CPP14Parser.MemberdeclarationContext getMemberDeclaration() {
        return this.memberDeclaration;
    }

    public void setMemInitializer(CPP14Parser.MeminitializeridContext memInitializer) {
        this.memInitializer = memInitializer;
    }

    public CPP14Parser.MeminitializeridContext getMemInitializer() {
        return memInitializer;
    }

    public ArrayList<String> getParameters() {
        return parameters;
    }

    boolean equals(String name, ArrayList<String> params) {
        return this.name.equals(name) && this.parameters.equals(params);
    }

    static CppFunction makeConstructor(String className, CppFunction cppFunction) {
        CppFunction constructor = new CppFunction(cppFunction.accessSpecifier);
        StringBuilder sb = new StringBuilder();
        sb.append("    _")
                .append(className)
                .append(cppFunction.getParams())
                .append(" : ")
                .append(cppFunction.name)
                .append("(");
        int len = cppFunction.paramNames.size()-1;
        for (int i = 0; i < len; ++i) {
            sb.append(cppFunction.paramNames.get(i))
                    .append(", ");
        }
        sb.append(cppFunction.paramNames.get(len))
                .append(") {}\n");
        constructor.setContent(sb.toString());
        return constructor;
    }

    public void setParameters(ArrayList<String> parameters) {
        this.parameters = parameters;
    }

    public void setParamNames(ArrayList<String> paramNames) {
        this.paramNames = paramNames;
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

    public boolean isConstructor() {
        return isConstructor;
    }

    public boolean isVirtual() {
        return isVirtual;
    }

    public boolean isPrivate() {
        return accessSpecifier == CppAccessSpecifier.PRIVATE;
    }

    public boolean isNoPrivate() {
        return accessSpecifier == CppAccessSpecifier.PUBLIC || accessSpecifier == CppAccessSpecifier.PROTECTED;
    }

    boolean isNoStatic() {
        return isVirtual || isConstructor || isDestructor;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("memberDeclaration Name: ")
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
