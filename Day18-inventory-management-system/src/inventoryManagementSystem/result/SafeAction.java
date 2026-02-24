package inventoryManagementSystem.result;

public interface SafeAction<T> {
    Result<T> execute();
}
