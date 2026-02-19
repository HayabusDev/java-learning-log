package tikectSalesSimulator;

import tikectSalesSimulator.controller.TicketController;
import tikectSalesSimulator.repository.EventRepository;
import tikectSalesSimulator.repository.OrderRepository;
import tikectSalesSimulator.service.TicketService;
import tikectSalesSimulator.validator.TicketValidator;

public class Main {
    public static void main(String[] args){
        EventRepository eventRepository = new EventRepository();
        OrderRepository orderRepository = new OrderRepository();
        TicketValidator ticketValidator = new TicketValidator();
        TicketService ticketService = new TicketService(eventRepository, orderRepository, ticketValidator);
        TicketController ticketController = new TicketController(ticketService);

        ticketController.launch();
    }
}
