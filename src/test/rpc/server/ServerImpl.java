package test.rpc.server;

import test.rpc.common.*;

public class ServerImpl implements ServerApi {

	@Override
	public String sayHi(String arg) throws InterruptedException {
		System.out.println("收到请求入参：" + arg);
		// 假设业务处理耗时500毫秒
		Thread.sleep(500);
		return "服务端返回信息";
	}
}
