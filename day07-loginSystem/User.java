public class User {
    private final String userId;
    private final String userPassword;

    public User (String userId, String userPassword){
        this.userId = userId;
        this.userPassword = userPassword;
    }

    public String getUserId(){return userId;}

    public String getUserPassword() {return userPassword;}
}
