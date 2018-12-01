package test.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class Test1Controller extends AbstractController{
	public void testPrint() {
		System.out.println(">>> test1Controller testPrint(); 메소드 호출 <<<");
	}

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		req.setAttribute("result", "Test1Controller에서 넘겨준 값은 <span style='color: red;'>\"임진왜란의 구국영웅\"</span>입니다.");
		req.setAttribute("name", "이순신");
		
		super.setRedirect(false); // forward방식으로 넘어간다고 표시만 해둔 것
		super.setViewPage("/test/test1.jsp");
		/*
			확장자 .java, .xml인 파일에서 view단 페이지의 경로를 나타낼 때 
			맨 앞의 '/'는 http://localhost:port number/context name까지 생략된 것
			
			확장자 .html, .jsp인 파일에서 view단 페이지 경로를 나타낼 때
			맨 앞의 '/'는  http://localhost:port number/ 까지 생략된 것
		 */
	}



}
