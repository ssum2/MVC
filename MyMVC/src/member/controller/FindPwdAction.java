package member.controller;

import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;

public class FindPwdAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		String userid = "";
		String email ="";
		HttpSession session = req.getSession();
		if("POST".equalsIgnoreCase(method)) {
//		>> 찾기 버튼을 눌렀을 때(POST 방식으로 submit) 
			userid = req.getParameter("userid");
			email = req.getParameter("email");
			
			MemberDAO memberdao = new MemberDAO();
			int n = memberdao.isUserExists(userid, email);
			
			if(n == 1) {
//			>> DAO에서 전해준 userid가 존재 할 때	이메일에 인증키 전송
				GoogleMail mail = new GoogleMail();
				
//				#랜덤 인증키 생성
				Random rnd = new Random();
				
				String certificationCode = "";
//				>> 영문자, 숫자 혼용된 인증키 랜덤 생성
				
//				1) a~z사이의 랜덤한 영소문자 5개 가져오기
				char rndchar = ' ';
				for(int i=0; i<5; i++) {
//					min부터 max사이의 값으로 랜덤한 정수를 얻으려면 int rndnum = rnd.nextInt(max-min+1)+min;
//					rnd.nextInt(122-97+1)+97; ---> rnd.nextInt('z'-'a'+1)+'a';
					rndchar = (char)(rnd.nextInt(26)+97);
					certificationCode+=rndchar;
				}
				
//				2) 0~9사이의 랜덤한 숫자 7개 가져오기
				int rndnum=0;
				for(int i=0; i<7; i++) {
					rndnum = rnd.nextInt(10);
					certificationCode+=rndnum;
				}

//				3) 랜덤생성된 인증키를 사용자 이메일로 전송
				try {
					mail.sendmail(email, certificationCode);
					session.setAttribute("certificationCode", certificationCode);
					
				} catch (Exception e) {
//				>> 메일 발송 실패시
					e.printStackTrace();
					n=-1;
					req.setAttribute("sendFailmsg", "메일 전송 실패하였습니다. 이메일 주소를 확인해주세요.");
				}
				
			}
			req.setAttribute("n", n);
			/*
				n == 0 : 존재하지 않은 userid, email인 경우(회원x)
				n == 1 : 회원 존재
				n == -1 : 회원은 존재하지만 email이 유효하지 않음
			 */
			
			req.setAttribute("userid", userid);
			req.setAttribute("email", email);
		}

		req.setAttribute("method", method);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/login/pwdFind.jsp");
		
	}

}
