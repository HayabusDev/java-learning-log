import java.util.Scanner;

public class GradeJudge {
    static void main(String[] args){
        System.out.println("成績判定ツールへようこそ。");

        Scanner sc = new Scanner(System.in);
        int score;
        char grade;
        String comment;

        while(true){
            System.out.println("点数を入力してください。");
            try {
                score = sc.nextInt();
            }catch (Exception e){
                System.out.println("エラー: 数値を入力してください。");
                sc.nextLine();
                continue;
            }
            if (score > 100 || score < 0)
            {
                System.out.println("エラー： 0~100 の値を入力してください。");
            }else
            {
                break;
            }
        }

        if (score >= 90){
            grade = 'A';
            comment = "よく頑張りました。";
        }else if (score >= 80){
            grade = 'B';
            comment = "次回以降も頑張りましょう。";
        }else if (score >= 70) {
            grade = 'C';
            comment = "もっと出来るはずですよ。";
        }else if (score >= 60) {
            grade = 'D';
            comment = "もっと頑張りましょう。";
        }else{
            grade = 'F';
            comment = "再試験です。";
        }

        System.out.println("あなたの成績は: " + grade + "\n" + comment);

    }

}
