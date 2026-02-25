package inventoryManagementSystem.valueObject;

import java.util.UUID;

public class ItemId {
    private final UUID itemId;

    public ItemId(UUID ItemId) {
        if (ItemId == null) {
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        this.itemId = ItemId;
    }

    public UUID getItemId() {
        return itemId;
    }

    @Override
    public boolean equals(Object other) {
        // null と比較した場合は必ず false を返す
        // equals は例外を出さず安全に終了する必要があるため
        if (other == null) {
            return false;
        }

        // 同一インスタンスなら無条件で同一
        // 最速判定（参照一致チェック）で処理を打ち切る最適化
        if (this == other) {
            return true;
        }

        // 型が違うなら同一にはなり得ない
        // 「ItemId同士だけを比較する」という契約の確認
        if (!(other instanceof ItemId)) {
            return false;
        }

        // ValueObjectの本体比較
        // 内部のUUIDが同じなら同一とみなす
        ItemId otherId = (ItemId) other;
        if (this.itemId.equals(otherId.itemId)) {
            return true;
        }

        // UUIDが違う → 別の識別子
        return false;
    }

    @Override
    public int hashCode() {
        return this.itemId.hashCode();
    }

    @Override
    public String toString(){
        return this.itemId.toString();
    }
}
