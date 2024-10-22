import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;


public class Main {
    public static int winCount;
    public static int loseCount;

    public static Scanner scanner = new Scanner(System.in);
    public static ArrayList<String> words = new ArrayList<>();
    public static int errorCount = 0;
    public static ArrayList<Character> letterList = new ArrayList<>();
    public static File file = new File("E:\\JavaLearning\\ViselicaByKDA\\src\\WinLoseBase.txt");

    public static void main(String[] args) {
        statisticLoad();
        System.out.println("Вы хотите начать новую игру? Введите да, чтобы начать, или другой символ для выхода");

        boolean gameStart = gameStart();
        boolean gameRestart = gameRestartIfLose(errorCount);
        if (gameStart || gameRestart) {
            gamelogic();
        }


    }

    public static void statisticLoad() {
        try (Scanner scanner = new Scanner(file)) {
            ArrayList<Integer> statisticList = new ArrayList<>();
            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                statisticList.add(Integer.parseInt(line));
            }
            winCount = statisticList.get(0);
            loseCount = statisticList.get(1);
            System.out.println("Статистика предыдущих игр: \n Побед: " + winCount + "\n Поражений: " + loseCount);

        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден " + file.getPath());
            e.printStackTrace();
        }
    }

    public static void statisticWriteToFile() {
        try (PrintWriter pw = new PrintWriter(file)) {
            pw.println(winCount);
            pw.println(loseCount);
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден, не могу записать статистику: " + file.getPath());
            e.printStackTrace();
        }
    }

    public static void gamelogic() {

        takeWordFromFile();
        takeRandomWord();
        String word = takeRandomWord();

        ArrayList<Character> wordCharList = wordCharCreation(word);
        System.out.println();
        makeMaskOnWord(wordCharList);
        System.out.println();

        StringBuilder resultWord = new StringBuilder(word.length());
        resultWord.append("*".repeat(word.length()));

        while (!((resultWord.toString()).equals(word)) || errorCount < 6) {
            listOfUsedLetters(letterList);
            char letter = Character.toLowerCase(letterInput());
            if (letterList.contains(letter)) {
                System.out.println("Эта буква уже вводилась, попробуйте другую.");
                continue;
            }
            ArrayList<Integer> compareInputCharacter = compareInputCharacter(letter, wordCharList);

            letterCheck(letter, wordCharList);
            if (!(letterList.contains(letter)) && !(wordCharList.contains(letter))) {
                hangmanPrint(errorCount);
            }
            pringWord(wordCharList, letter, compareInputCharacter, resultWord);
            System.out.println();

            letterList.add(letter);
            if (errorCount < 6) {
                System.out.println(resultWord);
            } else if (errorCount == 6) {
                System.out.println("Было загаданно слово " + word);
            }
            if (winCheck(resultWord, word)) {
                break;
            }
            if (errorCount == 6) {
                break;
            }
        }
        if ((resultWord.toString()).equals(word)) {
            gameRestartIfWin(resultWord, word);
        } else {
            gameRestartIfLose(errorCount);
            errorCount = 0;
            letterList.clear();
            gamelogic();
        }

    }

    public static boolean winCheck(StringBuilder x, String word) {
        if ((x.toString()).equals(word)) {
            System.out.println("Поздравляю, вы победили");
            winCount++;
            return true;
        } else {
            return false;
        }
    }

    public static void gameRestartIfWin(StringBuilder x, String word) {
        if (x.toString().equals(word)) {
            System.out.println("Хотите попробовать еще раз? Введите да, чтобы начать, или другой символ для выхода");

            String y = scanner.next();

            if ((y).equalsIgnoreCase("да")) {
                System.out.println("Игра начинается, загадано слово:");
                errorCount = 0; // Сброс счетчика ошибок
                letterList.clear(); // Очистка списка введенных букв
                gamelogic();
            } else {
                System.out.println("Спасибо за игру");
                statisticWriteToFile();
                System.exit(0);
            }
        }

    }

    public static boolean gameStart() {

        String Y = scanner.next();
        if ((Y).equalsIgnoreCase("да")) {
            System.out.println("Игра начинается, загадано слово:");
            return true;
        } else {
            System.out.println("Спасибо за игру");
            statisticWriteToFile();
            System.exit(0);
            return false;
        }

    }


    public static void takeWordFromFile() {
        try {
            File file = new File("E:\\JavaLearning\\ViselicaByKDA\\src\\Words.txt");
            Scanner fileScanner = new Scanner(file);
            while (fileScanner.hasNext()) {
                String word = fileScanner.next();

                words.add(word.toLowerCase());
            }
            fileScanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Файл не найден: " + e.getMessage());
        }
    }

    public static String takeRandomWord() {
        Random random = new Random();
        int randomIndexFromCollection = random.nextInt(words.size());
        return words.get(randomIndexFromCollection);
    }

    public static ArrayList<Character> wordCharCreation(String x) {
        ArrayList<Character> wordChar = new ArrayList<>();
        for (int i = 0; i < x.length(); i++) {
            wordChar.add(x.charAt(i));

        }
        return wordChar;
    }

    public static char letterInput() {

        return scanner.next().charAt(0);

    }
    public static void listOfUsedLetters(ArrayList<Character> letterList){
        if (letterList.isEmpty()) {

        } else {
            System.out.println("Список использованных букв: ");
            for (Character letter : letterList) {
                System.out.print(letter + ", ");
            }
            System.out.println();
        }
    }

    public static void letterCheck(char letter, ArrayList<Character> wordCharList) {
        if (letterList.contains(letter)) {
            System.out.println("Такая буква уже вводилась, введите новую букву");
        } else if (!(wordCharList.contains(letter))) {
            errorCount++;
        }
    }

    public static void makeMaskOnWord(ArrayList<Character> wordCharList) {
        for (int i = 0; i < wordCharList.size(); i++) {
            System.out.print("*");
        }
    }

    public static ArrayList<Integer> compareInputCharacter(char letter, ArrayList<Character> wordCharList) {
        ArrayList<Integer> listofIndexes = new ArrayList<>();
        for (int i = 0; i < wordCharList.size(); i++) {
            if (wordCharList.get(i) == letter) {
                listofIndexes.add(i);
            }
        }
        return listofIndexes;
    }

    public static void pringWord(ArrayList<Character> wordCharList, char letter, ArrayList<Integer> compareInputCharacter, StringBuilder resultWord) {
        for (int i = 0; i < wordCharList.size(); i++) {
            if (compareInputCharacter.contains(i)) {
                resultWord.setCharAt(i, letter);
            }
        }
    }

    public static void hangmanPrint(int errorCount) {
        if (errorCount == 1) {
            System.out.println();
            System.out.println("Совершена первая ошибка, у вас осталось 5 жизней");
            System.out.println("     -----\n     |   |\n     |\n     |\n     |\n     |\n    _|_");


        } else if (errorCount == 2) {
            System.out.println();
            System.out.println("Совершена вторая ошибка, у вас осталось 4 жизни");
            System.out.println("     -----\n     |   |\n     |   O\n     |\n     |\n     |\n    _|_");

        } else if (errorCount == 3) {
            System.out.println();
            System.out.println("Совершена третья ошибка, у вас осталось 3 жизни");
            System.out.println("     -----\n     |   |\n     |   O\n     |   |\n     |\n     |\n    _|_");

        } else if (errorCount == 4) {
            System.out.println();
            System.out.println("Совершена четвертая ошибка, у вас осталось 2 жизни");
            System.out.println("     -----\n     |   |\n     |   O\n     |  /|\n     |\n     |\n    _|_");

        } else if (errorCount == 5) {
            System.out.println();
            System.out.println("Совершена пятая ошибка, у вас осталось 1 жизни");
            System.out.println("     -----\n     |   |\n     |   O\n     |  /|\\\n     |\n     |\n    _|_");

        } else if (errorCount == 6) {
            System.out.println();
            System.out.println("Совершена шестая ошибка, Вы проиграли");
            System.out.println("     -----\n     |   |\n     |   O\n     |  /|\\\n     |  / \\\n     |\n    _|_");
            loseCount++;


        }
    }

    public static boolean gameRestartIfLose(int errorCount) {
        if (errorCount == 6) {
            System.out.println("Хотите попробовать еще раз? Введите да, чтобы начать, или любой другой символ для выхода");

            String y = scanner.next();

            if ((y).equalsIgnoreCase("да")) {
                System.out.println("Игра начинается, загадано слово:");
                return true;
            } else {
                System.out.println("Спасибо за игру");
                statisticWriteToFile();
                System.exit(0);
                return false;
            }
        } else {
            return false;
        }

    }

}




