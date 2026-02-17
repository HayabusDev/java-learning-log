package tikectSalesSimulator.repository;
import tikectSalesSimulator.domain.Event;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class EventRepository {
    private HashMap<UUID, Event> inMemoryEventRepository = new HashMap<UUID, Event>();

    public EventRepository(){}

    public Event findEventById(UUID eventId){
        return inMemoryEventRepository.get(eventId);
    }

    public Map<UUID, Event> findAllEvents(){
        return Collections.unmodifiableMap(inMemoryEventRepository);
    }

    public boolean containsKey(UUID eventId){
        return inMemoryEventRepository.containsKey(eventId);
    }

    public void saveEvent(Event createdEvent){
        if (createdEvent == null){
            throw new IllegalArgumentException("event must not be null");
        }

        inMemoryEventRepository.put(createdEvent.getEventId(), createdEvent);
    }

    public void deleteEventById(UUID eventId){
        if (eventId == null){
            throw new IllegalArgumentException("eventId must not be null");
        }

        //SaleStatusがCLOSEなら削除可能(TicketServicenで判定)
        inMemoryEventRepository.remove(eventId);
    }
}
