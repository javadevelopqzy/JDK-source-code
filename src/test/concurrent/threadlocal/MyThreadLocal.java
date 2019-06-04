package test.concurrent.threadlocal;

public class MyThreadLocal<T> extends ThreadLocal<T> {

	String name;

	public MyThreadLocal(String name) {
		this.name = name;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println(name + "已经被回收");
	}
}
