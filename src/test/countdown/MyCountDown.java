package test.countdown;

import java.util.concurrent.locks.*;

public class MyCountDown extends AbstractQueuedSynchronizer {

	private int count;

	private int current;

	private Condition condition = new ConditionObject();

	public MyCountDown(int count) {
		this.count = count;
		this.current = count;
	}

	public void await() throws InterruptedException {
		condition.await();
	}

	public void countDown() {
		if (current > 0) {
			current--;
		} else {
			condition.signalAll();
			current = count;
		}
	}
}
