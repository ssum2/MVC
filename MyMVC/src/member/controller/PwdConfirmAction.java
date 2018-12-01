package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;

public class PwdConfirmAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String userid = req.getParameter("userid");
		String method = req.getMethod();
		
		if("POST".equalsIgnoreCase(method)) {
			String pwd = req.getParameter("pwd");
			
			MemberDAO memberdao = new MemberDAO();
			int n = memberdao.changeNewPwd(userid, pwd);
			req.setAttribute("n", n);
		}
		
		req.setAttribute("method", method);
		req.setAttribute("userid", userid);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdChange.jsp");
		
	}

}
