package test.concurrent.volatiletest;

public class TestLock {

	public static void main(String[] args) throws InterruptedException {
		AddTask task = new AddTask();
		Thread thread1 = new Thread(task, "线程1");
		Thread thread2 = new Thread(task, "线程2");
		thread1.start();
		thread2.start();
		AddTask.flag = true;
		Thread.sleep(10000);
	}
}
