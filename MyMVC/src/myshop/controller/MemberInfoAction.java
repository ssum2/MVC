package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class MemberInfoAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		if(!"admin".equals(loginuser.getUserid())){
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
		String odrcode = req.getParameter("odrcode");
		
		ProductDAO pdao = new ProductDAO();
		MemberVO membervo = pdao.getOneUserByOdrcode(odrcode);
		
		req.setAttribute("membervo", membervo);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/admin/memberInfo.jsp");
		
	}

}
