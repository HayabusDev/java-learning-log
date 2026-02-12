public interface SafeAction<T> {
    Result<T> execute();
}
