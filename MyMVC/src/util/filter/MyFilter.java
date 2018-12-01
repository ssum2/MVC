package util.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/*
<< 필터 적용 순서 >>
1. Filter 인터페이스를 구현하는 자바 클래스를 생성. --> 생성하고 난 다음 서버를 재구동 해야함
2. /WEB-INF/web.xml 에 filter 엘리먼트를 사용하여 필터 클래스를 등록하는데
     하지만 web.xml 을 사용하지 않고 @WebFilter 어노테이션을 많이 사용한다.
*/
public class MyFilter implements Filter {

	@Override
	public void destroy() {
		// 필터 인스턴스를 종료시키기 전에 호출하는 메소드
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		request.setCharacterEncoding("UTF-8");
		chain.doFilter(request, response);
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// 서블릿 컨테이너가 필터 인스턴스를 초기화하기 위해서 호출하는 메소드, 내용 기술X
		
	}

}
