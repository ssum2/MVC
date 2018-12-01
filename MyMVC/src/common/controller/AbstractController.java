package common.controller;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import member.model.MemberVO;
import myshop.model.CategoryVO;
import myshop.model.ProductDAO;

public abstract class AbstractController implements Command {
	// 추상클래스
	private boolean isRedirect = false;
	/*	#개발자 지정 규칙
	 	View 페이지(.jsp 페이지)에 sendRedirect 방법으로 이동 할 때; 
	 		isRedirect = true
	 	View 페이지(.jsp 페이지)에 forward 방법으로 이동 할 때; 
	 		isRedirect = false
	 */
	
	private String viewPage; // view에서 사용할 page

	public boolean isRedirect() {
		return isRedirect;
		// >> return타입이 boolean인 경우 get이 아니라 is~~
	}

	public void setRedirect(boolean isRedirect) {
		this.isRedirect = isRedirect;
	}

	public String getViewPage() {
		return viewPage;
	}

	public void setViewPage(String viewPage) {
		this.viewPage = viewPage;
	}
	
//	#현재 세션에 객체가 있는지 없는지 알려주는 메소드 (로그인 유무 검사) --> return MemberVO or null
	public MemberVO getLoginUser(HttpServletRequest req) {
		MemberVO loginuser = null;
		
		HttpSession session = req.getSession();
		loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) { // 로그인 객체가 없을 때
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "javascript:history.back();";
        	
        	req.setAttribute("msg", msg);
        	req.setAttribute("loc", loc);
        	
        	isRedirect = false;
        	viewPage = "/WEB-INF/msg.jsp";
		}
		return loginuser;
	}
	
//	#이 클래스를 부모클래스로 두고 있는 자식클래스에서 카테고리 목록을 보여주는 메소드
//	 >> jsp_category테이블에서 code, cname컬럼을 가져와서 request영역에 저장
	public void getCategoryList(HttpServletRequest req) throws SQLException {
		ProductDAO pdao = new ProductDAO();
		
		List<CategoryVO> categoryList = pdao.getCategoryList();
		
		req.setAttribute("categoryList", categoryList);
		
	}
}
