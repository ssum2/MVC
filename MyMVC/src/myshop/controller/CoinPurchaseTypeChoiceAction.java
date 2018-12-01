package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;

public class CoinPurchaseTypeChoiceAction extends AbstractController {
 
	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser==null) return; // 로그인하지 않았을 때 return
		
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
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/coinPurchaseTypeChoice.jsp");
		}
	}

}
