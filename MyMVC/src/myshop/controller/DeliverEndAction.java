package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;

public class DeliverEndAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String method = req.getMethod();
		if(!"POST".equalsIgnoreCase(method)) {
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		MemberVO loginuser = super.getLoginUser(req);
		String admin = loginuser.getUserid();
		if(loginuser == null || !"admin".equals(admin))  return;
		
		
//		#체크박스 및 주문코드가 여러개이기 때문에 getParameterValues로 값을 가져옴
		String[] pnumArr = req.getParameterValues("deliverEndPnum");
		String[] odrcodeArr = req.getParameterValues("odrcode");
		
//		#in()에 넣기 좋은 형태로 문자열 배열 가공; 's20181122-1/1', 's20181122-11/3'
//										~odrcode||pnum
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<odrcodeArr.length; i++) {
			String comma = (i < pnumArr.length-1)? ",": "";
			sb.append("\'"+odrcodeArr[i]+"/");
			sb.append(pnumArr[i]+"\'"+comma);
		}
		String odrcodePnum = sb.toString();
		System.out.println(odrcodePnum);
		
		InterProductDAO pdao = new ProductDAO();	
		// InterProductDAO로 받아서 사용 가능(다형성) --> 인터페이스에 정의된 메소드만 사용하겠다는 의미
		int result = pdao.updateDeliverEnd(odrcodePnum, odrcodeArr.length);
		if(result==1) {
			req.setAttribute("msg", "선택하신 주문에 대하여 배송상태가 '배송완료'로 변경되었습니다.");
			req.setAttribute("loc", "orderList.do");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		else {
			req.setAttribute("msg", "배송상태 변경에 실패하였습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			return;
		}
		
	}

}
