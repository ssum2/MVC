package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class CartDelAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) 
		throws Exception {

		String method = req.getMethod();
		
		if(!"POST".equalsIgnoreCase(method)) {
			// POST 방식이 아니라면
			String msg = "비정상적인 경로로 들어왔습니다.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		
		// *** 장바구니 비우기는 반드시 해당사용자가 로그인을 해야만 할수 있다.*** //
		//     관리자는 장바구니 비우기를 할수 없도록 해야 한다.
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser != null && "admin".equals(loginuser.getUserid()) ) {
			
			String msg = "관리자는 장바구니 비우기를 할 수 없습니다.";
			String loc = "javascript:history.back();";
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else if(loginuser != null && !"admin".equals(loginuser.getUserid())) {
			
			String str_cartno = req.getParameter("cartno");
			int cartno = Integer.parseInt(str_cartno);
			ProductDAO pdao = new ProductDAO();
			
			int n = pdao.updateDeleteCart(cartno, 0);
			
			String msg = (n>0)?"장바구니에서 제품 비우기 성공!!":"장바구니에서 제품 비우기 실패!!"; 
			String loc = (n>0)?"cartList.do":"javascript:history.back();"; 
			
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
		}	
		
	}// end of void execute(HttpServletRequest req, HttpServletResponse res)-----------------

}
