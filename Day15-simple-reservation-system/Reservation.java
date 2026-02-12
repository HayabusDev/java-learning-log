import java.util.UUID;

public class Reservation {

    private final UUID id;
    private final String userName;
    private final ReservationSlot slot;

    public Reservation(UUID id, String userName, ReservationSlot slot){
        if(id == null){
            throw new IllegalArgumentException("id must not be null");
        }
        if(userName == null){
            throw new IllegalArgumentException("userName must not be null");
        }
        if(slot == null){
            throw new IllegalArgumentException("slot must not be null");
        }
        this.id = id;
        this.userName = userName;
        this.slot = slot;
    }

    public UUID getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public ReservationSlot getSlot() {
        return slot;
    }
}
