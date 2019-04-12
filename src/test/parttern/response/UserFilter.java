package test.parttern.response;

public class UserFilter implements Filter {

	@Override
	public void invoke(Filter filter) {
		System.out.println("处理User的逻辑");
		filter.invoke(this);
	}
}
