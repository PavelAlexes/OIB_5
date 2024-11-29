import java.util.Arrays;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        while (true) {

            Scanner scanner = new Scanner(System.in);

            System.out.println("Выберите действие: 1 - Зашифровать, 2 - Расшифровать");
            int choice = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Введите числовой ключ: ");
            String keyWord = scanner.nextLine();

            if (choice == 1) {
                System.out.print("Введите текст для шифрования: ");
                String input = scanner.nextLine();
                String encryptedText = translate(input, keyWord, true);
                System.out.println("Зашифрованный текст: " + encryptedText);
            } else if (choice == 2) {
                System.out.print("Введите текст для расшифровки: ");
                String input = scanner.nextLine();
                String decryptedText = translate(input, keyWord, false);
                System.out.println("Расшифрованный текст: " + decryptedText);
            } else {
                System.out.println("Неверный выбор!");
            }
        }
    }

    public static String translate(String text, String keyWord, boolean encode) {
        int columns = keyWord.length();
        int rows = (int) Math.ceil((double) text.length() / columns);
        char[][] table = new char[rows][columns];

        StringBuilder result = new StringBuilder();

        // Индексы перестановки из числового ключа
        int[] indexes = getKeyOrder(keyWord);

        if (encode) {
            // Заполняем таблицу символами текста
            int count = 0;
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    if (count < text.length()) {
                        table[i][j] = text.charAt(count++);
                    } else {
                        table[i][j] = ' '; // Пустые ячейки заполняем пробелами
                    }
                }
            }

            printTable(table, keyWord);

            // Считываем таблицу по столбцам в порядке ключа
            for (int index : indexes) {
                for (int i = 0; i < rows; i++) {
//                    System.out.println(i + " " + index + " " + table[i][index]);
                    result.append(table[i][index]);
                }
            }
        } else {
            // Заполняем таблицу зашифрованным текстом по столбцам
            int count = 0;
            for (int index : indexes) {
                for (int i = 0; i < rows; i++) {
                    if (count < text.length()) {
                        table[i][index] = text.charAt(count++);
                    }
                }
            }

            printTable(table, keyWord);

            // Считываем таблицу построчно
            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < columns; j++) {
                    result.append(table[i][j]);
                }
            }
        }
//        for (int i = 0; i < result.length(); i++) {
//            if(result.charAt(i) == ' '){
//                result.deleteCharAt(i);
//            }
//        }
//        System.out.println(result);
        return result.toString().trim();
    }

    private static int[] getKeyOrder(String keyWord) {
        // Преобразуем ключ в массив чисел
        Integer[] keyArray = new Integer[keyWord.length()];
        for (int i = 0; i < keyWord.length(); i++) {
            keyArray[i] = Character.getNumericValue(keyWord.charAt(i));
        }

        // Копируем и сортируем ключ
        Integer[] sortedKeyArray = keyArray.clone();
        Arrays.sort(sortedKeyArray);

        // Определяем порядок индексов
        int[] order = new int[keyWord.length()];
        for (int i = 0; i < keyWord.length(); i++) {
            int index = Arrays.asList(sortedKeyArray).indexOf(keyArray[i]);
            order[index] = i;
            sortedKeyArray[index] = null; // Убираем обработанный индекс
        }

        return order;
    }

    private static void printTable(char[][] table, String keyWord) {
        // Выводим ключ
        for (char ch : keyWord.toCharArray()) {
            System.out.print(ch + " ");
        }
        System.out.println();

        // Выводим таблицу
        for (char[] row : table) {
            for (char cell : row) {
                System.out.print((cell == '\0' ? ' ' : cell) + " ");
            }
            System.out.println();
        }
        System.out.println();
    }
}
