package test.LRU;

public class TestLRU {

	public static void main(String[] args) {
		// JDK LRU
		LRUCache<String, Integer> cache = new LRUCache<>(2);
		cache.put("a", 15);
		cache.put("b", 17);
		cache.get("b");
		System.out.println(cache);
		cache.put("c", 18);
		System.out.println("满了之后：" + cache);

		// CustomerLRU
		CustomLRUCache<String, String> customLRUCache = new CustomLRUCache<>(5);
		customLRUCache.put("a", "v");
		customLRUCache.put("b", "c");
		customLRUCache.put("d", "e");
		customLRUCache.put("t", "t");
		customLRUCache.put("a1", "a2");
		customLRUCache.get("a1");
		customLRUCache.get("a");
		customLRUCache.get("b");
		customLRUCache.get("d");
		customLRUCache.put("a3", "a4");
		System.out.println(customLRUCache);
	}
}
