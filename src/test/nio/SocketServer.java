package test.nio;

import java.io.*;
import java.net.*;

public class SocketServer {

	public static void main(String[] args) throws IOException, InterruptedException {
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(8888));
		while (true) {
			Socket accept = server.accept();
			byte[] b = new byte[100];
			InputStream inputStream = accept.getInputStream();
			inputStream.read(b);
			System.out.println("收到客户端信息：" + new String(b));
			accept.getOutputStream().write(("服务端已经收到：" + Thread.currentThread().getName()).getBytes());
			accept.close();
		}
	}
}
