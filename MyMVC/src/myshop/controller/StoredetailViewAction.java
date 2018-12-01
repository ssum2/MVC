package myshop.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import myshop.model.StoremapVO;

public class StoredetailViewAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String storeno = req.getParameter("storeno");
		
		
		InterProductDAO pdao = new ProductDAO();
		
//		#강사님이 하신 것
		List<HashMap<String,String>> storeDetailList = pdao.getStoreDetailList(storeno);
		
//		#내가 한 것
		StoremapVO storeInfo = pdao.getStoreDetail(storeno);
		
		req.setAttribute("storeDetailList", storeDetailList);
		req.setAttribute("storeInfo", storeInfo);
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/storeDetailView.jsp");
	}

}
