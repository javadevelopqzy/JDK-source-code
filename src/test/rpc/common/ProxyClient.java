package test.rpc.common;

import java.io.*;
import java.lang.reflect.*;
import java.net.*;

public class ProxyClient implements InvocationHandler {

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		Socket socket = new Socket();
		socket.connect(new InetSocketAddress(8880));
		String arg = "serviceName=" + method.getDeclaringClass().getName() + ",arg=" + args[0] + ",method=" + method.getName();
		OutputStream outputStream = socket.getOutputStream();
		outputStream.write(arg.getBytes());
		byte[] b = new byte[100];
		socket.getInputStream().read(b);
		return new String(b).trim();
	}
}
