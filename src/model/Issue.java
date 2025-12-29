package model;

import java.util.Objects;

public class Issue {
    private String id;
    private String description;
    private boolean releasedToTypesetting;

    public Issue(String id, String description, boolean releasedToTypesetting) {
        this.id = id;
        this.description = description;
        this.releasedToTypesetting = releasedToTypesetting;
    }

    public String getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public boolean isReleasedToTypesetting() {
        return releasedToTypesetting;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReleasedToTypesetting(boolean releasedToTypesetting) {
        this.releasedToTypesetting = releasedToTypesetting;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Issue issue = (Issue) o;
        return Objects.equals(id, issue.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + "; " + description + "; сдан в набор: " + (releasedToTypesetting ? "да" : "нет");
    }
}
