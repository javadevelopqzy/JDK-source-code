package test.parttern.singal;

public class TestSingal {

	static int count = 0;

	public static void main(String[] args) throws ClassNotFoundException {
		walk(4);
		System.out.println(count);
	}

	private static void walk(int n) {
		if (n <= 0) {
			if (n == 0) count++;
			return;
		}
		walk(n - 1);
		walk(n - 2);
	}
}
