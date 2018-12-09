package test.threadlocal;

public class TestThreadLocal {

	public static void main(String[] args) throws InterruptedException {
		MyThreadLocal<String> myThreadLocal = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal2 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal3 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal4 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal5 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal6 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal7 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal8 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal9 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal10 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal11 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal12= new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal13 = new MyThreadLocal<>("自2");
		MyThreadLocal<String> myThreadLocal14 = new MyThreadLocal<>("自2");
		MyThread thread = new MyThread(() -> {
			put();
			myThreadLocal.set("何淑君");
			myThreadLocal2.set("aaaaaa1");
			myThreadLocal3.set("aaaaaa2");
			myThreadLocal4.set("aaaaaa3");
			myThreadLocal5.set("aaaaaa4");
			myThreadLocal6.set("aaaaaa6");
			myThreadLocal7.set("aaaaaa7");
			myThreadLocal8.set("aaaaaa8");
			myThreadLocal9.set("aaaaaa9");
			myThreadLocal10.set("aaaaaa10");
			myThreadLocal11.set("aaaaaa11");
			myThreadLocal12.set("aaaaaa12");
			myThreadLocal13.set("aaaaaa13");
			myThreadLocal14.set("aaaaaa13");
		});
		thread.start();
		Thread.sleep(1000);
		System.gc();
		Thread.sleep(5000);
		System.out.println("结束");
	}

	private static void put() {
		MyThreadLocal<String> myThreadLocal = new MyThreadLocal<>("自定义的");
		myThreadLocal.set("哈哈哈");
	}
}
