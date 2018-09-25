package JSON;

import java.util.ArrayList;

public class JSONArray extends JSONObject {
    private ArrayList<String> jsonArray;

    public JSONArray() {
        jsonArray = new ArrayList<>();
    }

    public void add(JSONDictionary json) {
        jsonArray.add(json.toString());
    }

    public void add(JSONArray json) {
        jsonArray.add(json.toString());
    }

    public void add(String value) {
        jsonArray.add(value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int len = jsonArray.size();
        for (int i = 0; i < len-1; ++i) {
            sb.append(jsonArray.get(i)).append(",");
        }
        if (len > 0)
            sb.append(jsonArray.get(len-1));
        sb.append("]");
        return sb.toString();
    }
}
