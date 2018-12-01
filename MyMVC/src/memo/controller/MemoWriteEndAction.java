package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import memo.model.MemoDAO;
import memo.model.MemoVO;

public class MemoWriteEndAction extends AbstractController {

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
			MemberVO loginuser = (MemberVO)req.getSession().getAttribute("loginuser");
			String fk_userid = loginuser.getUserid();
			String name = loginuser.getName();
			String msg = req.getParameter("msg");
			
			// 클라이언트 IP주소 알아오기
			String cip = req.getRemoteAddr();
//			request.getRemoteAddr(); --> 현재 접근하고 있는 클라이언트의 ip주소를 String타입으로 반환
			
			System.out.println("아이디: "+fk_userid);
			System.out.println("이름: "+name);
			System.out.println("메세지: "+msg);
			System.out.println("cip: "+cip);
			
//			#받아온 값들을 VO에 셋팅
			MemoVO memovo = new MemoVO();
			memovo.setFk_userid(fk_userid);
			memovo.setName(name);
			memovo.setMsg(msg);
			memovo.setCip(cip);
			
			MemoDAO memodao = new MemoDAO();
			int n = memodao.memoInsert(memovo);
			if(n==1) {
				// 정상적으로 insert 완료되었을 때
				req.setAttribute("msg", "메모 작성이 완료되었습니다.");
				req.setAttribute("loc", "memoList.do");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
			else {
				// insert실패했을 때
				req.setAttribute("msg", "메모 작성에 실패하였습니다.");
				req.setAttribute("loc", "javascript:history.back();");
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
			}
			
			super.setViewPage("/WEB-INF/memoWriteEnd.jsp");
			
			
		}
	}

}
