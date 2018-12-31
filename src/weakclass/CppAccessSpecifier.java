package weakclass;

public enum CppAccessSpecifier {
    PRIVATE("\nprivate:\n"), PROTECTED("\nprotected:\n"), PUBLIC("\npublic:\n");

    private final String name;

    CppAccessSpecifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
