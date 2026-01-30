import java.util.HashMap;

public class Repository {

    private HashMap <String, User> userInfo = new HashMap<String, User>();

    public Repository(){
        userInfo.put("admin", new User("admin", "abc1234"));
        userInfo.put("root" , new User("root", "0000"));
        userInfo.put("Kenji", new User("Kenji", "Kenji1234"));
    }

    public User getUserInfo(String userKey)
    {
            return userInfo.get(userKey);
    }

    public void createNewUser(String inputId, String inputPassword){
        User user = new User(inputId, inputPassword);
        userInfo.put(inputId, user);
    }
}
