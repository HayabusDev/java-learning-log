public class Main {
    public static void main(String[] args){
        Repository repository = new Repository();
        Validator validator = new Validator();
        Service service = new Service(repository, validator);
        Controller controller = new Controller(service);

        controller.launch();
    }
}
