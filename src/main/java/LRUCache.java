import java.util.LinkedList;
import java.util.Map;

public class LRUCache {
    private int capacity;
    private Map<Integer, Object> cacheMap;
    private LinkedList<Integer> recentOrder;

    public LRUCache(int capacity) {
        if  (capacity <= 0) {
            throw new IllegalArgumentException();
        }
        this.capacity = capacity;
        this.cacheMap = new java.util.HashMap<>();
        this.recentOrder = new LinkedList<>();
    }

    public int put(Object value){
        int hash = value.hashCode();
        cacheMap.put(hash, value);
        touch(hash);

        return value.hashCode();
    }

    public Object get(int hash){
        if (!cacheMap.containsKey(hash)) {
            throw new IllegalArgumentException("Object not in cache");
        }

        touch(hash);

        return cacheMap.get(hash);
    }

    // It updates recentness and evict if capacity is exceeded
    private void touch(int hash){
        if (!cacheMap.containsKey(hash)) {
            throw new IllegalArgumentException("Object not in cache");
        }

        // Remove the original entry if present, then add the new one
        recentOrder.remove(Integer.valueOf(hash));
        recentOrder.addFirst(Integer.valueOf(hash));

        if (recentOrder.size() > capacity) {
            Integer LRUHash = recentOrder.removeLast();
            cacheMap.remove(LRUHash);
        }
    }
}