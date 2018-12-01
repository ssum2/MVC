package myshop.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import my.util.MyUtil;
import myshop.model.*;


public class MallHomeAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res)
		throws Exception {
//		#부모클래스에 선언되어있는 카테고리목록 메소드를 가져와서 로그인폼 밑에 출력
		super.getCategoryList(req);
		String goBackURL = MyUtil.getCurrentURL(req);
		HttpSession session = req.getSession();
		session.setAttribute("returnPage", goBackURL);
		
		ProductDAO pdao = new ProductDAO();
		String pspec = "HIT";
//		#pspec 컬럼의 값에 따라 select
//		1. HIT 상품 가져오기 (더보기 버튼)
//		 - 페이징 처리 하지 않은 것; Ajax 처리하기 이전
		List<ProductVO> hitList = pdao.selectByPspec(pspec);
		
		req.setAttribute("hitList", hitList);
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/myshop/mallHome.jsp"); 
		
		// 2. NEW 상품 가져오기 
		
		
	} // end of excute()
} // end of class
