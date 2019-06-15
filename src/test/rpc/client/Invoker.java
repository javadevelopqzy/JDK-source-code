package test.rpc.client;

import java.lang.reflect.*;

import test.rpc.common.*;

public class Invoker {

	public static void main(String[] args) {
		ServerApi server = getServer(ServerApi.class);
		System.out.println(server.sayHi("你妹好吗"));
	}

	private static  <T> T getServer(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new ProxyClient());
	}
}
