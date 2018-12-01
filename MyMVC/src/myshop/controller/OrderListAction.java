package myshop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class OrderListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser==null) return;
		
//		#일반사용자는 자신의 내역만 조회, 관리자는 모든 사용자의 내역을 조회할 수 있도록 나눔
		String userid = loginuser.getUserid();
		ProductDAO pdao = new ProductDAO();
		List<HashMap<String,String>> orderList =pdao.getOrderList(userid);
		
		req.setAttribute("orderList", orderList);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/orderList.jsp");
		
	} // end of execute
} // end of class
