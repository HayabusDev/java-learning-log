import java.text.Normalizer;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class InputUtil {
    static Scanner sc = new Scanner(System.in);

    private InputUtil(){};

    //入力管理
    public static String readNonEmptyString(String question){
        String userInput;

        while (true){
            System.out.println(question);
            userInput = sc.nextLine().trim();
            if (userInput.isBlank()){
                System.out.println("入力が空です。");
            }else {
                break;
            }
        }
        return userInput;
    }

    public static boolean confirmYesNo(String question){
        String userInput;

        while (true){
            System.out.println(question);
            userInput = sc.nextLine().trim();

            String normalizedUserInput = Normalizer.normalize(userInput, Normalizer.Form.NFKC);
            String finalizedUserInput = normalizedUserInput.toUpperCase();

            //下記2種は毎回new クラス定数化を検討
            String[] yChoices = {"Y", "YES", "はい"};
            String[] nChoices = {"N", "NO", "いいえ"};

            for (String yChoice : yChoices) {
                if (finalizedUserInput.equals(yChoice)) {
                    return true;
                }
            }

            for (String nChoice : nChoices){
                if (finalizedUserInput.equals(nChoice)){
                    return false;
                }
            }

            System.out.println("y か n で入力してください。");
        }
    }

    public static int readIntInRange(String question, int min, int max){
        String userInput;

        while (true){
            System.out.println(question);
            userInput = sc.nextLine().trim();
            String normalizedUserInput = Normalizer.normalize(userInput, Normalizer.Form.NFKC);
            try{
                int convertedToInt = Integer.parseInt(normalizedUserInput);
                if (convertedToInt < min || max < convertedToInt){
                    System.out.println("入力値が範囲外です。");
                }else {
                    return convertedToInt;
                }
            }catch (NumberFormatException e){
                System.out.println("入力値が不正です。");
            }
        }
    }

    public static LocalDate readDateWithinRange(LocalDate min, LocalDate max){

        if (min == null || max == null || min.isAfter(max)) {
            throw new IllegalArgumentException("Invalid date range.");
        }

        while (true){
            int minYear = min.getYear();
            int maxYear = max.getYear();

            int year = readIntInRange("年を入力してください。", minYear, maxYear);

            int minMonth;
            int maxMonth;

            if (year == minYear) {
                minMonth = min.getMonthValue();
            } else {
                minMonth = 1;
            }

            if (year == maxYear) {
                maxMonth = max.getMonthValue();
            } else {
                maxMonth = 12;
            }

            int month = readIntInRange("月を入力してください。", minMonth, maxMonth);

            YearMonth yearMonth = YearMonth.of(year, month);
            int maxDayOfMonth = yearMonth.lengthOfMonth();

            int minDay = 1;
            int maxDay = maxDayOfMonth;

            if (year == minYear && month == min.getMonthValue()) {
                minDay = min.getDayOfMonth();
            }

            if (year == maxYear && month == max.getMonthValue()) {
                maxDay = max.getDayOfMonth();
            }

            int day = readIntInRange("日を入力してください。", minDay, maxDay);

            LocalDate selectedDate = LocalDate.of(year, month, day);

            if (!selectedDate.isBefore(min) && !selectedDate.isAfter(max)) {
                return selectedDate;
            }

            System.out.println("指定された範囲外の日付です。再入力してください。");
        }
    }
}
