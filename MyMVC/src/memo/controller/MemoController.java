package memo.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import my.util.MyUtil;

public class MemoController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#로그인이 되었을 때에만 페이지를 이동시킴
		MemberVO loginuser = super.getLoginUser(req);

		if(loginuser != null) {
		
			super.setRedirect(false); // 안해도 default값이 false
			// super.setRedirect(true) -> sendRedirect --> 페이지 url만 변경됨
			// 현재는 보내줄 key값, DB가 없기 때문에 sendRedirect와 흡사하게 기능
			// forward로 보내는 경우 url이 변하지않음
			super.setViewPage("/WEB-INF/memo/memoForm.jsp");
			// >> view단의 페이지를 띄움(.jsp)
		}
		else {
			return;
		}
		
	}

}
