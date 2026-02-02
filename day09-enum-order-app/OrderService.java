import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class OrderService {
    Scanner sc = new Scanner(System.in);

    private ArrayList<Order> orderList = new ArrayList<>();
    private int latestId;

    public OrderService(){
        this.latestId = maxOrderId();
    }

    public void makeOrder(){
        String customerChoice;
        DrinkType orderedDrink;
        int drinkQuantity;
        String userYorN;
        System.out.println("ご注文を下記から選んでください。");
        for (DrinkType drinkType : DrinkType.values()){
            System.out.println(drinkType);
        }
        customerChoice = sc.nextLine();
        orderedDrink = DrinkType.valueOf(customerChoice);
        System.out.println("何点にしますか？");
        drinkQuantity = sc.nextInt();
        sc.nextLine();
        System.out.println(orderedDrink + " を " + drinkQuantity + "点" + "\n" + "ご注文を確定します。よろしいですか？(y / n)");
        userYorN = sc.nextLine();
        if (userYorN.equals("y")){
            Order newOrder = new Order(generateId(), orderedDrink, drinkQuantity);
            orderList.add(newOrder);
            System.out.println(showOrder() + "\nご注文を承りました。");
        }else {
        System.out.println("ご注文をキャンセルしました。");
        }
    }

    private int generateId(){
        latestId++;
        return latestId;
    }

    private int maxOrderId(){
        int maxId = 0;
        for (Order order : orderList){
            if (order.getOrderId() > maxId){
                maxId = order.getOrderId();
            }
        }
        return maxId;
    }

    private List<Order> showOrder(){
        return Collections.unmodifiableList(orderList);
    }
}
