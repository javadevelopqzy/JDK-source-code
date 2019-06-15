package test.rpc.server;

import test.rpc.common.*;

public class ServerImpl implements ServerApi {

	@Override
	public String sayHi(String arg) {
		System.out.println("收到请求入参：" + arg);
		return "服务端返回信息";
	}
}
