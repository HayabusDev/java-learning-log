import java.io.*;
import java.io.FileNotFoundException;

public class Storage {

    private String filePath = "D:\\プログラミング\\projectFile\\memo.txt";

    public void saveMemo(String text){
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(filePath));
            bufferedWriter.write(text);
            bufferedWriter.close();

        }catch (FileNotFoundException e){
            System.out.println("Could not locate file location.");

        }catch (IOException ex){
            ex.printStackTrace();
        }
    }

    //上書き仕様、追加仕様ではない
    public String loadMemo(){

        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
            String data;
            String allData = "";
            while ((data = bufferedReader.readLine()) != null){
                if (allData.isEmpty()){
                    allData = data;
                }else {
                    //String は 不変（immutable）→　+= をするたびに新しい String を作り直す
                    //回数分、メモリとコピーが発生 → 重くなる可能性
                    //パフォーマンス向上 → 「可変文字列」系クラス
                    allData += "\n" + data;
                }
            }

            bufferedReader.close();
            return allData;

        }catch (FileNotFoundException e){
            System.out.println("Could not locate file.");

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;
    }
}
