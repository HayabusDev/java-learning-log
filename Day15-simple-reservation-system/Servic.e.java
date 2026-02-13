import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

public class Service {
    //TODO 登録・一覧・キャンセル・検索（日付）

    private final Repository repository;

    public Service(Repository repository){
        this.repository = repository;
    }

    //将来拡張するなら、class ReservationPolicy を作る
    private static final int SLOT_DURATION = 60;
    private static final LocalTime OPEN_TIME = LocalTime.of(9, 0);
    private static final LocalTime CLOSE_TIME = LocalTime.of(18,0);
    private static final LocalTime LAST_START_TIME = CLOSE_TIME.minusMinutes(SLOT_DURATION);


    private <T> Result<T> executeSafely(SafeAction<T> action, String operationName, String inputValue){
        try{
            return action.execute();

        }catch (IllegalArgumentException iae){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            iae.printStackTrace();

            return Result.failure(SystemErrorCode.INPUT_INVALID);

        }catch (IllegalStateException ise){

            System.err.println("[ERROR] " + operationName);
            System.err.println("input=" + inputValue);
            ise.printStackTrace();

            return Result.failure(SystemErrorCode.SYSTEM_ERROR);

        }catch (RuntimeException re){

            System.err.println("[FATAL] " + operationName);
            System.err.println("input=" + inputValue);
            re.printStackTrace();

            return Result.failure(SystemErrorCode.SYSTEM_ERROR);

        }
    }

    public Result<Void> saveReservation(String userName, LocalDate date, LocalTime time){
        if (userName == null || userName.isBlank()){
            return Result.failure(SystemErrorCode.INPUT_INVALID);
        }
        if(date == null){
            return Result.failure(SystemErrorCode.INPUT_INVALID);
        }
        if(time == null){
            return Result.failure(SystemErrorCode.INPUT_INVALID);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                ReservationSlot createdSlot = createSlot(date, time);
                Map<UUID, Reservation> existingReservations = repository.findAll();
                for (Reservation reservation : existingReservations.values()){
                    if(reservation.getSlot().overlaps(createdSlot)){
                        return Result.failure(SystemErrorCode.RESERVATION_CONFLICT);
                    }
                }
                UUID id = UUID.randomUUID();
                Reservation createdReservation = new Reservation(id, userName, createdSlot);
                repository.save(createdReservation);
                return Result.success();
            }
        };

        return executeSafely(action, "saveReservation", date.toString());
    }

    private ReservationSlot createSlot(LocalDate date, LocalTime time){
        LocalDateTime start = LocalDateTime.of(date, time);
        LocalDateTime end = start.plusMinutes(SLOT_DURATION);

        return new ReservationSlot(start, end);
    }

    public Result<List<ReservationSlot>> getAvailableSlots(LocalDate date){
        if (date == null) {
            return Result.failure(SystemErrorCode.INPUT_INVALID);
        }

        SafeAction<List<ReservationSlot>> action = new SafeAction<List<ReservationSlot>>() {
            @Override
            public Result<List<ReservationSlot>> execute() {
                List<ReservationSlot> bookedSlots = new ArrayList<>();
                for (Reservation reservation : repository.findAll().values()){
                    ReservationSlot slot = reservation.getSlot();
                    if (slot.getStart().toLocalDate().equals(date)){
                        bookedSlots.add(slot);
                    }
                }

                List<ReservationSlot> availableSlots = new ArrayList<>();

                LocalTime firstStartTime = resolveFirstStartTime(date);

                if (firstStartTime.isAfter(LAST_START_TIME)) {
                    return Result.success(availableSlots);
                }

                for (LocalTime time = firstStartTime; !time.isAfter(LAST_START_TIME); time = time.plusMinutes(SLOT_DURATION)){
                    ReservationSlot candidate = createSlot(date, time);

                    boolean conflicted = false;
                    for (ReservationSlot booked : bookedSlots){
                        if (booked.overlaps(candidate)){
                            conflicted = true;
                            break;
                        }
                    }

                    if(!conflicted){
                        availableSlots.add(candidate);
                    }
                }
                return Result.success(availableSlots);
            }
        };

        return executeSafely(action, "getAvailableSlots", "no-input");
    }

    private LocalTime resolveFirstStartTime(LocalDate date) {
        LocalDate today = LocalDate.now();

        if (date.isBefore(today)) {
            // 過去日は枠なし扱い
            return LAST_START_TIME.plusMinutes(1); // ループが回らない値
        }

        // 未来日は通常営業開始から
        if (date.isAfter(today)) {
            return OPEN_TIME;
        }

        // 当日：いま以降の「次の枠」へ繰り上げ
        LocalTime now = LocalTime.now();

        // 開店前ならそのままOPEN
        if (!now.isAfter(OPEN_TIME)) {
            return OPEN_TIME;
        }

        // CLOSE後なら枠なし
        if (now.isAfter(CLOSE_TIME)) {
            return LAST_START_TIME.plusMinutes(1); // ループが回らない値
        }

        long slotSeconds = (long) SLOT_DURATION * 60L;

        // OPEN_TIME から now までの経過秒数
        long diffSeconds = java.time.Duration.between(OPEN_TIME, now).getSeconds();

        // 次のスロット境界へ切り上げ（ぴったり境界でも now がその境界時刻なら「その枠は開始済み」なので次へ）
        // 例）OPEN=09:00、now=10:00:00 → 11:00 を返す
        if (diffSeconds % slotSeconds == 0) {
            diffSeconds += 1; // 境界ちょうどは次枠へ
        }

        long slots = (diffSeconds + slotSeconds - 1) / slotSeconds; // ceil
        LocalTime first = OPEN_TIME.plusSeconds(slots * slotSeconds);

        return first;
    }

    public Result<List<Reservation>> getAllReservations(){
        SafeAction<List<Reservation>> action = new SafeAction<List<Reservation>>() {
            @Override
            public Result<List<Reservation>> execute() {
                List<Reservation> copiedList = convertMapToList();
                copiedList.sort(Comparator.comparing(reservation -> reservation.getSlot().getStart()));
                return Result.success(copiedList);
            }
        };

        return executeSafely(action, "getAllReservations", "no-input");
    }

    private List<Reservation> convertMapToList(){
        return new ArrayList<>(repository.findAll().values());
    }

    public Result<List<Reservation>> searchReservationsByDate(LocalDate date){
        if (date == null) {
            return Result.failure(SystemErrorCode.INPUT_INVALID);
        }

        SafeAction<List<Reservation>> action = new SafeAction<List<Reservation>>() {
            @Override
            public Result<List<Reservation>> execute() {
                List<Reservation> matchedList = new ArrayList<>();
                List<Reservation> convertedList = convertMapToList();

                for (Reservation reservation : convertedList){
                    if (reservation.getSlot().getStart().toLocalDate().isEqual(date)){
                        matchedList.add(reservation);
                    }
                }
                matchedList.sort(Comparator.comparing(reservation -> reservation.getSlot().getStart()));
                return Result.success(matchedList);
            }
        };

        return executeSafely(action, "searchReservationsByDate", "no-input");
    }

    public Result<Void> cancelReservation(UUID id){
        if (id == null ){
            return Result.failure(SystemErrorCode.INPUT_INVALID);
        }

        if (repository.findById(id) == null){
            return Result.failure(SystemErrorCode.DATA_NOT_FOUND);
        }

        SafeAction<Void> action = new SafeAction<Void>() {
            @Override
            public Result<Void> execute() {
                repository.removeById(id);
                return Result.success();
            }
        };

        return executeSafely(action, "cancelReservation", id.toString());
    }

    public LocalDate[] getReservableDateRange() {

        LocalDate today = LocalDate.now();
        LocalDate maxDate = today.plusDays(90);

        return new LocalDate[] { today, maxDate };
    }
}
