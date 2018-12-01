package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;

public class VerifyCertificationAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String userid = req.getParameter("input_userid");
		String userCertificationCode = req.getParameter("userCertificationCode");
		HttpSession session = req.getSession();
		String certificationCode = (String)session.getAttribute("certificationCode");
		
		String msg="";
		String loc="";
		
		if(certificationCode.equals(userCertificationCode)) {
			msg ="인증성공";
			loc = req.getContextPath()+"/pwdConfirm.do?userid="+userid;
		}
		else {
			msg="인증코드가 일치하지 않습니다.";
			loc=req.getContextPath()+"/pwdFind.do";
		}
		req.setAttribute("msg", "회원가입에 실패하였습니다.");
		req.setAttribute("loc", "javascript:history.back();");
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/msg.jsp");
		
		session.removeAttribute("certificationCode");
	}

}
