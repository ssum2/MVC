package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberRecoveryAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		if(!"POST".equalsIgnoreCase(method)) {
			// POST방식으로 들어온 것이 아닐 때 DB에 접근하지 못하도록 막음
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다.");
			req.setAttribute("loc", "javascript:history.back();"); 
			// >> history.back() --> 이전 상태의 스냅샷을 저장해두고 거기로 이동
			
			super.setRedirect(false); // 데이터를 전송해야하기 때문에 forward방식으로
			super.setViewPage("/WEB-INF/msg.jsp"); // view단으로 전송
			
			return;
		}
		else {
			MemberVO loginuser = super.getLoginUser(req);
		
			if(loginuser == null) {
				req.setAttribute("msg", "로그인 후 사용 가능합니다.");
				req.setAttribute("loc", "javascript:history.back();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			}
			else if(!("admin".equalsIgnoreCase(loginuser.getUserid()))) {
				System.out.println("MemberRecoveryAction userid: "+loginuser.getUserid());
				
				req.setAttribute("msg", "권한이 없습니다.");
				req.setAttribute("loc", "javascript:history.back();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				return;
			}
			else {
				MemberDAO memberdao = new MemberDAO();
				String str_idx = req.getParameter("idx");
				int idx = Integer.parseInt(str_idx);
				String currentURL = req.getParameter("currentURL");
				System.out.println("MemberDeleteAction의 currentURL: "+currentURL);
				int result = memberdao.recoveryMember(idx);
				
				if(result==1) {
					req.setAttribute("msg", "회원 복원 성공");
					req.setAttribute("loc", currentURL);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");	
				}
				else {
					req.setAttribute("msg", "회원 복원 실패");
					req.setAttribute("loc", "javascript:history.back();");
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					return;
				}
			}
		}
	}
}
