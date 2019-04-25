package test.concurrent.semaphore;

import java.util.concurrent.Semaphore;

public class SamephoreTask implements Runnable {

	Semaphore semaphore = new Semaphore(3);

	@Override
	public void run() {
		try {
			semaphore.acquire();
			System.out.println("线程获得许可：" + Thread.currentThread().getName());
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			System.out.println("线程释放许可：" + Thread.currentThread().getName());
			semaphore.release();
		}
	}
}
