package util;

import exception.DataLoadException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.List;

public final class FileUtil {
    private FileUtil() {
    }

    public static Path pathInData(String filename) {
        return Paths.get("src", "data", filename);
    }

    public static List<String> readAllLines(String filename) {
        Path p = pathInData(filename);
        try {
            if (Files.notExists(p)) {
                Files.createDirectories(p.getParent());
                Files.createFile(p);
            }
            return Files.readAllLines(p, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new DataLoadException("Ошибка при чтении файла " + filename, e);
        }
    }

    public static void writeAllLines(String filename, List<String> lines) {
        Path p = pathInData(filename);
        try {
            if (Files.notExists(p.getParent())) {
                Files.createDirectories(p.getParent());
            }
            Files.write(p, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException e) {
            throw new DataLoadException("Ошибка при записи файла " + filename, e);
        }
    }
}
