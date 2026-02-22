package inventoryManagementSystem.repository;

import inventoryManagementSystem.domain.Item;
import inventoryManagementSystem.valueObject.ItemId;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ItemRepository {

    private HashMap<ItemId, Item> itemMap = new HashMap<ItemId, Item>();

    public ItemRepository(){}

    public Item findById(ItemId itemId){
        if (itemId == null){
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        return itemMap.get(itemId);
    }

    public Map<ItemId, Item> findAll(){
        return Collections.unmodifiableMap(itemMap);
    }

    public boolean existsById(ItemId itemId){
        if (itemId == null){
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        return itemMap.containsKey(itemId);
    }

    //save = 新規専用(存在チェックはServiceで行う)
    public void save(Item createdItem){
        if (createdItem == null){
            throw new IllegalArgumentException("Item cannot be null");
        }

        itemMap.put(createdItem.getId(), createdItem);
    }

    public void deleteById(ItemId itemId){
        if  (itemId == null){
            throw new IllegalArgumentException("ItemId cannot be null");
        }

        itemMap.remove(itemId);
    }
}
