package app;

import exception.NotFoundException;
import exception.ValidationException;
import model.Author;
import model.Issue;
import model.Material;
import service.CoordinationService;
import repository.AuthorRepository;
import repository.IssueRepository;
import repository.MaterialRepository;
import util.ConsoleHelper;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        AuthorRepository authorRepo = new AuthorRepository();
        IssueRepository issueRepo = new IssueRepository();
        MaterialRepository materialRepo = new MaterialRepository();
        CoordinationService service = new CoordinationService(authorRepo, issueRepo, materialRepo);

        while (true) {
            printMenu();
            int option = ConsoleHelper.readInt("Выберите пункт: ");
            try {
                switch (option) {
                    case 0 -> {
                        System.out.println("Выход");
                        return;
                    }
                    case 1 -> addWorker(service);
                    case 2 -> editWorker(service);
                    case 3 -> addIssue(service);
                    case 4 -> releaseIssue(service);
                    case 5 -> addMaterial(service);
                    case 6 -> changeMaterialStatus(service);
                    case 7 -> findFreeAuthors(service);
                    case 8 -> findMaterialsByStatus(service);
                    case 9 -> findPrintedMaterialsByAuthor(service);
                    case 10 -> printAllData(service);
                    default -> System.out.println("Неверный пункт меню");
                }
            } catch (ValidationException | NotFoundException e) {
                System.out.println("Ошибка: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("Произошла непредвиденная ошибка: " + e.getMessage());
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("=== СИСТЕМА КООРДИНАЦИИ ВЫПУСКА ГАЗЕТЫ ===");
        System.out.println("0 - Выход");
        System.out.println("1 - Добавить работника");
        System.out.println("2 - Изменить данные работника");
        System.out.println("3 - Добавить номер газеты");
        System.out.println("4 - Сдать номер в набор");
        System.out.println("5 - Добавить материал");
        System.out.println("6 - Изменить статус материала");
        System.out.println("7 - Поиск свободных авторов по специальности");
        System.out.println("8 - Поиск материалов по статусу");
        System.out.println("9 - Поиск отпечатанных материалов автора");
        System.out.println("10 - Показать все данные");
    }

    private static void addWorker(CoordinationService service) {
        String surname = ConsoleHelper.readLine("Фамилия: ");
        String specialty = ConsoleHelper.readLine("Специальность (журналист/фотограф): ");
        String phone = ConsoleHelper.readLine("Телефон: ");
        service.addAuthor(surname, specialty, phone);
        System.out.println("Работник добавлен");
    }

    private static void editWorker(CoordinationService service) {
        String phone = ConsoleHelper.readLine("Телефон работника для изменения: ");
        String surname = ConsoleHelper.readLine("Новая фамилия (Enter чтобы пропустить): ");
        String specialty = ConsoleHelper.readLine("Новая специальность (журналист/фотограф) (Enter чтобы пропустить): ");
        service.updateAuthor(phone, surname.isBlank() ? null : surname, specialty.isBlank() ? null : specialty);
        System.out.println("Данные работника обновлены");
    }

    private static void addIssue(CoordinationService service) {
        String id = ConsoleHelper.readLine("Идентификатор номера: ");
        String desc = ConsoleHelper.readLine("Описание: ");
        service.addIssue(id, desc);
        System.out.println("Номер добавлен");
    }

    private static void releaseIssue(CoordinationService service) {
        String id = ConsoleHelper.readLine("Идентификатор номера для сдачи в набор: ");
        service.releaseIssueToTypesetting(id);
        System.out.println("Номер успешно сдан в набор");
    }

    private static void addMaterial(CoordinationService service) {
        String id = ConsoleHelper.readLine("Идентификатор материала: ");
        String authorPhone = ConsoleHelper.readLine("Телефон автора: ");
        String issueId = ConsoleHelper.readLine("Идентификатор номера: ");
        String desc = ConsoleHelper.readLine("Описание материала: ");
        service.addMaterial(id, authorPhone, issueId, desc);
        System.out.println("Материал добавлен в статусе 'готовиться'");
    }

    private static void changeMaterialStatus(CoordinationService service) {
        String id = ConsoleHelper.readLine("Идентификатор материала: ");
        System.out.println("Возможные статусы: готовиться, сдан редактору, корректура, готов");
        String status = ConsoleHelper.readLine("Новый статус: ");
        service.changeMaterialStatus(id, status);
        System.out.println("Статус материала обновлён");
    }

    private static void findFreeAuthors(CoordinationService service) {
        String specialty = ConsoleHelper.readLine("Специальность (журналист/фотограф): ");
        List<Author> list = service.findFreeAuthorsBySpecialty(specialty);
        if (list.isEmpty()) {
            System.out.println("Свободных авторов данной специальности не найдено");
        } else {
            System.out.println("Свободные авторы:");
            list.forEach(a -> System.out.println(a.toString()));
        }
    }

    private static void findMaterialsByStatus(CoordinationService service) {
        System.out.println("Возможные статусы: готовиться, сдан редактору, корректура, готов");
        String status = ConsoleHelper.readLine("Статус поиска: ");
        List<Material> list = service.findMaterialsByStatus(status);
        if (list.isEmpty()) {
            System.out.println("Материалов с таким статусом не найдено");
        } else {
            System.out.println("Материалы:");
            list.forEach(m -> System.out.println(m.toString()));
        }
    }

    private static void findPrintedMaterialsByAuthor(CoordinationService service) {
        String phone = ConsoleHelper.readLine("Телефон автора: ");
        List<Material> list = service.findPrintedMaterialsByAuthor(phone);
        if (list.isEmpty()) {
            System.out.println("Отпечатанных материалов данного автора не найдено");
        } else {
            System.out.println("Отпечатанные материалы:");
            list.forEach(m -> System.out.println(m.toString()));
        }
    }

    private static void printAllData(CoordinationService service) {
        System.out.println("Авторы:");
        service.allAuthors().forEach(a -> System.out.println(a.toString()));
        System.out.println();
        System.out.println("Номера:");
        service.allIssues().forEach(i -> System.out.println(i.toString()));
        System.out.println();
        System.out.println("Материалы:");
        service.allMaterials().forEach(m -> System.out.println(m.toString()));
    }
}
