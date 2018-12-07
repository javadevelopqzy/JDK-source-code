package test.provider;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		Provider provider = new Provider();
		provider.start();
		provider.consumer(new Consumer(new Thread(() -> System.out.println("消费者来了"))));
		Thread.sleep(1000);
		provider.consumer(new Consumer(new Thread(() -> System.out.println("消费者来了"))));
		Thread.sleep(1000);
		provider.consumer(new Consumer(new Thread(() -> System.out.println("消费者来了"))));
		provider.close();
//		provider.consumer(new Consumer(new Thread(() -> System.out.println("消费者来了"))));
		Thread.sleep(1000);
	}
}
