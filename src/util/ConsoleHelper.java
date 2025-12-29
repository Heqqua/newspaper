package util;

import java.util.Scanner;

public final class ConsoleHelper {
    private static final Scanner SCANNER = new Scanner(System.in);

    private ConsoleHelper() {
    }

    public static String readLine(String prompt) {
        System.out.print(prompt);
        return SCANNER.nextLine().trim();
    }

    public static int readInt(String prompt) {
        while (true) {
            String s = readLine(prompt);
            try {
                return Integer.parseInt(s);
            } catch (NumberFormatException e) {
                System.out.println("Введите корректное число");
            }
        }
    }
}
