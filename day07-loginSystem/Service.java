public class Service {
    private final Repository repository;

    public Service (Repository repository){
        this.repository = repository;
    };

    public boolean loginUser(String inputId, String inputPassword)
    {
        User userInfo = repository.getUserInfo(inputId);

        if (userInfo == null){
            return false;
        }else {
            String correctPassword = userInfo.getUserPassword();
            return correctPassword.equals(inputPassword);
        }
    }

    public boolean registerUser(String inputId, String inputPassword){
        User userInfo = repository.getUserInfo(inputId);

        if (userInfo == null){
            repository.createNewUser(inputId, inputPassword);
            return true;
        }
        return false;
    }

    public boolean isAvailableUserId(String inputId){
        return repository.getUserInfo(inputId) == null;
    }
}
