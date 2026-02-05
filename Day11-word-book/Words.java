public class Words {
    //編集機能実装するなら、finalにしない
    //setter作成
    private String word;
    private String meaning;

    public Words (String word, String meaning){
        this.word = word;
        this.meaning = meaning;
    }


    public String getWord() {
        return word;
    }

    public String getMeaning() {
        return meaning;
    }

    @Override
    public String toString(){
        return "[" + word + "]" + ": " + meaning;
    }
}
