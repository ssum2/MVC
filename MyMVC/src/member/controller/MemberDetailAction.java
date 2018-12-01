package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberDetailAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		String str_idx =req.getParameter("idx");
		String currentURL = req.getParameter("currentURL");
		int idx = 0;
//		#idx가 숫자가 아닌 문자열이 들어왔을 때
		try{
			idx = Integer.parseInt(str_idx);
		} catch(NumberFormatException e){
			idx= -1;
		}
		if(loginuser == null) {
			req.setAttribute("msg", "로그인 후 사용 가능합니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else if(!("admin".equalsIgnoreCase(loginuser.getUserid())) && loginuser.getIdx() != idx) {
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
			MemberDAO memberdao = new MemberDAO();
			MemberVO membervo = memberdao.getOneMember(idx);
			
//			member객체가 null일때(idx가 잘못되었을 때)
			if(membervo == null){
				req.setAttribute("msg", "해당 회원이 없습니다");
				req.setAttribute("loc", currentURL);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");

				return;
			}
			else {
				req.setAttribute("membervo", membervo);
				req.setAttribute("currentURL", currentURL);
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/member/memberDetail.jsp");
			}
		}
	}


}
