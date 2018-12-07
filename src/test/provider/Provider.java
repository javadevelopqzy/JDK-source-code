package test.provider;

import java.util.concurrent.*;

public class Provider {

	private LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>();

	private ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(4);

	private volatile boolean started = false;

	public void start() {
		executor.execute(() -> {
			while (started) {
				try {
					Runnable peek = queue.take();
					executor.execute(peek);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		started = true;
	}

	public void close() {
		started = false;
		while (executor.getActiveCount() != 0) {}
		executor.shutdown();
	}

	public void consumer(Consumer consumer) {
		if (!started) {
			throw new IllegalStateException("生成者服务已经关闭");
		}
		queue.offer(consumer.task);
	}
}
