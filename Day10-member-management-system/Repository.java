import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Repository {

    private HashMap<String, User> userMap = new HashMap<String, User>();

    public Repository(){
        userMap.put("admin", new User("admin"));
        userMap.put("Tanaka", new User("Tanaka"));
        userMap.put("Shun", new User("Shun"));
    }

    public User getUserByKey(String userKey){
        return userMap.get(userKey);
    }

    public Map<String, User> findAllUser(){
        return Collections.unmodifiableMap(userMap);
    }

    public void addToUserMap(String userId, User createdUser){
        if (createdUser == null){
            throw new IllegalStateException("User must not be null.");
        }

        if (userId == null){
            throw new IllegalArgumentException("UserId must not be null.");
        }

        if (userMap.containsKey(userId)){
            throw new IllegalStateException("Duplicate userId: " + userId);
        }

        userMap.put(userId, createdUser);
    }

    public boolean removeUserMap(String userKey){
        if (userKey == null){
            throw new IllegalArgumentException("UserKey is null.");
        }
        return userMap.remove(userKey) != null;
    }
}
