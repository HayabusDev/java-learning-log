public class DataFactory {

    public Task createTask(String taskId){
        return new Task(taskId);
    }

    public User createUser(String userId){
        return new User(userId);
    }
}
