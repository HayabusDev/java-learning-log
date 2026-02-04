public class Main {
    public static void main(String[] args){
        Repository repository = new Repository();
        Service service = new Service(repository);
        Controller controller = new Controller(service);

        controller.launch();
    }
}
