package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class CartEditAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		System.out.println("CartEditAction 1/5 success");
		
		String method = req.getMethod();
		if(!"POST".equalsIgnoreCase(method)) return;
		
		System.out.println("CartEditAction 2/5 success");
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser==null) {
			req.setAttribute("msg", "로그인 후 사용 가능합니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else if(loginuser!=null && !"admin".equals(loginuser.getUserid())) {
			System.out.println("CartEditAction 3/5 success");
			
			String str_cartno = req.getParameter("cartno");
			int cartno = Integer.parseInt(str_cartno);
			String str_oqty = req.getParameter("oqty");
			int oqty = Integer.parseInt(str_oqty);
	
			ProductDAO pdao = new ProductDAO();
			int result = pdao.updateDeleteCart(cartno, oqty);
			
			System.out.println("CartEditAction 4/5 success");
			
			String msg = (result>0)?"장바구니 수정이 완료되었습니다.":"수정 실패 ㅜㅜ";
			String loc = (result>0)?"cartList.do":"javascript:history.back();";
			
			System.out.println("CartEditAction 5/5 success");
			req.setAttribute("msg", msg);
			req.setAttribute("loc", loc);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
		}
	} // end of execute

} // end of class
