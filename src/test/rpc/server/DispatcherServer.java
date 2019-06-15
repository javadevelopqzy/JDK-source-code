package test.rpc.server;

import java.lang.reflect.*;
import java.net.*;
import java.util.*;

import test.rpc.common.*;

public class DispatcherServer {

	public static void main(String[] args) throws Exception {
		Map<String, Class> impl = new HashMap<>();
		impl.put(ServerApi.class.getName(), ServerImpl.class);
		ServerSocket server = new ServerSocket();
		server.bind(new InetSocketAddress(8880));
		while (true) {
			Socket socket = server.accept();
			byte[] b = new byte[500];
			socket.getInputStream().read(b);
			String request = new String(b);
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
			Object o = clazz.newInstance();
			Method m = clazz.getMethod(method, String.class);
			String invoke = (String) m.invoke(o, arg);
			socket.getOutputStream().write(invoke.getBytes());
		}
	}
}
