package test.threadlocal;

public class MyThread extends Thread {

	public MyThread(Runnable target) {
		super(target);
	}

	@Override
	public void run() {
		super.run();
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("线程被回收");
	}
}
