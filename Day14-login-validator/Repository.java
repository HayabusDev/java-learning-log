import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Repository {

    private HashMap<String, User> userMap = new HashMap<String, User>();

    public Repository(){}

    public User getUser(String userId){
        return userMap.get(userId);
    }

    public Map<String ,User> findAllUsers(){
        return Collections.unmodifiableMap(userMap);
    }

    public void addUserToMap(String userId, User createdUser){
        if (createdUser == null){
            throw new IllegalArgumentException("User must not be null.");
        }

        if (userId == null){
            throw new IllegalArgumentException("userId must not be null.");
        }

        //ServiceでDATA_DUPLICATE を Result.failure で返してるが・・・
        if (userMap.containsKey(userId)){
            throw new IllegalStateException("Duplicate userId: " + userId);
        }

        userMap.put(userId, createdUser);
    }
}
