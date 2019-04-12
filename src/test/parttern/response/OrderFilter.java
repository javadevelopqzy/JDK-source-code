package test.parttern.response;

public class OrderFilter implements Filter {

	@Override
	public void invoke(Filter filter) {
		System.out.println("处理order的逻辑");
		filter.invoke(this);
	}
}
