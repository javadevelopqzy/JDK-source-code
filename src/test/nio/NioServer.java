package test.nio;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

public class NioServer {

	private Selector selector;

	public NioServer(int port) throws IOException {
		ServerSocketChannel channel = ServerSocketChannel.open();
		// 监听8888端口
		channel.socket().bind(new InetSocketAddress(port));
		// 设置为非阻塞
		channel.configureBlocking(false);
		// 创建selector线程
		selector = Selector.open();
		// 绑定selector到channel上
		channel.register(selector, SelectionKey.OP_ACCEPT);
	}

	private void listen() throws IOException {
		while (true) {
			// 每隔100毫秒唤醒一次selector
			selector.select(100);
			// 轮询所有的key
			Set<SelectionKey> keys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = keys.iterator();
			// 当有就绪状态的key时，处理此key
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				keyIterator.remove();
				try {
					handler(key);
				} catch (Exception e) {
					e.printStackTrace();
					if (key != null) {
						key.cancel();
						key.channel().close();
					}
				}
			}
		}
	}

	/**
	 * 根据key的操作位判断是什么类型的请求
	 *
	 * @param key
	 * @throws IOException
	 */
	private void handler(SelectionKey key) throws IOException, InterruptedException {
		if (key.isValid()) {
			// 新的客户端连接请求，创建channel实例，完成TCP的3次握手
			if (key.isAcceptable()) {
				ServerSocketChannel channel = (ServerSocketChannel) key.channel();
				SocketChannel sc = channel.accept();
				// 设置为非阻塞
				sc.configureBlocking(false);
				// 设置客端可读
				sc.register(selector, SelectionKey.OP_READ);
			}
			// 读取数据请求
			if (key.isReadable()) {
				SocketChannel channel = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(100);
				int index = channel.read(buffer);
				if (index > 0) {
					buffer.flip();
					byte[] b = new byte[buffer.remaining()];
					buffer.get(b);
					// 输出接收数据
					System.out.println(new String(b, StandardCharsets.UTF_8));
					// 给客户端响应数据
					channel.write(ByteBuffer.wrap(("服务端已经收到：" + Thread.currentThread().getName()).getBytes()));
				}
				// 返回-1，说明链路已经关闭，直接关闭key和channel
				else if (index < 0) {
					key.cancel();
					key.channel().close();
				}
			}
		}
	}

	public static void main(String[] args) throws IOException {
		NioServer nioServer = new NioServer(8888);
		nioServer.listen();
	}
}
