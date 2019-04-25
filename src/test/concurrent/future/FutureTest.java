package test.concurrent.future;

import java.util.concurrent.*;

public class FutureTest {

	private static ExecutorService executorService = Executors.newFixedThreadPool(2);

	public static void main(String[] args) throws Exception {
		getResult();
		cancelTask();
		executorService.shutdown();
	}

	private static void getResult() throws ExecutionException, InterruptedException, TimeoutException {
		Future<?> future = executeTask();
		System.out.println(future.get(3, TimeUnit.SECONDS));
	}

	private static void cancelTask() throws InterruptedException {
		Future<?> future = executeTask();
		Thread.sleep(100);
		future.cancel(false);
	}

	private static Future<?> executeTask() {
		return executorService.submit(() -> {
			try {
				System.out.println("执行一个2秒钟的任务");
				Thread.sleep(500);
				System.out.println("执行完了");
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return "result";
		});
	}
}
