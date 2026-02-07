import java.util.*;

public class InMemoryRepository<T extends Identifiable> implements Repository<T> {

    private HashMap<String, T> entityMap = new HashMap<>();

    public InMemoryRepository(){

    }

    @Override
    public T findById(String id){
        return entityMap.get(id);
    }

    @Override
    public Map<String, T> findAll(){
        return Collections.unmodifiableMap(entityMap);
    }

    @Override
    public void saveEntity(T entity){
        entityMap.put(entity.getId() , entity);
    }
}
