package service;

import exception.NotFoundException;
import exception.ValidationException;
import model.*;
import repository.AuthorRepository;
import repository.IssueRepository;
import repository.MaterialRepository;

import java.util.List;
import java.util.stream.Collectors;

public class CoordinationService {
    private final AuthorRepository authorRepository;
    private final IssueRepository issueRepository;
    private final MaterialRepository materialRepository;

    public CoordinationService(AuthorRepository authorRepository, IssueRepository issueRepository, MaterialRepository materialRepository) {
        this.authorRepository = authorRepository;
        this.issueRepository = issueRepository;
        this.materialRepository = materialRepository;
    }

    public void addAuthor(String surname, String specialtyStr, String phone) {
        AuthorSpecialty specialty = AuthorSpecialty.fromString(specialtyStr);
        if (surname == null || surname.isBlank()) {
            throw new ValidationException("Фамилия не может быть пустой");
        }
        if (phone == null || phone.isBlank()) {
            throw new ValidationException("Телефон не может быть пустым");
        }
        authorRepository.save(new Author(surname.trim(), specialty, phone.trim()));
    }

    public void updateAuthor(String phone, String newSurname, String newSpecialty) {
        Author author = authorRepository.findByPhone(phone).orElseThrow(() -> new NotFoundException("Автор не найден"));
        if (newSurname != null && !newSurname.isBlank()) {
            author.setSurname(newSurname.trim());
        }
        if (newSpecialty != null && !newSpecialty.isBlank()) {
            author.setSpecialty(AuthorSpecialty.fromString(newSpecialty));
        }
        authorRepository.save(author);
    }

    public void addIssue(String id, String description) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Идентификатор номера не может быть пустым");
        }
        issueRepository.save(new Issue(id.trim(), description == null ? "" : description.trim(), false));
    }

    public void releaseIssueToTypesetting(String issueId) {
        Issue issue = issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("Номер не найден"));
        List<Material> materials = materialRepository.findByIssueId(issueId);
        if (materials.isEmpty()) {
            throw new ValidationException("В номере нет материалов. Нельзя сдавать номер в набор.");
        }
        boolean allReady = materials.stream().allMatch(m -> m.getStatus() == MaterialStatus.READY);
        if (!allReady) {
            throw new ValidationException("Нельзя сдать номер в набор — не все материалы готовы");
        }
        issue.setReleasedToTypesetting(true);
        issueRepository.save(issue);
    }

    public void addMaterial(String id, String authorPhone, String issueId, String description) {
        if (id == null || id.isBlank()) {
            throw new ValidationException("Идентификатор материала не может быть пустым");
        }
        authorRepository.findByPhone(authorPhone).orElseThrow(() -> new NotFoundException("Автор не найден"));
        issueRepository.findById(issueId).orElseThrow(() -> new NotFoundException("Номер не найден"));
        List<Material> authorPreparing = materialRepository.findByAuthorPhone(authorPhone).stream()
                .filter(m -> m.getStatus() == MaterialStatus.PREPARING)
                .collect(Collectors.toList());
        if (!authorPreparing.isEmpty()) {
            throw new ValidationException("У автора уже есть материал в состоянии 'готовиться'");
        }
        materialRepository.save(new Material(id.trim(), authorPhone, issueId, description == null ? "" : description.trim(), MaterialStatus.PREPARING));
    }

    public void changeMaterialStatus(String materialId, String statusStr) {
        Material material = materialRepository.findById(materialId).orElseThrow(() -> new NotFoundException("Материал не найден"));
        MaterialStatus status = MaterialStatus.fromString(statusStr);
        material.setStatus(status);
        materialRepository.save(material);
    }

    public List<Author> findFreeAuthorsBySpecialty(String specialtyStr) {
        AuthorSpecialty specialty = AuthorSpecialty.fromString(specialtyStr);
        List<Author> all = authorRepository.findAll();
        return all.stream()
                .filter(a -> a.getSpecialty() == specialty)
                .filter(a -> materialRepository.findByAuthorPhone(a.getPhone()).stream().noneMatch(m -> m.getStatus() == MaterialStatus.PREPARING))
                .collect(Collectors.toList());
    }

    public List<Material> findMaterialsByStatus(String statusStr) {
        MaterialStatus status = MaterialStatus.fromString(statusStr);
        return materialRepository.findByStatus(status);
    }

    public List<Material> findPrintedMaterialsByAuthor(String authorPhone) {
        authorRepository.findByPhone(authorPhone).orElseThrow(() -> new NotFoundException("Автор не найден"));
        List<Material> byAuthor = materialRepository.findByAuthorPhone(authorPhone);
        return byAuthor.stream()
                .filter(m -> {
                    return issueRepository.findById(m.getIssueId()).map(Issue::isReleasedToTypesetting).orElse(false);
                })
                .collect(Collectors.toList());
    }

    public List<Author> allAuthors() {
        return authorRepository.findAll();
    }

    public List<Issue> allIssues() {
        return issueRepository.findAll();
    }

    public List<Material> allMaterials() {
        return materialRepository.findAll();
    }
}
