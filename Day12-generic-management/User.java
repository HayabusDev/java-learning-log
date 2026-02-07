public class User implements Identifiable {
    private final String userId;

    public User(String userId){
        this.userId = userId;
    }

    @Override
    public String getId(){
        return this.userId;
    }

    public String toString(){
        return "ユーザー名: " + userId;
    }
}
