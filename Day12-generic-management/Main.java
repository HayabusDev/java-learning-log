public class Main {
    public static void main(String[] args){
        //Task実装
        InMemoryRepository<Task> taskInMemoryRepository = new InMemoryRepository<>();
        Service<Task> taskService = new Service<>(taskInMemoryRepository);

        //User実装
        InMemoryRepository<User> userInMemoryRepository = new InMemoryRepository<>();
        Service<User> userService = new Service<>(userInMemoryRepository);

        DataFactory dataFactory = new DataFactory();

        Controller controller = new Controller(taskService, userService, dataFactory);

        controller.launch();
    }
}
