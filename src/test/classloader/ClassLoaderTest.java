package test.classloader;

public class ClassLoaderTest extends SuperClass {

	public static void main(String[] args) throws Exception {
		ClassLoader classLoader = new MyClassLoader();
		Class<?> aClass = classLoader.loadClass("test.classloader.ClassLoaderTest");
		Object o = aClass.newInstance();
		System.out.println(o.getClass());
		System.out.println(o instanceof ClassLoaderTest);
		System.out.println(aClass == ClassLoaderTest.class);
	}
}
