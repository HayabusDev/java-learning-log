import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Repository {
    private HashMap<String, Words> wordsMap = new HashMap<String, Words>();

    public Repository(){
        wordsMap.put("apple", new Words("apple", "りんご"));
        wordsMap.put("pencil", new Words("pencil", "鉛筆"));
        wordsMap.put("circumstance", new Words("circumstance", "状況"));
        wordsMap.put("region", new Words("region", "地域・地方"));
        wordsMap.put("urban", new Words("urban", "都市"));
    }

    public Words getWordByKey(String wordKey){
        return wordsMap.get(wordKey);
    }

    public Map<String, Words> findAllWords(){
        return Collections.unmodifiableMap(wordsMap);
    }

    public void addToWordMap(String wordKey, Words createdWord){
        if (createdWord == null){
            throw new IllegalStateException("Word must not be null.");
        }

        if (wordKey == null){
            throw new IllegalArgumentException("wordKey must not be null.");
        }

        if (wordsMap.containsKey(wordKey)){
            throw new IllegalStateException("Duplicate wordKey: " + wordKey);
        }

        wordsMap.put(wordKey, createdWord);
    }

    public void removeWord(String wordKey){

        if (wordKey == null){
            throw new IllegalArgumentException("wordKey is null.");
        }

        wordsMap.remove(wordKey);
    }
}
