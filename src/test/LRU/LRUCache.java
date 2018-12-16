package test.LRU;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 基于LinkedHashMap的LRU算法
 */
public class LRUCache<K, V> extends LinkedHashMap<K, V> {

	private int CACHE_SIZE;

	public LRUCache(int size) {
		super(size, 1f, true);
		CACHE_SIZE = size;
	}

	@Override
	protected boolean removeEldestEntry(Map.Entry eldest) {
		return size() > CACHE_SIZE;
	}
}
