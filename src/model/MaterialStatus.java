package model;

public enum MaterialStatus {
    PREPARING("готовиться"),
    SUBMITTED_TO_EDITOR("сдан редактору"),
    PROOFREADING("корректура"),
    READY("готов");

    private final String display;

    MaterialStatus(String display) {
        this.display = display;
    }

    public String display() {
        return display;
    }

    public static MaterialStatus fromString(String s) {
        String normalized = s == null ? "" : s.trim().toLowerCase();
        switch (normalized) {
            case "готовиться":
                return PREPARING;
            case "сдан редактору":
                return SUBMITTED_TO_EDITOR;
            case "корректура":
                return PROOFREADING;
            case "готов":
                return READY;
            default:
                throw new IllegalArgumentException("Неизвестный статус материала: " + s);
        }
    }
}
