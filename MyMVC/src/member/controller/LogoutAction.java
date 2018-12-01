package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;


public class LogoutAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#로그아웃 처리
		HttpSession session = req.getSession();
		session.removeAttribute("loginuser");
//		>> session에 저장되어있는 atrribute의 key값을 넣으면 해당 객체가 삭제
//		로그인한 객체를 삭제하여 해당 객체를 null로 만듦 ----> 로그인 전의 상태와 동일해짐
		super.setRedirect(true); // 페이지 이동
		super.setViewPage("index.do");
/*		
		String returnPage = (String)session.getAttribute("returnPage");
		
		if(returnPage==null) {
			super.setRedirect(true); // 페이지 이동
			super.setViewPage("index.do");
		}
		else {
			super.setRedirect(true); 
			super.setViewPage(returnPage);
			session.removeAttribute("returnPage");
		}
*/		
		
	}

}
