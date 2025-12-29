package repository;

import model.Author;
import model.AuthorSpecialty;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class AuthorRepository {
    private final List<Author> authors;
    private final String filename = "authors.txt";

    public AuthorRepository() {
        this.authors = load();
    }

    private List<Author> load() {
        List<String> lines = FileUtil.readAllLines(filename);
        List<Author> result = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 3) continue;
            String surname = parts[0].trim();
            AuthorSpecialty specialty = AuthorSpecialty.fromString(parts[1].trim().toLowerCase());
            String phone = parts[2].trim();
            result.add(new Author(surname, specialty, phone));
        }
        return result;
    }

    private void persist() {
        List<String> lines = authors.stream()
                .map(a -> String.join(";", a.getSurname(), a.getSpecialty().display(), a.getPhone()))
                .collect(Collectors.toList());
        FileUtil.writeAllLines(filename, lines);
    }

    public List<Author> findAll() {
        return new ArrayList<>(authors);
    }

    public Optional<Author> findByPhone(String phone) {
        return authors.stream().filter(a -> a.getPhone().equals(phone)).findFirst();
    }

    public void save(Author author) {
        findByPhone(author.getPhone()).ifPresentOrElse(existing -> {
            existing.setSurname(author.getSurname());
            existing.setSpecialty(author.getSpecialty());
        }, () -> authors.add(author));
        persist();
    }

    public void deleteByPhone(String phone) {
        authors.removeIf(a -> a.getPhone().equals(phone));
        persist();
    }
}
