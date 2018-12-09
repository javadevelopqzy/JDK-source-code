package test.interrupt;

public class InterruptTest {

	public static void main(String[] args) throws Exception {
		ThreadLocal<String> local = ThreadLocal.withInitial(() -> "abcde");
		local.set("bbb");
		System.out.println(local.get());

		Thread thread = new Thread(() -> {
			System.out.println("线程1");
			System.out.println(local.get());
			local.set("aaa");
			System.out.println(local.get());
			System.out.println("线程结束");
		});
		thread.start();
		Thread.sleep(1000);
		System.out.println(local.get());
	}
}
