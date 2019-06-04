package test.concurrent.countdown;

import java.util.concurrent.*;

public class TestCountDown {

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch countDownLatch = new CountDownLatch(2);
		countDownLatch.countDown();
		countDownLatch.countDown();
		new Thread(() -> {
			try {
				System.out.println("进入现场");
				countDownLatch.await();
				System.out.println("线程结束");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).start();
		Thread.sleep(1000);
//		countDownLatch.countDown();
//		countDownLatch.countDown();
		countDownLatch.await();
		System.out.println("结束");
	}

}
