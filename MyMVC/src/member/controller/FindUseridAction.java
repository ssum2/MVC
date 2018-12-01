package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;


public class FindUseridAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		String name = "";
		String mobile ="";
		if("POST".equalsIgnoreCase(method)) {
//		>> 찾기 버튼을 눌렀을 때(POST 방식으로 submit) 
			name = req.getParameter("name");
			mobile = req.getParameter("mobile");
			
			MemberDAO memberdao = new MemberDAO();
			String userid = memberdao.findUserid(name, mobile);
			
			if(userid != null) {
//			>> DAO에서 전해준 userid가 존재 할 때	
				req.setAttribute("userid", userid);
			}
			else {
				req.setAttribute("userid", "입력하신 정보와 일치하는 회원이 없습니다.");
			}
			
		}
		req.setAttribute("name", name);
		req.setAttribute("mobile", mobile);
		req.setAttribute("method", method);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/idFind.jsp");

	}

}
