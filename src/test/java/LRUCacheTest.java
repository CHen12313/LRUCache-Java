import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LRUCacheTest {
    @Test
    void creatingCacheWithPositiveCapacitySucceeds() {
        assertDoesNotThrow(() -> new LRUCache(3));
    }

    @Test
    void creatingCacheWithNonPositiveCapacityThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new LRUCache(0));
        assertThrows(IllegalArgumentException.class, () -> new LRUCache(-1));
    }

    @Test
    void putReturnsHashCodeOfStoredObject() {
        LRUCache cache = new LRUCache(3);

        Object value = "hello";
        int expectedHash = value.hashCode();

        int returnedHash = cache.put(value);

        assertEquals(expectedHash, returnedHash);
    }

    @Test
    void putStoresObjectInCache() {
        LRUCache cache = new LRUCache(3);

        Object value = "hello";
        int hash = cache.put(value);

        Object retrieved = cache.get(hash);

        assertEquals(value, retrieved);
    }

    @Test
    void cacheEvictsLeastRecentlyUsedWhenCapacityExceeded() {
        LRUCache cache = new LRUCache(2);

        Object a = "A";
        Object b = "B";
        Object c = "C";

        int hashA = cache.put(a);
        int hashB = cache.put(b);

        // Cache is full [A, B], A is least recently used
        int hashC = cache.put(c);  // Should evict A

        // A should be gone
        assertThrows(IllegalArgumentException.class, () -> cache.get(hashA));

        // B and C should still be present
        assertEquals(b, cache.get(hashB));
        assertEquals(c, cache.get(hashC));
    }

    @Test
    void getUpdatesRecentnessSoAccessedItemIsNotEvicted() {
        LRUCache cache = new LRUCache(2);

        Object a = "A";
        Object b = "B";
        Object c = "C";

        int hashA = cache.put(a);
        int hashB = cache.put(b);

        // At this point, A is least recently used
        // Access A to make it most recent.
        Object retrievedA = cache.get(hashA);
        assertEquals(a, retrievedA);

        // Now insert C should evict B, not A
        int hashC = cache.put(c);

        // B should be gone
        assertThrows(IllegalArgumentException.class, () -> cache.get(hashB));

        // A and C should still be here
        assertEquals(a, cache.get(hashA));
        assertEquals(c, cache.get(hashC));
    }

    @Test
    void puttingExistingObjectUpdatesRecentnessAndPreventsItsEviction() {
        LRUCache cache = new LRUCache(2);

        Object a = "A";
        Object b = "B";
        Object c = "C";

        int hashA = cache.put(a);
        int hashB = cache.put(b);

        // Reput A, this should make A the most recent
        int hashA2 = cache.put(a);
        assertEquals(hashA, hashA2);  // same hash

        // Now insert C, this should evict B, not A
        int hashC = cache.put(c);

        // B should be gone
        assertThrows(IllegalArgumentException.class, () -> cache.get(hashB));

        // A and C should still be here
        assertEquals(a, cache.get(hashA));
        assertEquals(c, cache.get(hashC));
    }
}
