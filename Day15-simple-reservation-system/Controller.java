import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class Controller {
    private final Service service;

    public Controller(Service service){
        this.service = service;
    }

    public void launch(){
        showLaunchMenu();
        boolean isRunning = true;

        while (isRunning) {
            int userInput = InputUtil.readIntInRange("要件を入力してください。", 1, 5);

            switch (userInput) {
                case 1:
                    handleSaveReservation();
                    break;
                case 2:
                    handleListReservation();
                    break;
                case 3:
                    handleSearchReservation();
                    break;
                case 4:
                    handleDeleteReservation();
                    break;
                case 5:
                    System.out.println("終了します。");
                    isRunning = false;
                    break;
                default:
            }
        }
    }

    private void showLaunchMenu(){
        System.out.println("=====================");
        System.out.println("予約管理システム");
        System.out.println("1. 予約");
        System.out.println("2. 一覧");
        System.out.println("3. 日付検索");
        System.out.println("4. 削除");
        System.out.println("5. 終了");
        System.out.println("=====================");
    }

    private void handleSaveReservation() {
        String userName = InputUtil.readNonEmptyString("名前を入力してください。");
        LocalDate reservedDate = inputReservationDate();

        Result<List<ReservationSlot>> canGetAvailable = service.getAvailableSlots(reservedDate);

        if(!canGetAvailable.isSuccess()){
            System.out.println(canGetAvailable.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<ReservationSlot> availableSlots = canGetAvailable.getData();

        if (availableSlots.isEmpty()){
            System.out.println("予約可能な時間はありません。日時選択からやり直してください。");
            return;
        }

        System.out.println("予約可能時間は以下の通りです。");
        for (int i = 0; i < availableSlots.size(); i++){
            System.out.println(i+1 + ". " + availableSlots.get(i).getStart().toLocalTime() + " - " + availableSlots.get(i).getEnd().toLocalTime());
        }
        int userChoice = InputUtil.readIntInRange("予約時間を選択してください。", 1, availableSlots.size());

        ReservationSlot selectedSlot = availableSlots.get(userChoice - 1);
        LocalTime selectedTime = selectedSlot.getStart().toLocalTime();

        boolean userYesNo = InputUtil.confirmYesNo(userName + " 様: " + reservedDate + " の " + selectedTime + " で予約します。よろしいですか？(y / n)");

        if(!userYesNo){
            System.out.println("予約受付を中止しました。");
            return;
        }

        Result<Void> canReserve = service.saveReservation(userName, reservedDate, selectedTime);
        if(!canReserve.isSuccess()){
            System.out.println(canReserve.getMessage() + "\n最初からやり直してください。");
            return;
        }

        System.out.println(userName + " 様: " + reservedDate + " の " + selectedTime + " で予約しました。" );
    }

    private void handleListReservation() {
        Result<List<Reservation>> allReservations = service.getAllReservations();
        if(!allReservations.isSuccess()){
            System.out.println(allReservations.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<Reservation> reservationList = allReservations.getData();

        if (reservationList.isEmpty()){
            System.out.println("予約はありません。");
            return;
        }

        printReservations(reservationList);
    }

    private void handleSearchReservation() {
        System.out.println("検索日を入力します。");
        LocalDate searchedDate = inputReservationDate();

        Result<List<Reservation>> canSearch = service.searchReservationsByDate(searchedDate);

        if (!canSearch.isSuccess()){
            System.out.println(canSearch.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<Reservation> reservationList = canSearch.getData();

        if (reservationList.isEmpty()){
            System.out.println(searchedDate + "の予約はありません。");
            return;
        }

        printReservations(reservationList);
    }

    private void printReservations(List<Reservation> list){
        System.out.println("全 " + list.size() + " 件");

        for (Reservation reservation : list){
            System.out.println("予約日: " + reservation.getSlot().getStart().toLocalDate()
                    + " 予約時間:" + reservation.getSlot().getStart().toLocalTime() + " - " + reservation.getSlot().getEnd().toLocalTime()
                    + " 予約名: " + reservation.getUserName()
                    + " 予約ID: " + reservation.getId());
        }
    }

    private void handleDeleteReservation() {
        System.out.println("予約日を入力します。");
        LocalDate reservedDate = inputReservationDate();

        Result<List<Reservation>> canGetReservations = service.searchReservationsByDate(reservedDate);

        if (!canGetReservations.isSuccess()){
            System.out.println(canGetReservations.getMessage() + "\n最初からやり直してください。");
            return;
        }

        List<Reservation> reservations = canGetReservations.getData();

        if (reservations.isEmpty()){
            System.out.println(reservedDate + " の予約はありません。");
            return;
        }

        System.out.println("予約一覧は以下の通りです。");
        for (int i = 0; i < reservations.size(); i++){
            System.out.println(i+1 + ". " + "予約ID: " + reservations.get(i).getId() + " 予約名: " + reservations.get(i).getUserName()
                    + " 予約時間: " + reservations.get(i).getSlot().getStart().toLocalTime());
        }

        int userChoice = InputUtil.readIntInRange("キャンセルする予約を選択してください。", 1, reservations.size());
        Reservation cancelReservation = reservations.get(userChoice - 1);

        boolean userYesNo = InputUtil.confirmYesNo("予約ID: " + cancelReservation.getId() + " 予約名: " + cancelReservation.getUserName()
                + " 予約時間: " + cancelReservation.getSlot().getStart().toLocalTime() + " をキャンセルします。よろしいですか？(y / n)");

        if(!userYesNo){
            System.out.println("キャンセル受付を中止しました。");
            return;
        }

        Result<Void> canCancel = service.cancelReservation(cancelReservation.getId());
        if (!canCancel.isSuccess()){
            System.out.println(canCancel.getMessage() + "\n最初からやり直してください。");
            return;
        }

        System.out.println("予約ID: " + cancelReservation.getId() + " 予約名: " + cancelReservation.getUserName()
                + " 予約時間: " + cancelReservation.getSlot().getStart().toLocalTime() + " をキャンセルしました。");
    }

    private LocalDate inputReservationDate(){
        LocalDate[] range = service.getReservableDateRange();
        LocalDate min = range[0];
        LocalDate max = range[1];
        return InputUtil.readDateWithinRange(min, max);
    }
}
