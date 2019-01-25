package weakclass;

import java.util.*;
import java.util.stream.Collectors;

public class CppClass extends CppNamespace {
    private Set<CppFunction> functionSet;
    private Set<CppMember> memberSet;
    private Set<CppClass> superSet;
    private long numOfSuperVirtualFunction;

    public CppClass(String className) {
        super(className);
        this.functionSet = new HashSet<>();
        this.memberSet = new HashSet<>();
        this.superSet = new HashSet<>();
        this.numOfSuperVirtualFunction = 0;
    }

    public CppClass(String className, Stack<String> namespace) {
        super(className, namespace);
        this.functionSet = new HashSet<>();
        this.memberSet = new HashSet<>();
        this.superSet = new HashSet<>();
    }

    public void addSuperSet(CppClass superClass) {
        superSet.add(superClass);
    }

    public Set<CppFunction> getFunctionSet(CppAccessSpecifier accessSpecifier) {
        return functionSet.stream()
                .filter(x -> !x.isVirtual())
                .filter(x -> x.accessSpecifier.equals(accessSpecifier))
                .collect(Collectors.toSet());
    }

    public Set<CppMember> getMemberSet(CppAccessSpecifier accessSpecifier) {
        return memberSet.stream()
                .filter(x -> x.accessSpecifier.equals(accessSpecifier))
                .collect(Collectors.toSet());
    }

    public Set<CppFunction> getVirtualFunctionSet(CppAccessSpecifier accessSpecifier) {
        return functionSet.stream()
                .filter(CppFunction::isVirtual)
                .filter(x -> x.accessSpecifier.equals(accessSpecifier))
                .collect(Collectors.toSet());
    }

    public void inheritSuperVirtualFunction() {
        for (CppClass cppClass : superSet) {
            cppClass.functionSet.stream()
                    .filter(CppFunction::isVirtual)
                    .forEach(this::updateFunction);
        }
        numOfSuperVirtualFunction = numOfVirtualFunction();
    }

    public boolean isWeak() {
        return !superSet.isEmpty() && numOfSuperVirtualFunction != numOfVirtualFunction();
    }

    private long numOfVirtualFunction() {
        return functionSet.stream()
                .filter(CppFunction::isVirtual)
                .count();
    }

    public void updateFunction(CppFunction function) {
        functionSet.remove(function);
        functionSet.add(function);
    }

    public void updateMember(CppMember member) {
        memberSet.remove(member);
        memberSet.add(member);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CppClass) {
            return getFullName().equals(((CppClass) obj).getFullName());
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(name).append("\nsuper:\n");
        for (CppClass superClass : superSet) {
            sb.append(superClass.toString());
        }
        sb.append("virtual:\n");
        functionSet.stream()
                .filter(CppFunction::isVirtual)
                .forEach(x -> sb.append(x.toString()));
        sb.append("\n");
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return (int) (name.length()*31 + numOfVirtualFunction() * 29);
    }
}
