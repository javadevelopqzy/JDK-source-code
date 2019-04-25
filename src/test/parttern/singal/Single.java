package test.parttern.singal;

public class Single {

	public static String a = "a";

	static {
		System.out.println("加载");
	}

	private Single() {
		System.out.println("构造对象了");
	}

	public static Single getInstance() {
		return SingleWrap.single;
	}

	private static class SingleWrap {

		static Single single = new Single();
	}
}
