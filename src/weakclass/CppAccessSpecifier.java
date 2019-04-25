package weakclass;

import java.io.Serializable;

public enum CppAccessSpecifier implements Serializable {
    DEFAULT("\n"), PRIVATE("\nprivate:\n"), PROTECTED("\nprotected:\n"), PUBLIC("\npublic:\n");

    private final String name;
    private static final CppAccessSpecifier[] nonPrivates = new CppAccessSpecifier[]{PROTECTED, PUBLIC};
    private static final CppAccessSpecifier[] privates = new CppAccessSpecifier[]{DEFAULT, PRIVATE};

    CppAccessSpecifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static CppAccessSpecifier[] nonPrivate() {
        return nonPrivates;
    }

    public static CppAccessSpecifier[] privates() {
        return privates;
    }

    public static CppAccessSpecifier getEnum(String name) {
        for (CppAccessSpecifier value : CppAccessSpecifier.values()) {
            if (value.getName().replace("\n", "").equals(name))
                return value;
        }
        return DEFAULT;
    }
}
