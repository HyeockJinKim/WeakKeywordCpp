package JSON;

import java.util.HashMap;

public class JSONDictionary extends JSONObject {
    private HashMap<String, String> jsonDictionary;

    public JSONDictionary() {
        jsonDictionary = new HashMap<>();
    }

    public void put(JSONObject key, JSONObject value) {
        jsonDictionary.put(key.toString(), value.toString());
    }

    public void put(JSONObject json, String value) {
        jsonDictionary.put(json.toString(), value);
    }

    public void put(String key, JSONObject json) {
        jsonDictionary.put(key, json.toString());
    }

    public void put(String key, String value) {
        jsonDictionary.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        String[] kv = new String[2];
        kv[0] = null;
        kv[1] = null;
        jsonDictionary.forEach((x, y) -> {
            if (kv[0] != null) {
                sb.append(kv[0])
                    .append(":")
                    .append(kv[1])
                    .append(",");
            }
            kv[0] = x;
            kv[1] = y;
        });
        if (kv[0] != null) {
            sb.append(kv[0])
                .append(":")
                .append(kv[1]);
        }
        sb.append("}");
        return sb.toString();
    }
}