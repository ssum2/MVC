package member.controller;

import java.util.Date;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class LoginEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#로그인 처리
		String method = req.getMethod();
		if(!"POST".equalsIgnoreCase(method)) {
			// POST방식으로 들어온 것이 아닐 때 DB에 접근하지 못하도록 막음
			req.setAttribute("msg", "비정상적인 경로로 들어왔습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false); // 데이터를 전송해야하기 때문에 forward방식으로
			super.setViewPage("/WEB-INF/msg.jsp"); // view단으로 전송
			
			return;
		}
		else {
	        String userid = req.getParameter("userid");
	        String pwd = req.getParameter("pwd");
	        
	        String saveid = req.getParameter("saveid");
//	        System.out.println("~~>>확인용 saveid: "+saveid);
//	        >> checked -> on / unchecked -> null
	        
	        MemberDAO memberdao = new MemberDAO();
	        MemberVO loginuser = memberdao.loginOKmemberInfo(userid, pwd);
	        
	        if(loginuser==null) {
//	        a) 로그인 실패했을 경우
	        	String msg = "로그인에 실패하였습니다. 아이디 또는 패스워드를 확인해주세요.";
	        	String loc = "javascript:history.back();";
	        	req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
	        }
	        else if(loginuser.isRequireCertify()) {
//	        b) 마지막 로그인 일시가 오래됐을 경우 ---> 인증 요구
				String msg = "마지막 로그인 일시가 6개월 전이므로 휴면계정으로 전환 되었습니다. 관리자에게 문의하세요.";
				String loc = "index.do";
				
				req.setAttribute("msg", msg);
				req.setAttribute("loc", loc);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/msg.jsp");
				
				return;
			}
	        else {
//	        c) 로그인 성공했을 경우
			/*
				#세션(session)
				WAS의 메모리(RAM)의 일부분을 사용하는 저장공간
				세션에 저장된 데이터는 명령어로 삭제하지 않는 한 모든 파일(*.do, *.jsp)에서 사용가능(접근 가능)
				>> 로그인정보는 다양한 기능에서 공유하기 때문에 모든 파일에서 접근가능한 세션을 이용해야함.
				
				#DTO(VO)객체를 넘겨주는 방법
				[1] request로 넘겨주기	: 현재 웹페이지 내에선 얼마든지 가능
					request.setAttribute("dto", dto);
				[2] applicaton으로 넘겨주기
					application.setAttribute("dto", dto);
				[3]-1 session으로 넘겨주기 - 서버에 완전 저장해놓는거
					session.setAttribute("dto", dto);
				[3]-2 request를 통해서 세션 접근해 넘겨주기. 서버x? 짐싸가서 풀기만하면 끝나!
					request.getSession().setAttribute("dto", dto);
				
				[4] 그냥 페이지 자체를 연결해줄 수도 있음
					pageContext.forward("NewFile1.jsp");	// ==> [1] [2] [3] 경우 다 가능!
					response.sendRedirect("NewFile1.jsp");	// ==> [1]로 하면 오류남. 전송이 안됨. sendRediredt는 객체를 못가져감.
															// 	   [2] [3] 경우 가능!; 서버에 저장해두었기 때문에
			 	application > session > request
			 	application에 담아둔 정보를 지우려면 서버를 재구동해야하기 때문에 session을 자주 이용
			 */
	        	
//	        	#session을 통해서 DTO(VO)넘겨주기
//	        	1) 세션 객체 생성
	        	HttpSession session = req.getSession();
	        	
//	        	2) 세션객체에 VO객체 저장하기
	        	session.setAttribute("loginuser", loginuser);
//	        	req.getSession().setAttribute("loginuser", loginuser);
	
		     /*
	        	#쿠키(Cookie); WAS PC가 아닌 Client PC의 디스크공간을 저장공간으로 사용하는 기법
	        	- WAS의 메모리에 넣어두면 과부하가 올 수 있기 때문에 비교적 보안성이 떨어지는 데이터를 사용자의 디스크공간을 활용함
	        	- javax.servlet.http.Cookie 클래스
	         */        	
//				3)아이디저장 체크박스 체크 여부에 따른 userid값 설정
	        	Cookie cookie = new Cookie("saveid", loginuser.getUserid());
//	        	> 기본생성자 없이 파라미터가 있는 생성자만 존재 --> Cookie("key값", 쿠키에 저장할 value)
//	        	3-1) checked(req.getParameter("saveid") == on) ---> 쿠키에 저장
	        	if(saveid !=null) {
	        		cookie.setMaxAge(7*24*60*60);
//	        		cookie.setMaxAge(기한(초)); --> cookie의 저장(수명) 기한
	        	}
//				3-2) unchecked ---> 쿠키 삭제
	        	else {
	        		cookie.setMaxAge(0); // 쿠키의 생존기한을 0초로 맞춤
	        	}
	        	
//	        	3-3) 쿠키를 전송할 경로를 설정하고 쿠키 전송
        		cookie.setPath("/");
			/*	cookie.setPath("사용 경로");
				쿠키가 사용되는 디렉토리 정보 설정. 
				cookie.setPath("/"); 해당 도메인의 모든 페이지에서 사용가능 */
        		
				res.addCookie(cookie);
			/*	사용자 컴퓨터의 웹브라우저로 쿠키를 전송시킨다.
				7일간 사용가능한 쿠키를 전송하든지 아니면 0초 짜리 쿠키(쿠키삭제)를 사용자 컴퓨터의 웹브라우저로 전송시킨다.    */
				
//				d) 마지막 비밀번호 변경일시 limit이 true(변경필요)일 경우 
//				---> 일단 로그인은 되었음 (세션 저장 후)
				
//				#세션에 담겨있는 돌아갈 페이지 주소가 있는지 확인
				String returnPage = (String)session.getAttribute("returnPage");
				
				if(loginuser.isRequirePwdChange()) {
					String msg = "비밀번호 변경 일자가 6개월이 지났습니다. 비밀번호를 변경하세요.";
					String loc = "index.do";
					
					req.setAttribute("msg", msg);
					req.setAttribute("loc", loc);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
					
					return;
				}
				else if(returnPage == null) {
					// 돌아갈 페이지주소가 없는 경우; 그냥 메인에서 로그인 한 경우
					super.setRedirect(true); // 페이지 이동만 실행
					super.setViewPage("index.do"); // login.jsp에서 로그온화면을 구현한 내용이 index.jsp에 include된 모습으로 보임
				}
				else {
					// 로그인 하지 않은 상태에서 장바구니 담기 또는 바로 구매하기 버튼을 눌렀을 때
					super.setRedirect(true);
					super.setViewPage(returnPage);
					
					session.removeAttribute("returnPage");
				}
				
				
	        }
	        
		}
	}

}
