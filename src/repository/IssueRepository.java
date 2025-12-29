package repository;

import model.Issue;
import util.FileUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IssueRepository {
    private final List<Issue> issues;
    private final String filename = "issues.txt";

    public IssueRepository() {
        this.issues = load();
    }

    private List<Issue> load() {
        List<String> lines = FileUtil.readAllLines(filename);
        List<Issue> result = new ArrayList<>();
        for (String line : lines) {
            if (line.trim().isEmpty()) continue;
            String[] parts = line.split(";");
            if (parts.length < 3) continue;
            String id = parts[0].trim();
            String desc = parts[1].trim();
            boolean released = Boolean.parseBoolean(parts[2].trim());
            result.add(new Issue(id, desc, released));
        }
        return result;
    }

    private void persist() {
        List<String> lines = issues.stream()
                .map(i -> String.join(";", i.getId(), i.getDescription(), Boolean.toString(i.isReleasedToTypesetting())))
                .collect(Collectors.toList());
        FileUtil.writeAllLines(filename, lines);
    }

    public List<Issue> findAll() {
        return new ArrayList<>(issues);
    }

    public Optional<Issue> findById(String id) {
        return issues.stream().filter(i -> i.getId().equals(id)).findFirst();
    }

    public void save(Issue issue) {
        findById(issue.getId()).ifPresentOrElse(existing -> {
            existing.setDescription(issue.getDescription());
            existing.setReleasedToTypesetting(issue.isReleasedToTypesetting());
        }, () -> issues.add(issue));
        persist();
    }
}
