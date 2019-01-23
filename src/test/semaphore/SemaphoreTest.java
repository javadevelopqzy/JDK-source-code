package test.semaphore;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SemaphoreTest {

	public static void main(String[] args) {
		SamephoreTask task = new SamephoreTask();
		ExecutorService executor = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 12; i++) {
			executor.execute(task);
		}
		executor.shutdown();
	}
}
