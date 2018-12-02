package processing;

import JSON.JSONArray;
import JSON.JSONDictionary;
import JSON.JSONObject;
import weakclass.CppClass;
import weakclass.CppFunction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ProcessJson {

    private static Set<CppClass> readSuperSet(JSONArray json) {
        Set<CppClass> superSet = new HashSet<>();
        int len = json.size();
        for (int i = 0; i < len; ++i) {
            JSONDictionary dict = (JSONDictionary) json.get(i);
            CppClass cppClass = new CppClass(dict.get("name").toString());
            cppClass.virtualFunctionSet = readVirtualFunction((JSONArray) dict.get("virtual"));
            superSet.add(cppClass);
        }

        return superSet;
    }

    private static Set<CppFunction> readVirtualFunction(JSONArray json) {
        Set<CppFunction> virtualFunctionSet = new HashSet<>();
        int len = json.size();
        for (int i = 0; i < len; ++i) {
            JSONDictionary dict = (JSONDictionary) json.get(i);
            CppFunction virtualFunction = new CppFunction();
            virtualFunction.functionName = dict.get("name").toString();

            ArrayList<String> virtualParams = new ArrayList<>();
            JSONArray params = (JSONArray) dict.get("params");
            int paramLen = params.size();
            for (int j = 0; j < paramLen; ++j) {
                virtualParams.add(params.get(j).toString());
            }
            virtualFunction.functionParameter = virtualParams;
            virtualFunctionSet.add(virtualFunction);
        }

        return virtualFunctionSet;
    }

    static Set<CppClass> readJson(String json) {
        JSONArray classSets = (JSONArray) JSONObject.parseJson(json);
        if (classSets == null) {
            return new HashSet<>();
        }

        int size = classSets.size();
        Set<CppClass> set = new HashSet<>();
        for (int i = 0; i < size; ++i) {
            JSONDictionary dict = (JSONDictionary) classSets.get(i);
            JSONDictionary classDict = (JSONDictionary) dict.get("class");
            CppClass cppClass = new CppClass(dict.get("name").toString());
            cppClass.superSet = readSuperSet((JSONArray) classDict.get("super"));
            cppClass.virtualFunctionSet = readVirtualFunction((JSONArray) classDict.get("virtual"));
            set.add(cppClass);
        }

        return set;
    }

    private static JSONArray superClassParsing(Set<CppClass> SuperClassSet) {
        JSONArray superSet = new JSONArray();
        for (CppClass superClass : SuperClassSet) {
            JSONDictionary superDict = new JSONDictionary();
            superDict.put("name", superClass.className);
            superDict.put("virtual", virtualFunctionParsing(superClass.virtualFunctionSet));
            superSet.add(superDict);
        }

        return superSet;
    }

    private static JSONArray virtualFunctionParsing(Set<CppFunction> virtualFunctionSet) {
        JSONArray virtual = new JSONArray();
        for (CppFunction virtualFunction : virtualFunctionSet) {
            JSONDictionary function = new JSONDictionary();
            JSONArray params = new JSONArray();
            for (String param : virtualFunction.functionParameter) {
                params.add(param);
            }
            function.put("name", virtualFunction.functionName);
            function.put("params", params);
            virtual.add(function);
        }

        return virtual;
    }

    static String jsonifyClassSet(Set<CppClass> classSet) {
        StringBuilder sb = new StringBuilder();
        JSONArray classesArr = new JSONArray();
        for (CppClass cppClass : classSet) {
            JSONDictionary classInfo = new JSONDictionary();

            classInfo.put("super", superClassParsing(cppClass.superSet));
            classInfo.put("virtual", virtualFunctionParsing(cppClass.virtualFunctionSet));

            JSONDictionary classDict = new JSONDictionary();
            classDict.put("name", cppClass.className);
            classDict.put("class", classInfo);
            classesArr.add(classDict);
        }

        sb.append(classesArr.toString());
        return sb.toString();
    }
}
