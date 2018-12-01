package myshop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class ProductRegisterAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		#admin으로 로그인 했을 경우에만 접근 가능하도록 제약
		MemberVO loginuser = super.getLoginUser(req);
		System.out.println("ProductRegisterAction 1/6 success");
		if(loginuser == null) return;
		
		String userid = loginuser.getUserid();
		if(!"admin".equals(userid)) {
			System.out.println("ProductRegisterAction 2/6 success");
			
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
//			#admin으로 접속했을 경우
			
//			1) DAO작업
			System.out.println("ProductRegisterAction 2/6 success");
			ProductDAO pdao = new ProductDAO();
			List<HashMap<String, String>> categoryList = pdao.selectCategory();
			System.out.println("ProductRegisterAction 3/6 success");
			
			List<HashMap<String, String>> specList = pdao.selectSpec();
			System.out.println("ProductRegisterAction 4/6 success");
			
//			2) attribute 셋팅
			req.setAttribute("categoryList", categoryList);
			req.setAttribute("specList", specList);
			System.out.println("ProductRegisterAction 5/6 success");
			
//			3) 전송
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/admin/productRegister.jsp");
			System.out.println("ProductRegisterAction 6/6 success");
		}
	}

}
