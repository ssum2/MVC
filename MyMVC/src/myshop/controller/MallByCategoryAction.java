package myshop.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class MallByCategoryAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		super.getCategoryList(req);
		
		String code = req.getParameter("code");
		ProductDAO pdao = new ProductDAO();

		List<ProductVO> productListBycategory = pdao.selectByCode(code);
		String cname = pdao.getCnameByCode(code);
		if(cname==null) {
			req.setAttribute("str_pnum", code);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/myshop/errorNoProduct.jsp");
			
			return;
		}
		req.setAttribute("productListBycategory", productListBycategory);
		req.setAttribute("cname", cname);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallByCategory.jsp"); 
		
/*		[강사님 버전]
		List<HashMap<String, String>> prodByCategoryList = pdao.getProductsByCategory(code);
		
		if(prodByCategoryList == null) {
           req.setAttribute("str_pnum", code);

           super.setRedirect(false);
           super.setViewPage("/WEB-INF/myshop/errorNotProduct.jsp");
           return;
		}
		
		req.setAttribute("prodByCategoryList", prodByCategoryList);
		req.setAttribute("cname", prodByCategoryList.get(0).get("CNAME")); 
   	
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallByCategory.jsp");  
*/
	}

}
