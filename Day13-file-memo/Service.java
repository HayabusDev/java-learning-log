public class Service {

    private final Storage storage;

    public Service(Storage storage){
        this.storage =storage;
    }

    public boolean writeMemo(String text){
        if (text == null) {
            return false;
        }

        storage.saveMemo(text);
        return true;
    }

    public String readMemo(){
        return storage.loadMemo();
    }
}
