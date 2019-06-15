package test.nio;

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.nio.charset.*;
import java.util.*;

public class NioClient {

	SocketChannel channel;

	Selector selector;

	public NioClient() throws IOException {
		channel = SocketChannel.open();
		channel.configureBlocking(false);
		selector = Selector.open();
	}

	public void connect() throws IOException {
		doConnect();
		selector.select(100);

		// 轮询所有的key
		Set<SelectionKey> keys = selector.selectedKeys();
		Iterator<SelectionKey> keyIterator = keys.iterator();
		while (true) {
			// 当有就绪状态的key时，处理此key
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				keyIterator.remove();
				try {
					handlerInput(key);
				} catch (Exception e) {
					// 出现异常关闭key
					if (key != null) {
						key.cancel();
						if (key.channel() != null) {
							key.channel().close();
						}
					}
				}
			}
		}
		/*
		// 关闭selector
		if (selector != null) {
			selector.close();
		}
		*/
	}

	private void handlerInput(SelectionKey key) throws IOException {
		if (key.isValid()) {
			// 判断是否连接成功
			if (key.isConnectable()) {
				SocketChannel channel = (SocketChannel) key.channel();
				// 连接成功则给服务端发送消息
				if (channel.finishConnect()) {
					channel.register(selector, SelectionKey.OP_READ);
					ByteBuffer wrap = ByteBuffer.wrap("发送消息给服务端！".getBytes());
					channel.write(wrap);
				} else {
					System.exit(1);
				}
			}
			// 读取数据请求
			if (key.isReadable()) {
				SocketChannel channel = (SocketChannel) key.channel();
				ByteBuffer buffer = ByteBuffer.allocate(1024);
				int index = channel.read(buffer);
				if (index > 0) {
					// 切换读模式
					buffer.flip();
					byte[] bytes = new byte[buffer.remaining()];
					// 读取byte
					buffer.get(bytes);
					// 输出接收数据
					System.out.println(new String(bytes, StandardCharsets.UTF_8));
					// 给客户端响应数据
					channel.write(ByteBuffer.wrap("客户端已经收到".getBytes()));
				}
				// 返回-1，说明链路已经关闭，直接关闭key和channel
				else if (index < 0) {
					key.cancel();
					key.channel().close();
				}
			}
		}
	}

	private void doConnect() throws IOException {
		// 连接服务端
		if (channel.connect(new InetSocketAddress(8888))) {
			// 如果连接成功直接发送请求
			channel.register(selector, SelectionKey.OP_READ);
			ByteBuffer wrap = ByteBuffer.wrap("发送消息给服务端！".getBytes());
			channel.write(wrap);
			if (wrap.hasRemaining()) {
				System.out.println(new String(wrap.array()));
			}
		} else {
			// 连接不成功，发起重连
			channel.register(selector, SelectionKey.OP_CONNECT);
		}
	}

	public static void main(String[] args) throws IOException {
		NioClient client = new NioClient();
		client.connect();
	}
}
