package test.singal;

public class Singal {

	public static String a = "a";

	static {
		System.out.println("加载");
	}

	private Singal() {
		System.out.println("构造对象了");
	}

	public static Singal getInstance() {
		return SingalWrap.singal;
	}

	private static class SingalWrap {

		static Singal singal = new Singal();
	}
}
