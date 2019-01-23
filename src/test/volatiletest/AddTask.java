package test.volatiletest;

import java.util.concurrent.locks.*;

public class AddTask implements Runnable {

	ReentrantLock lock = new ReentrantLock(true);
	volatile static boolean flag = false;

	@Override
	public void run() {
		while (!flag) {

		}
		for (int i = 0; i < 20; i++) {
			lock.lock();
			System.out.println(Thread.currentThread().getName() + "   获得锁");
			lock.unlock();
		}
	}
}
