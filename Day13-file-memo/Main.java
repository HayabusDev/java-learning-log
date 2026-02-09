public class Main {
    public static void main(String[] args){
        Storage storage = new Storage();
        Service service = new Service(storage);
        Controller controller = new Controller(service);

        controller.launch();
    }
}
