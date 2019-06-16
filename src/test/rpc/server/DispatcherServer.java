package test.rpc.server;

import test.rpc.common.ServerApi;

import java.io.IOException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DispatcherServer {

	public static void main(String[] args) throws Exception {
		Map<String, Class> impl = new HashMap<>();
		impl.put(ServerApi.class.getName(), ServerImpl.class);
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(8880));
		ExecutorService threadPool = Executors.newFixedThreadPool(10);
		while (true) {
			Socket socket = server.accept();
			byte[] b = new byte[500];
			socket.getInputStream().read(b);
			String request = new String(b);
			threadPool.execute(() -> {
				Class clazz = null;
				String method = null;
				String arg = null;
				for (String s : request.split(",")) {
					int index = s.indexOf("=");
					String type = s.substring(0, index);
					String value = s.substring(index + 1).trim();
					switch (type) {
						case "serviceName":
							clazz = impl.get(value);
							break;
						case "method":
							method = value;
							break;
						case "arg":
							arg = value;
					}
				}
				try {
					Object o = clazz.newInstance();
					Method m = clazz.getMethod(method, String.class);
					String invoke = (String) m.invoke(o, arg);
					socket.getOutputStream().write(invoke.getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					try {
						socket.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			});
		}
	}
}
