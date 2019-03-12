package classinfo;

import weakclass.CppAccessSpecifier;
import weakclass.CppClass;
import weakclass.CppFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.StringTokenizer;

public class ClassReader {
    private int i;
    private List<String> infoList;

    public ClassReader(List<String> infoList) {
        this.infoList = infoList;
    }

    public HashSet<CppClass> readClassSet() {
        HashSet<CppClass> set = new HashSet<>();
        for (i = 0; i < infoList.size();) {
            if (infoList.get(i).equals("#")) {
                ++i;
                set.add(readClass());
            }
        }
        return set;
    }

    private CppClass readClass() {
        CppClass cppClass = new CppClass(infoList.get(i), infoList.get(i+2));
        i += 3;
        for (; i < infoList.size();) {
            switch (infoList.get(i)) {
                case "-":
                    ++i;
                    cppClass.updateFunction(readFunction());
                    break;
                default:
                    ++i;
            }
        }
        return cppClass;
    }

    private ArrayList<String> readArgs(String args) {
        StringTokenizer tokenizer = new StringTokenizer(args);
        ArrayList<String> list = new ArrayList<>();
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }
        return list;
    }

    private CppFunction readFunction() {
        String name = infoList.get(i);
        System.out.println(name);
        i += 2;
        String accessSpecifier = infoList.get(i++);
        CppFunction function = new CppFunction(CppAccessSpecifier.getEnum(accessSpecifier));
        function.setName(name);
        function.setVirtual();
        for (; i < infoList.size();) {
            switch (infoList.get(i)) {
                case "@":
                    ++i;
                    function.setParameters(readArgs(infoList.get(i)));
                    ++i;
                    break;
                default:
                    ++i;
                    return function;
            }
        }

        return function;
    }
}
