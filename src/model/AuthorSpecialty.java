package model;

public enum AuthorSpecialty {
    JOURNALIST("журналист"),
    PHOTOGRAPHER("фотограф");

    private final String display;

    AuthorSpecialty(String display) {
        this.display = display;
    }

    public String display() {
        return display;
    }

    public static AuthorSpecialty fromString(String s) {
        String normalized = s == null ? "" : s.trim().toLowerCase();
        switch (normalized) {
            case "журналист":
                return JOURNALIST;
            case "фотограф":
                return PHOTOGRAPHER;
            default:
                throw new IllegalArgumentException("Неизвестная специальность: " + s);
        }
    }
}
