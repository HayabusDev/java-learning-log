import java.util.Map;

public interface Repository<T extends Identifiable> {
    void saveEntity(T entity);
    T findById(String id);
    Map<String, T> findAll();
}
