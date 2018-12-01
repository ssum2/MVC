package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class MemberEditEndAction extends AbstractController {
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		System.out.println("MemberEditEndAction 1/5 success");
		/*String method = req.getMethod();
		if(!"POST".equalsIgnoreCase(method)) {
			req.setAttribute("msg", "잘못된 경로로 들어왔습니다.");
			req.setAttribute("loc", "javascript:history.back();"); 
			super.setRedirect(false); 
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {*/
			String str_idx = req.getParameter("idx");
			int idx = Integer.parseInt(str_idx);
			String name = req.getParameter("name");
			String pwd = req.getParameter("pwd");
			String email = req.getParameter("email");
			String hp1 = req.getParameter("hp1");
			String hp2 = req.getParameter("hp2");
			String hp3 = req.getParameter("hp3");
			String post1 = req.getParameter("post1");
			String post2 = req.getParameter("post2");
			String addr1 = req.getParameter("addr1");
			String addr2 = req.getParameter("addr2");
			System.out.println("MemberEditEndAction name: "+name);
			System.out.println("MemberEditEndAction 2/5 success");
			
//			#받아온 값들을 VO에 셋팅
			MemberVO membervo = new MemberVO();
			
			membervo.setIdx(idx);
			membervo.setName(name);
			membervo.setPwd(pwd);
			membervo.setEmail(email);
			membervo.setHp1(hp1);
			membervo.setHp2(hp2);
			membervo.setHp3(hp3);
			membervo.setPost1(post1);
			membervo.setPost2(post2);
			membervo.setAddr1(addr1);
			membervo.setAddr2(addr2);
			System.out.println("MemberEditEndAction 3/5 success");
			
			MemberDAO memberdao = new MemberDAO();
			int result = memberdao.updateMember(membervo);
			System.out.println("MemberEditEndAction 4/5 success");
			System.out.println("MemberEditEndAction: "+result);
			
			if(result ==1) {	// 정상적으로 update 완료되었을 때
//				#로그인탭의 이름부분에 변경된 이름으로 들어오도록 수정 --> 세션 수정
				loginuser.setName(name);
				HttpSession session = req.getSession();
//				session.removeAttribute("loginuser"); // 기존 세션을 없앰 --> 생략 가능
				session.setAttribute("loginuser", loginuser); // 새로운 세션값을 넣어줌
				
				System.out.println("MemberEditEndAction 5/5 success");
				req.setAttribute("msg", "회원 정보 수정 성공!");
				req.setAttribute("loc", "memberDetail.do");
				req.setAttribute("script1", "window.opener.location.reload();\n" + "window.close();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
			else {
				System.out.println("MemberEditEndAction 5/5 success");
				req.setAttribute("msg", "회원 정보 수정 실패!");
				req.setAttribute("loc", "memberList.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
//		}
	}
	
}
