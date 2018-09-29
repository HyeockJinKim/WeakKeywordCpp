package JSON;

import java.util.ArrayList;

public class JSONArray extends JSONObject {
    private ArrayList<JSONObject> jsonArray;

    public JSONArray() {
        jsonArray = new ArrayList<>();
    }

    public void add(JSONObject json) {
        jsonArray.add(json);
    }

    public void add(String value) {
        jsonArray.add(new JSONString(value));
    }

    public int size() {
        return jsonArray.size();
    }

    public JSONObject get(int index) {
        return jsonArray.get(index);
    }

    static JSONArray parseJson(char[] json) {
        JSONArray array = new JSONArray();
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
                case ',':
                    array.add(curObject);
                    curObject = null;
                    break;
                case ']':
                    if (curObject != null)
                        array.add(curObject);

                    return array;
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
        return jsonArray.size();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONArray) {
            return jsonArray.equals(((JSONArray) obj).jsonArray);
        }
        return false;
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        int len = jsonArray.size();
        for (int i = 0; i < len-1; ++i) {
            sb.append(jsonArray.get(i).toString()).append(",");
        }
        if (len > 0)
            sb.append(jsonArray.get(len-1).toString());
        sb.append("]");
        return sb.toString();
    }
}
