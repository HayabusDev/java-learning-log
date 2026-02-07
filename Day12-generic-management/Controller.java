import java.util.List;

public class Controller {
    //ControllerをGenericにするのか問題
    //Generic化→Task専用画面 / User専用画面に分ける→画面/APIが型ごとに分かれている
    //非Generic→1つのメニューで Task/User を切り替える→1つの画面/メニューで複数ユースケースをまとめる

    private final Service<Task> taskService;
    private final Service<User> userService;
    private final DataFactory dataFactory;

    public Controller(Service<Task> taskService, Service<User> userService, DataFactory dataFactory){
        this.taskService = taskService;
        this.userService = userService;
        this.dataFactory = dataFactory;
    }

    public void launch(){
        showMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 3);

            switch (userInput) {
                case 1:
                    handleAddEntity();
                    break;
                case 2:
                    handleListEntity();
                    break;
                case 3:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void showMenu(){
        System.out.println("=====================");
        System.out.println("ようこそ！");
        System.out.println("1. 追加");
        System.out.println("2. 一覧");
        System.out.println("3. 終了");
        System.out.println("=====================");
    }

    private void handleAddEntity(){
        showAddMenu();
        int userInput = InputUtil.readIntInRange("数値を入力してください。", 1, 2);

        switch (userInput){
            case 1:
                handleAddTask();
                break;
            case 2:
                handleAddUser();
                break;
            default:
        }
    }

    private void showAddMenu(){
        System.out.println("何を追加しますか？");
        System.out.println("1. Task");
        System.out.println("2. User");
    }

    private void handleAddTask(){
        String taskId = InputUtil.readNonEmptyString("タスク名を入力してください。");
        taskService.saveEntity(dataFactory.createTask(taskId));
    }

    private void handleAddUser(){
        String userId = InputUtil.readNonEmptyString("ユーザー名を入力してください。");
        userService.saveEntity(dataFactory.createUser(userId));
    }

    private void handleListEntity(){
        showListMenu();
        int userInput = InputUtil.readIntInRange("数値を入力してください。", 1, 2);

        switch (userInput){
            case 1:
                handleListTask();
                break;
            case 2:
                handleListUser();
                break;
            default:
        }
    }

    private void showListMenu(){
        System.out.println("どの一覧を閲覧しますか？");
        System.out.println("1. Task");
        System.out.println("2. User");
    }

    //タイポが怖い、enum化
    private void handleListTask(){
        printList(Label.Task, taskService.listEntities());
    }

    private void handleListUser(){
        printList(Label.User, userService.listEntities());
    }

    private <E extends Identifiable> void printList(Label label, List<E> list){
        if (list == null || list.isEmpty()) {
            System.out.println("登録されている" + label + "はありません。");
            return;
        }

        System.out.println("全 " + list.size() + " 件");
        for (E e : list) {
            System.out.println(e);
        }
    }
}
