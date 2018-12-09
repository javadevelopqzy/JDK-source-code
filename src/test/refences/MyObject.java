package test.refences;

public class MyObject {

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		System.out.println("自定义对象被回收");
	}
}
