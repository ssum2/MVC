package myshop.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;

public class CoinAddUpdateLoginuserAction extends AbstractController {
 
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser==null) return; // 로그인하지 않았을 때 return
		System.out.println("CoinAddUpdateLoginuserAction 1/6 success");
		
		int idx = loginuser.getIdx();
		String str_input_idx = req.getParameter("idx");
		int input_idx = Integer.parseInt(str_input_idx);
		
		if(idx!=input_idx) {
			// 다른 사용자의 코인충전 X
			System.out.println("CoinAddUpdateLoginuserAction 2/6 success");
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/msg.jsp");
			return;
		}
		else {
			HttpSession session = req.getSession();
			System.out.println("CoinAddUpdateLoginuserAction 2/6 success");
			@SuppressWarnings("unchecked")
			HashMap<String, String> coinmap = (HashMap<String, String>)session.getAttribute("coinmap");
			System.out.println("CoinAddUpdateLoginuserAction 3/6 success");
			
			String map_idx = coinmap.get("idx");
			idx = Integer.parseInt(map_idx);
			System.out.println("CoinAddUpdateLoginuserAction map의 idx: "+idx);
			System.out.println("CoinAddUpdateLoginuserAction input_idx: "+input_idx);
			
			String str_coinmoney = coinmap.get("coinmoney");
			int coinmoney = Integer.parseInt(str_coinmoney);
			
			int result = 0;
			if(idx == input_idx) {
				System.out.println("CoinAddUpdateLoginuserAction 4/6 success");
				MemberDAO memberdao = new MemberDAO();
				result = memberdao.coinAddUpdate(idx, coinmoney);
				System.out.println("CoinAddUpdateLoginuserAction update result: "+result);
			}
			
			String msg = "";
			String loc = req.getContextPath()+"/index.do";
			if(result == 1) {	// DB 업데이트 성공 시
				System.out.println("CoinAddUpdateLoginuserAction 5/6 success");
				msg = loginuser.getUserid() + "["+loginuser.getName()+"]님의 코인지갑으로 "+coinmoney+"코인이 충전 되었습니다.";
				loginuser.setCoin(loginuser.getCoin()+coinmoney); // 세션에 저장된 로그인유저객체의 코인값을 변경
				loginuser.setPoint(loginuser.getPoint()+coinmoney/100); // 세션에 저장된 로그인유저객체의 포인트값을 변경
				session.setAttribute("loginuser", loginuser); // 변경된 객체를 세션에 덮어 씌움	
			}
			else {
				
				msg="코인 충전에 실패하였습니다.";
			}
			System.out.println("CoinAddUpdateLoginuserAction 6/6 success");
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);

			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			session.removeAttribute("coinmap"); // 세션에 저장되어있는 coinmap 객체를 삭제
		}
		
		
		
	}

}
