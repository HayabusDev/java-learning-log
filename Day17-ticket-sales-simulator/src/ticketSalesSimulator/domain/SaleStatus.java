package tikectSalesSimulator.domain;

//振る舞いを持つenumのため、コンストラクタは作らない
public enum SaleStatus {
    //販売準備中
    DRAFT,
    //販売中
    ON_SALE,
    //売り切れ
    SOLD_OUT,
    //販売終了(販売期間終了)
    CLOSED;


    public boolean isPurchasable(){
        return switch (this) {
            case ON_SALE -> true;
            case DRAFT, CLOSED, SOLD_OUT -> false;
        };
    }

    public boolean isCancelable(){
        return switch (this){
            case ON_SALE, SOLD_OUT -> true;
            case DRAFT, CLOSED -> false;
        };
    }

    public boolean canTransitTo(SaleStatus next){
        return switch (this) {
            case DRAFT -> next == ON_SALE;
            case ON_SALE -> next == SOLD_OUT || next == CLOSED;
            case SOLD_OUT -> next == ON_SALE || next == CLOSED;
            case CLOSED -> false;
        };
    }
}
