package member.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;
import memo.model.MemoDAO;
import memo.model.MemoVO;

public class MemberRegisterEndAction extends AbstractController {
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
			// POST방식으로 들어왔을 때
			String userid = req.getParameter("userid");
			String pwd = req.getParameter("pwd");
			String name = req.getParameter("name");
			String email = req.getParameter("email");
			String hp1 = req.getParameter("hp1");
			String hp2 = req.getParameter("hp2");
			String hp3 = req.getParameter("hp3");
			String post1 = req.getParameter("post1");
			String post2 = req.getParameter("post2");
			String addr1 = req.getParameter("addr1");
			String addr2 = req.getParameter("addr2");
			String gender = req.getParameter("gender");
			String bd = req.getParameter("birthday");
			String birthday = "";
			String[] arr = bd.split("/");
			for(String b:arr) {
				birthday += b;
			}
			
			
//			#받아온 값들을 VO에 셋팅
			MemberVO membervo = new MemberVO();
			membervo.setUserid(userid);
			membervo.setPwd(pwd);
			membervo.setName(name);
			membervo.setEmail(email);
			membervo.setHp1(hp1);
			membervo.setHp2(hp2);
			membervo.setHp3(hp3);
			membervo.setPost1(post1);
			membervo.setPost2(post2);
			membervo.setAddr1(addr1);
			membervo.setAddr2(addr2);
			membervo.setGender(gender);
			membervo.setBirthday(birthday);

			MemberDAO memberdao = new MemberDAO();
			int n = memberdao.registerMember(membervo);
			if(n==1) {
				// 정상적으로 insert 완료되었을 때
				req.setAttribute("msg", "회원 가입 성공!");
				req.setAttribute("loc", "index.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
			else {
				// insert실패했을 때
				req.setAttribute("msg", "회원가입에 실패하였습니다.");
				req.setAttribute("loc", "javascript:history.back();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}

		}
		
	}

}
