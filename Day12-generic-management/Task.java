public class Task implements Identifiable {
    private final String taskId;

    public Task(String taskId){
        this.taskId = taskId;
    }

    @Override
    public String getId(){
        return this.taskId;
    }

    @Override
    public String toString(){
        return "タスク名: " + taskId;
    }
}
