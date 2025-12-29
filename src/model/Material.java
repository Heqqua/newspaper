package model;

import java.util.Objects;

public class Material {
    private String id;
    private String authorPhone;
    private String issueId;
    private String description;
    private MaterialStatus status;

    public Material(String id, String authorPhone, String issueId, String description, MaterialStatus status) {
        this.id = id;
        this.authorPhone = authorPhone;
        this.issueId = issueId;
        this.description = description;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public String getAuthorPhone() {
        return authorPhone;
    }

    public String getIssueId() {
        return issueId;
    }

    public String getDescription() {
        return description;
    }

    public MaterialStatus getStatus() {
        return status;
    }

    public void setAuthorPhone(String authorPhone) {
        this.authorPhone = authorPhone;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStatus(MaterialStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Material material = (Material) o;
        return Objects.equals(id, material.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return id + "; автор: " + authorPhone + "; номер: " + issueId + "; " + description + "; статус: " + status.display();
    }
}
