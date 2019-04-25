package test.concurrent.atomic;

import java.util.concurrent.atomic.*;

public class AtomicTest {

	public static void main(String[] args) {
//		AtomicInteger integer = new AtomicInteger();
//		System.out.println(integer.getAndSet(5));
//		System.out.println(integer.get());
		LongAdder longAdder = new LongAdder();
		for (int i = 0; i < 50067; i++) {
			longAdder.add(i * 3);
		}
	}
}
