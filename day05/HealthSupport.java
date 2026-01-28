import java.util.Scanner;

public class HealthSupport {
    public static void main(String[] args){
        System.out.println("こんにちは！週間健康管理アシスタントです！調子はいかがでしょうか？");

        Scanner sc = new Scanner(System.in);

        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        double[] userSleepTime = new double[7];

        for(int i = 0; i < userSleepTime.length; i++ ){
            System.out.println(days[i] + "の睡眠時間を入力してください！");
            userSleepTime[i] = sc.nextDouble();
            sc.nextLine();
        }

        double maxSleepTime = userSleepTime[0];
        String maxSleepDay = days[0];
        double minSleepTime = userSleepTime[0];
        String minSleepDay = days[0];
        double sleepTimeSum = 0;
        double averageSleepTime;

        for (int i = 0; i < userSleepTime.length; i++){
            if (maxSleepTime < userSleepTime[i]){
                maxSleepTime = userSleepTime[i];
                maxSleepDay = days[i];
            }
        }

        for (int i = 0; i < userSleepTime.length; i++){
            if (minSleepTime > userSleepTime[i]){
                minSleepTime = userSleepTime[i];
                minSleepDay = days[i];
            }
        }

        for (int i = 0; i < userSleepTime.length; i++){
            sleepTimeSum += userSleepTime[i];
        }

        averageSleepTime = sleepTimeSum / userSleepTime.length;

        System.out.println("最大睡眠： " + maxSleepTime + " 時間" + " (" + maxSleepDay + ")");
        System.out.println("最小睡眠： " + minSleepTime + " 時間" + " (" + minSleepDay + ")");
        System.out.println("合計睡眠時間: " + sleepTimeSum + " 時間");
        System.out.println("平均睡眠時間: " + averageSleepTime + " 時間");

        sc.close();
    }
}
