import java.time.LocalDateTime;

public class ReservationSlot {

    private final LocalDateTime start;
    private final LocalDateTime end;

    public ReservationSlot(LocalDateTime start, LocalDateTime end){
        if (start == null){
            throw new IllegalArgumentException("start must not be null");
        }
        if (end == null){
            throw new IllegalArgumentException("end must not be null");
        }
        if (start.isAfter(end) || start.isEqual(end)){
            throw new IllegalArgumentException("end must be after start");
        }
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public boolean overlaps(ReservationSlot other){
        return this.start.isBefore(other.end) && other.start.isBefore(this.end);
    }
}
