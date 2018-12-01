package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class IdDuplicateCheckAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		if("POST".equalsIgnoreCase(method)) {
			MemberDAO memberdao = new MemberDAO();
			String userid = req.getParameter("userid");
			System.out.println(userid);
			boolean isUseUserid = memberdao.idDuplicateCheck(userid);
			
			req.setAttribute("isUseUserid", isUseUserid);
			req.setAttribute("userid", userid);
		}
		req.setAttribute("method", method);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/member/idcheck.jsp");
	}

}
