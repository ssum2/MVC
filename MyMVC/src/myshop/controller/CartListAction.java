package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.CartVO;
import myshop.model.ProductDAO;

public class CartListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		super.getCategoryList(req);
		
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) {
			
			req.setAttribute("msg", "로그인 후 사용 가능합니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else if ("admin".equals(loginuser.getUserid())){
			req.setAttribute("msg", "해당 유저만 사용 가능합니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {
			ProductDAO pdao = new ProductDAO();
//			1) 페이징 처리 전 장바구니 목록 출력하기
			List<CartVO> cartList = pdao.getCartList(loginuser.getUserid());
			
			 req.setAttribute("cartList", cartList);
			 
			 super.setRedirect(false);
			 super.setViewPage("/WEB-INF/myshop/cartList.jsp");
			
//			2) 페이징 처리 후 장바구니 목록 출력하기
			
			
		}
	
	}

}
