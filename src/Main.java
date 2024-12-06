import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> spacePositions = new ArrayList<>();

        while (true) {
            System.out.println("Выберите действие:");
            System.out.println("1 - Зашифровать текст 2 - Расшифровать текст");
            int choice = scanner.nextInt();
            scanner.nextLine(); // Пропустить перевод строки

            if (choice == 1) {
                System.out.print("Введите текст для шифрования: ");
                String input = scanner.nextLine();

                System.out.print("Введите числовой ключ: ");
                String keyWord = scanner.nextLine();

                // Сохраняем позиции пробелов
                spacePositions.clear();
                spacePositions.addAll(getSpacePositions(input));
                String inputWithoutSpaces = input.replace(" ", "");

                // Шифруем текст
                String encryptedText = translate(inputWithoutSpaces, true, keyWord);
                System.out.println("Зашифрованный текст: " + encryptedText);
            } else if (choice == 2) {
                System.out.print("Введите текст для расшифровки: ");
                String input = scanner.nextLine();

                System.out.print("Введите числовой ключ: ");
                String keyWord = scanner.nextLine();

                // Расшифровываем текст
                String decryptedText = translate(input, false, keyWord);

                // Восстанавливаем пробелы
                String resultWithSpaces = restoreSpaces(decryptedText, spacePositions);
                System.out.println("Расшифрованный текст: " + resultWithSpaces);
            } else if (choice == 0) {
                System.out.println("Выход из программы.");
                break;
            } else {
                System.out.println("Неверный выбор. Попробуйте снова.");
            }
        }
        scanner.close();
    }

    public static String translate(String text, boolean encode, String keyWord) {
        int columns = keyWord.length();
        int rows = (int) Math.ceil((double) text.length() / columns);
        char[][] table = new char[rows][columns];
        int[] indexes = new int[columns];

        // Получаем порядок столбцов по ключу
        for (int i = 0; i < keyWord.length(); i++) {
            indexes[i] = keyWord.indexOf(String.valueOf(i + 1));
        }

        StringBuilder result = new StringBuilder();

        if (encode) {
            // Заполняем таблицу символами текста построчно
            int count = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (count < text.length()) {
                        table[i][j] = text.charAt(count++);
                    } else {
                        table[i][j] = '\0'; // Пустые ячейки
                    }
                }
            }

            // Печатаем таблицу шифрования
            printTable(table, keyWord);

            // Читаем таблицу по столбцам в порядке индексов
            for (int index : indexes) {
                for (int i = 0; i < rows; i++) {
                    if (table[i][index] != '\0') {
                        result.append(table[i][index]);
                    }
                }
            }
        } else {
            // Определяем количество символов в каждом столбце
            int[] colLengths = new int[columns];
            int fullCols = text.length() % columns == 0 ? columns : text.length() % columns;
            for (int i = 0; i < columns; i++) {
                colLengths[i] = (i < fullCols) ? rows : rows - 1;
            }

            // Заполняем таблицу текстом по столбцам в порядке индексов
            int count = 0;
            for (int index : indexes) {
                for (int i = 0; i < colLengths[index]; i++) {
                    if (count < text.length()) {
                        table[i][index] = text.charAt(count++);
                    }
                }
            }

            // Печатаем таблицу расшифровки
            printTable(table, keyWord);

            // Читаем таблицу построчно
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (table[i][j] != '\0') {
                        result.append(table[i][j]);
                    }
                }
            }
        }

        return result.toString();
    }

    private static void printTable(char[][] table, String keyWord) {
        System.out.println("Таблица:");
        for (char ch : keyWord.toCharArray()) {
            System.out.print(ch + " ");
        }
        System.out.println();

        for (char[] row : table) {
            for (char cell : row) {
                System.out.print((cell == '\0' ? ' ' : cell) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    private static List<Integer> getSpacePositions(String text) {
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == ' ') {
                positions.add(i);
            }
        }
        return positions;
    }

    private static String restoreSpaces(String text, List<Integer> spacePositions) {
        StringBuilder restored = new StringBuilder(text);
        for (int i = 0; i < spacePositions.size(); i++) {
            restored.insert(spacePositions.get(i), " ");
        }
        return restored.toString();
    }
}
