package model;

import java.util.Objects;

public class Author {
    private String surname;
    private AuthorSpecialty specialty;
    private String phone;

    public Author(String surname, AuthorSpecialty specialty, String phone) {
        this.surname = surname;
        this.specialty = specialty;
        this.phone = phone;
    }

    public String getSurname() {
        return surname;
    }

    public AuthorSpecialty getSpecialty() {
        return specialty;
    }

    public String getPhone() {
        return phone;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSpecialty(AuthorSpecialty specialty) {
        this.specialty = specialty;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return Objects.equals(phone, author.phone);
    }

    @Override
    public int hashCode() {
        return Objects.hash(phone);
    }

    @Override
    public String toString() {
        return surname + " (" + specialty.display() + "), тел.: " + phone;
    }
}
