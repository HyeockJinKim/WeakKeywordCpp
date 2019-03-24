package weakclass;

import java.io.Serializable;

public enum CppAccessSpecifier implements Serializable {
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

    public static CppAccessSpecifier getEnum(String name) {
        for (CppAccessSpecifier value : CppAccessSpecifier.values()) {
            if (value.getName().replace("\n", "").equals(name))
                return value;
        }
        return DEFAULT;
    }
}
