package weakclass;

import java.util.Stack;

public class CppNamespace {
    protected Stack<String> namespaceStack;
    protected String name;

    public CppNamespace(String name) {
        namespaceStack = new Stack<>();
        this.name = name;
    }

    public CppNamespace(String name, Stack<String> namespaceStack) {
        this.namespaceStack = (Stack<String>) namespaceStack.clone();
        this.name = name;
    }

    public String getFullName() {
        StringBuilder sb = new StringBuilder();
        for (String namespace : namespaceStack) {
            sb.append(namespace).append("::");
        }
        sb.append(name);
        return sb.toString();
    }

    public Stack<String> getFullNamespace() {
        Stack<String> clone = (Stack<String>) namespaceStack.clone();
        clone.push(name);
        return clone;
    }

    public String getName() {
        return name;
    }
}
