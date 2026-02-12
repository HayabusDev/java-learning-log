import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Repository {

    private final HashMap<UUID, Reservation> reservationMap = new HashMap<UUID, Reservation>();

    public Repository(){}

    public Reservation findById(UUID id){return reservationMap.get(id);}

    public Map<UUID, Reservation> findAll(){return Collections.unmodifiableMap(reservationMap);}

    public void save(Reservation createdReservation){
        if (createdReservation == null){
            throw new IllegalArgumentException("Reservation must not be null.");
        }

        reservationMap.put(createdReservation.getId(), createdReservation);
    }

    public void removeById(UUID id){
        if (id == null){
            throw new IllegalArgumentException("id must not be null.");
        }

        reservationMap.remove(id);
    }
}
