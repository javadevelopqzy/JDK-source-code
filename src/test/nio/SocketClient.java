package test.nio;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;

public class SocketClient {

	public static CyclicBarrier cyclicBarrier = new CyclicBarrier(10);

	public static void main(String[] args) throws Exception {
		Task001 task001 = new Task001();
		ExecutorService executorService = Executors.newFixedThreadPool(10);
		for (int i = 0; i < 10; i++) {
			executorService.execute(task001);
		}
		Thread.sleep(3000);
		executorService.shutdown();
	}

	private static class Task001 implements Runnable {

		@Override
		public void run() {
			try {
				cyclicBarrier.await();
				Socket socket = new Socket("localhost", 8888);
				OutputStream outputStream = socket.getOutputStream();
				outputStream.write(("nihao：" + Thread.currentThread().getName()).getBytes());
				Random random = new Random();
				Thread.sleep(random.nextInt(3) * 1000);
				outputStream.flush();
				InputStream inputStream = socket.getInputStream();
				byte[] b = new byte[50];
				inputStream.read(b);
				System.out.println(new String(b));
				socket.close();
			} catch (IOException | InterruptedException | BrokenBarrierException e) {
				e.printStackTrace();
			}
		}
	}
}
