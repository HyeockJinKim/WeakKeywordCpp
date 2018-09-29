package JSON;

public class JSONString extends JSONObject {
    private StringBuilder jsonString;

    public JSONString() {
        this.jsonString = new StringBuilder();
    }

    public JSONString(String jsonString) {
        this.jsonString = new StringBuilder(jsonString);
    }

    public void add(char ch) {
        jsonString.append(ch);
    }

    @Override
    public int hashCode() {
        return jsonString.length();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONString) {
            return jsonString.equals(((JSONString) obj).jsonString);
        }
        return false;
    }

    @Override
    public String toString() {
        return jsonString.toString();
    }
}
