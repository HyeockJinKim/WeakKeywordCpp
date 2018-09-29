package JSON;

import java.util.HashMap;
import java.util.Set;

public class JSONDictionary extends JSONObject {
    private HashMap<JSONObject, JSONObject> jsonDictionary;

    public JSONDictionary() {
        jsonDictionary = new HashMap<>();
    }

    public void put(JSONObject key, JSONObject value) {
        jsonDictionary.put(key, value);
    }

    public void put(JSONObject json, String value) {
        jsonDictionary.put(json, new JSONString(value));
    }

    public void put(String key, JSONObject json) {
        jsonDictionary.put(new JSONString(key), json);
    }

    public void put(String key, String value) {
        jsonDictionary.put(new JSONString(key), new JSONString(value));
    }

    public Set<JSONObject> keys() {
        return jsonDictionary.keySet();
    }

    public JSONObject get(String key) {
        return jsonDictionary.get(new JSONString(key));
    }

    static JSONDictionary parseJson(char[] json) {
        JSONDictionary dictionary = new JSONDictionary();
        JSONObject key = null;
        JSONObject curObject = null;
        for (; i < json.length;) {
            ++i;
            switch (json[i]) {
                case '[':
                    curObject = JSONArray.parseJson(json);
                    break;
                case '{':
                    curObject = JSONDictionary.parseJson(json);
                    break;
                case ':':
                    key = curObject;
                    curObject = null;
                    break;
                case ',':
                    dictionary.put(key, curObject);
                    curObject = null;
                    break;
                case '}':
                    if (key != null)
                        dictionary.put(key, curObject);

                    return dictionary;
                default:
                    if (curObject == null) {
                        curObject = new JSONString();
                    }
                    if (curObject instanceof JSONString)
                        ((JSONString) curObject).add(json[i]);
                    break;
            }
        }
        System.out.println("Parsing Problem !");
        return null;
    }

    @Override
    public int hashCode() {
        return jsonDictionary.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONDictionary) {
            return jsonDictionary.equals(((JSONDictionary) obj).jsonDictionary);
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        JSONObject[] kv = new JSONObject[2];
        kv[0] = null;
        kv[1] = null;
        jsonDictionary.forEach((x, y) -> {
            if (kv[0] != null) {
                sb.append(kv[0].toString())
                    .append(":")
                    .append(kv[1].toString())
                    .append(",");
            }
            kv[0] = x;
            kv[1] = y;
        });
        if (kv[0] != null) {
            sb.append(kv[0].toString())
                .append(":")
                .append(kv[1].toString());
        }
        sb.append("}");
        return sb.toString();
    }
}