package weakclass;

public enum CppAccessSpecifier {
    DEFAULT("\n"), PRIVATE("\nprivate:\n"), PROTECTED("\nprotected:\n"), PUBLIC("\npublic:\n");

    private final String name;
    private static final CppAccessSpecifier[] nonPrivates = new CppAccessSpecifier[]{DEFAULT, PROTECTED, PUBLIC};

    CppAccessSpecifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CppAccessSpecifier[] nonPrivate() {
        return nonPrivates;
    }

}
