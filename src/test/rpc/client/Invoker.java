package test.rpc.client;

import test.rpc.common.ProxyClient;
import test.rpc.common.ServerApi;

import java.lang.reflect.Proxy;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Invoker {

	public static void main(String[] args) throws InterruptedException {
		ServerApi server = getServer(ServerApi.class);
		CyclicBarrier cyclicBarrier =  new CyclicBarrier(5);
		ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 5; i++) {
			executorService.execute(() -> {
				try {
					cyclicBarrier.await();
					System.out.println(server.sayHi("你妹好吗"));
				} catch (InterruptedException | BrokenBarrierException e) {
					e.printStackTrace();
				}
			});
		}
		Thread.sleep(2000);
		executorService.shutdown();
	}

	private static  <T> T getServer(Class<T> clazz) {
		return (T) Proxy.newProxyInstance(clazz.getClassLoader(), new Class[] { clazz }, new ProxyClient());
	}
}
