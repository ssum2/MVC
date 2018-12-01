package myshop.controller;

import java.util.HashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseEndAction extends AbstractController {
 
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser==null) return; // 로그인하지 않았을 때 return
		
		String str_coinmoney = req.getParameter("coinmoney");
		int coinmoney = Integer.parseInt(str_coinmoney);
		
		String name = loginuser.getName();
		int idx = loginuser.getIdx();
		String str_input_idx = req.getParameter("idx");
		int input_idx = Integer.parseInt(str_input_idx);

		if(idx!=input_idx) {
			// 다른 사용자의 코인충전 X
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
			req.setAttribute("idx", idx);
			req.setAttribute("coinmoney", coinmoney);
			req.setAttribute("name", name);
			
			HashMap<String, String> coinmap = new HashMap<String, String>();
			coinmap.put("idx", str_input_idx);
			coinmap.put("coinmoney", str_coinmoney);
			
			HttpSession session = req.getSession();
			session.setAttribute("coinmap", coinmap);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/paymentGateway.jsp");
		}
	}

}
