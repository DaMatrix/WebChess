package net.daporkchop.webchess.common.util.locale;

public enum Locale {
    EN_US("English (US)"),
    DE_DE("Deutsch")
    ;

    public final String displayName;

    Locale(String displayName) {
        assert displayName != null;

        this.displayName = displayName;
    }
}
