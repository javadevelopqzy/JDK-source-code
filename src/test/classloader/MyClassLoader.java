package test.classloader;

import java.io.IOException;
import java.io.InputStream;

public class MyClassLoader extends ClassLoader {

	@Override
	public Class<?> loadClass(String name) throws ClassNotFoundException {
		byte[] b = null;
		try {
			String fileName = name.substring(name.lastIndexOf('.') + 1) + ".class";
			InputStream is = getClass().getResourceAsStream(fileName);
			// 如果当前类读取不到流，则用父类加载器加载
			// 因为加载类时，需要先加载它的父类即Object类，当前加载器加载不到Object，只能从父类（AppClassLoader）加载
			if (is == null) {
				return super.loadClass(name);
			}
			b = new byte[is.available()];
			is.read(b);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return defineClass(name, b, 0, b.length);
	}
}
