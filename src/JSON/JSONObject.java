package JSON;

public class JSONObject {
    protected static int i;

    public static JSONObject parseJson(String json) {
        char[] jsons = json.toCharArray();

        for (i = 0; i < jsons.length; ++i) {
            if (jsons[i] == '[')
                return JSONArray.parseJson(json.toCharArray());
            else if (jsons[i] == '{')
                return JSONDictionary.parseJson(json.toCharArray());
        }
        return null;
    }
}
