package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		String str_idx =req.getParameter("idx");
		String currentURL = req.getParameter("currentURL");
		int idx = Integer.parseInt(str_idx);
		System.out.println(str_idx);
		System.out.println("MemberEditAction 1/3 success");
		
		if(loginuser == null) {
			req.setAttribute("msg", "로그인 후 사용 가능합니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else if(!("admin".equalsIgnoreCase(loginuser.getUserid())) && loginuser.getIdx()!=idx) {

			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
			MemberDAO memberdao = new MemberDAO();
			MemberVO membervo = memberdao.getOneMember(idx);
			System.out.println("MemberEditAction 2/3 success");
			if(membervo==null) {
				req.setAttribute("msg", "해당 회원이 없습니다");
				req.setAttribute("loc", currentURL);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");

				return;
			}
			else {
				System.out.println("MemberEditAction 3/3 success");
				req.setAttribute("membervo", membervo);
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/member/memberEdit.jsp");
			}
		}
	}
	

}
