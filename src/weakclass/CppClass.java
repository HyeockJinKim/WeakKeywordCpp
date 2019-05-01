package weakclass;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CppClass extends CppNamespace {
    private HashSet<CppFunction> functionSet;
    private HashSet<CppFunction> constructorSet;
    private HashSet<CppMember> memberSet;
    private HashSet<CppClass> superSet;
    private long numOfSuperVirtualFunction;

    public CppClass(String className) {
        super(className);
        this.functionSet = new HashSet<>();
        this.constructorSet = new HashSet<>();
        this.memberSet = new HashSet<>();
        this.superSet = new HashSet<>();
        this.numOfSuperVirtualFunction = 0;
    }

    public CppClass(String className, Stack<String> namespace) {
        super(className, namespace);
        this.functionSet = new HashSet<>();
        this.constructorSet = new HashSet<>();
        this.memberSet = new HashSet<>();
        this.superSet = new HashSet<>();
    }

    public CppClass(String className, String namespace) {
        super(className, namespace);
        this.functionSet = new HashSet<>();
        this.constructorSet = new HashSet<>();
        this.memberSet = new HashSet<>();
        this.superSet = new HashSet<>();
    }

    public String getTempClassName() {
        return namespace.toString()+"_"+this.name;
    }

    public void addSuperSet(CppClass superClass) {
        superSet.add(superClass);
    }

    public void clearMemberSet() {
        memberSet = null;
    }

    public Set<CppFunction> getFunctionSet(CppAccessSpecifier accessSpecifier) {
        return functionSet.stream()
                .filter(x -> x.accessSpecifier.equals(accessSpecifier))
                .filter(x -> !x.isNoStatic())
                .collect(Collectors.toSet());
    }

    public Set<CppMember> getMemberSet(CppAccessSpecifier accessSpecifier) {
        return memberSet.stream()
                .filter(x -> x.accessSpecifier.equals(accessSpecifier))
                .collect(Collectors.toSet());
    }

    public Set<CppFunction> getVirtualFunctionSet(CppAccessSpecifier accessSpecifier) {
        return functionSet.stream()
                .filter(CppFunction::isNoStatic)
                .filter(this::isMyFunction)
                .filter(x -> x.accessSpecifier.equals(accessSpecifier))
                .collect(Collectors.toSet());
    }

    public Set<CppFunction> getAllVirtualFunctionSet() {
        return functionSet.stream()
                .filter(CppFunction::isNoStatic)
                .collect(Collectors.toSet());
    }

    public Set<CppFunction> getOnlyVirtualFunctionSet() {
        return functionSet.stream()
                .filter(CppFunction::isOnlyVirtual)
                .collect(Collectors.toSet());
    }

    public void inheritSuperVirtualFunction() {
        for (CppClass cppClass : superSet) {
            cppClass.functionSet.stream()
                    .filter(CppFunction::isVirtual)
                    .forEach(this.functionSet::add);

            cppClass.functionSet.stream()
                    .filter(x -> !x.isVirtual())
                    .filter(CppFunction::isNoPrivate)
                    .filter(CppFunction::isConstructor)
                    .map(x -> CppFunction.makeConstructor(name, x))
                    .forEach(this.constructorSet::add);
        }
        numOfSuperVirtualFunction = numOfVirtualFunction();
    }

    public void makeConstructor() {
        functionSet.addAll(constructorSet);
    }

    public Optional<CppFunction> findFunction(String functionName, ArrayList<String> params) {
        return functionSet.stream()
                .filter(x -> x.equals(functionName, params))
                .findAny();
    }

    private boolean isMyFunction(CppFunction function) {
        return function.getClassName().equals(getFullName());
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
        function.setClassName(getFullName(), name);
        functionSet.stream()
                .filter(x -> x.equals(function))
                .findAny()
                .ifPresent(x -> {
                    function.setVirtual();
                    functionSet.remove(function);
                });
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

    public boolean equals(String name) {
        return getFullName().equals(name);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getFullName()).append("\nsuper:\n");
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

    public Stream<CppFunction> linkConstructor() {
        return functionSet.stream()
                .filter(CppFunction::isConstructor)
                .filter(x -> !x.isVirtual())
                .filter(x -> x.getMemInitializer() != null);
    }

    @Override
    public int hashCode() {
        return (int) (name.length()*31 + numOfVirtualFunction() * 29);
    }
}
