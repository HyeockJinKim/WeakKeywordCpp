package weakclass;

import java.io.Serializable;
import java.util.Stack;

public class CppNamespace implements Serializable {
    protected StringBuilder namespace;
    protected String name;

    public CppNamespace(String name) {
        namespace = new StringBuilder();
        this.name = name;
    }

    public CppNamespace(String name, Stack<String> namespaceStack) {
        this.namespace = new StringBuilder();
        for (String s : namespaceStack) {
            namespace.append(s).append("::");
        }
        this.name = name;
    }

    public CppNamespace(String name, String namespace) {
        this.namespace = new StringBuilder();
        this.namespace.append(namespace);
        this.name = name;
    }

    public String getFullName() {
        return String.valueOf(namespace) + name;
    }

    public String getName() {
        return name;
    }

    public String getNamespace() {
        return namespace.toString();
    }
}
