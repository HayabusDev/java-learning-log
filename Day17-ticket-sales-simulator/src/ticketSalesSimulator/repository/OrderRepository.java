package tikectSalesSimulator.repository;
import tikectSalesSimulator.domain.TicketOrder;
import java.util.*;

public class OrderRepository {
    private HashMap<UUID, TicketOrder> inMemoryOrderRepository = new HashMap<UUID, TicketOrder>();

    public OrderRepository(){}

    public TicketOrder findOrderById(UUID orderId){
        return inMemoryOrderRepository.get(orderId);
    }

    public Map<UUID, TicketOrder> findAllOrders(){
        return Collections.unmodifiableMap(inMemoryOrderRepository);
    }

    public List<TicketOrder> findOrderByEventId(UUID eventId){
        List<TicketOrder> foundOrders = new ArrayList<TicketOrder>();

        for(TicketOrder order : inMemoryOrderRepository.values()){
            if (Objects.equals(order.getEventId(), eventId)){
                foundOrders.add(order);
            }
        }
        return foundOrders;
    }

    public void saveOrder(TicketOrder createdOrder){
        if (createdOrder == null){
            throw new IllegalArgumentException("order must not be null");
        }

        inMemoryOrderRepository.put(createdOrder.getOrderId(), createdOrder);
    }

    public void deleteOrderById(UUID orderId){
        if (orderId == null){
            throw new IllegalArgumentException("orderId must not be null");
        }

        inMemoryOrderRepository.remove(orderId);
    }
}
