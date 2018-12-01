package myshop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import my.util.MyUtil;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class ProdViewAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

//		#부모클래스에 선언되어있는 카테고리목록 메소드를 가져와서 로그인폼 밑에 출력
		super.getCategoryList(req);
		
		String str_pnum = req.getParameter("pnum");
	
		try {
			int pnum = Integer.parseInt(str_pnum);
			ProductDAO pdao = new ProductDAO();
			ProductVO pvo = pdao.getProductOneByPnum(pnum);
			List<HashMap<String, String>> attachImageList = pdao.selectAttachImage(pnum);
			
			String goBackURL = MyUtil.getCurrentURL(req);
			HttpSession session = req.getSession();
			session.setAttribute("returnPage", goBackURL);
			
			if(pvo==null) {
				req.setAttribute("str_pnum", str_pnum);
				
				super.setRedirect(false);
				super.setViewPage("/WEB-INF/myshop/errorNoProduct.jsp");
				
				return;
			}
			
			req.setAttribute("pvo", pvo);
			req.setAttribute("attachImageList", attachImageList);
			req.setAttribute("goBackURL", goBackURL);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/prodView.jsp");
			
		} catch(NumberFormatException e){
			
			req.setAttribute("str_pnum", str_pnum);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/errorNoProduct.jsp");
			
			return;
		}
	}

}
