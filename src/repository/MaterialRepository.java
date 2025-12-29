package repository;

import model.Material;
import model.MaterialStatus;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MaterialRepository {
    private final List<Material> materials;
    private final String filename = "materials.txt";

    public MaterialRepository() {
        this.materials = load();
    }

    private List<Material> load() {
        List<String> lines = FileUtil.readAllLines(filename);
        List<Material> result = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 5) continue;
            String id = parts[0].trim();
            String authorPhone = parts[1].trim();
            String issueId = parts[2].trim();
            String desc = parts[3].trim();
            MaterialStatus status = MaterialStatus.fromString(parts[4].trim().toLowerCase());
            result.add(new Material(id, authorPhone, issueId, desc, status));
        }
        return result;
    }

    private void persist() {
        List<String> lines = materials.stream()
                .map(m -> String.join(";", m.getId(), m.getAuthorPhone(), m.getIssueId(), m.getDescription(), m.getStatus().display()))
                .collect(Collectors.toList());
        FileUtil.writeAllLines(filename, lines);
    }

    public List<Material> findAll() {
        return new ArrayList<>(materials);
    }

    public Optional<Material> findById(String id) {
        return materials.stream().filter(m -> m.getId().equals(id)).findFirst();
    }

    public void save(Material material) {
        findById(material.getId()).ifPresentOrElse(existing -> {
            existing.setAuthorPhone(material.getAuthorPhone());
            existing.setIssueId(material.getIssueId());
            existing.setDescription(material.getDescription());
            existing.setStatus(material.getStatus());
        }, () -> materials.add(material));
        persist();
    }

    public List<Material> findByAuthorPhone(String phone) {
        return materials.stream().filter(m -> m.getAuthorPhone().equals(phone)).collect(Collectors.toList());
    }

    public List<Material> findByIssueId(String issueId) {
        return materials.stream().filter(m -> m.getIssueId().equals(issueId)).collect(Collectors.toList());
    }

    public List<Material> findByStatus(MaterialStatus status) {
        return materials.stream().filter(m -> m.getStatus() == status).collect(Collectors.toList());
    }
}
