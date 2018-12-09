package test.refences;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class TestReference {

	public static void main(String[] args) throws InterruptedException {
		Map<Object, Object> map = new HashMap<>();
		put(map);
		System.gc();
		Thread.sleep(5000);
		System.out.println(map);
	}

	static void put(Map<Object, Object> map) {
		WeakReference object = new WeakReference(new MyObject());
		map.put(object, object);
	}
}
