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
        return jsonString.toString().length();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof JSONString) {
            return jsonString.toString().equals(((JSONString) obj).jsonString.toString());
        }
        return false;
    }

    @Override
    public String toString() {
        return jsonString.toString();
    }
}
