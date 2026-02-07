import java.util.ArrayList;
import java.util.List;

//Generic Service: Tの中身が確定していないためここではnewできない→汎用処理
//Tは外からやってくる→DIやフレームワークなど、現状はControllerでnew
//非Generic Service: データが固定されている＝１つしか扱えない→newできるが、責任重くなる
public class Service<T extends Identifiable>{

    private final Repository<T> repository;

    public Service (Repository<T> repository){
        this.repository = repository;
    }

    public boolean saveEntity(T entity){
        if (entity == null){
            return false;
        }

        //T createdT = new T(); Generic Serviceはnewできない
        repository.saveEntity(entity);
        return true;
    }

    public List<T> listEntities(){
        return convertMapToList();
    }

    private List<T> convertMapToList(){
        return new ArrayList<>(repository.findAll().values());
    }
}
