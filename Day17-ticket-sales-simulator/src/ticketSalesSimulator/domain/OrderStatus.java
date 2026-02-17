package tikectSalesSimulator.domain;

public enum OrderStatus {
    ACTIVE,
    CANCELED;

    //席を消費（占有）しているか
    public boolean isActive(){
        return switch (this){
            case ACTIVE -> true;
            case CANCELED -> false;
        };
    }

    //状態変更可能（席を戻せるか）か
    public boolean canCancel(){
        return switch (this){
            case ACTIVE -> true;
            case CANCELED -> false;
        };
    }
}
