package tikectSalesSimulator.result;

public interface SafeAction<T> {
    Result<T> execute();
}
