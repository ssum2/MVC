package myshop.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberVO;
import myshop.model.ProductDAO;

public class CartAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		1) 데이터 전송방식 검사
		String method = req.getMethod();
		String goBackURL = req.getParameter("goBackURL");
		
		if(!"POST".equals(method)) {	// GET방식으로 들어왔을 때
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {	// POST방식으로 들어왔을 때
//			2) 로그인 유무 검사; 로그인하지 않은 상태에서 장바구니 담기를 시도한 경우에 다시 그 제품페이지로 돌아가야함 --> goBackURL
			MemberVO loginuser = super.getLoginUser(req);
			if(loginuser == null) {
//				#돌아갈 페이지를 파라미터로 받아와서 세션에 저장
				HttpSession session = req.getSession();
				session.setAttribute("returnPage", goBackURL);
				return;
			}
			else {
/*				로그인 한 경우 장바구니 테이블에 제품 데이터를 insert
				- 로그인한 유저의 아이디(fk_userid)를 where 조건절의 조건으로 넘김
				- 동일한 제품이 있는 경우에는 수량만 update
				- 장바구니 비우기 했을 때 delete (status == 0)
				
*/
				String str_pnum = req.getParameter("pnum");
				String str_oqty = req.getParameter("oqty");
				
				int pnum = Integer.parseInt(str_pnum);
				int oqty = Integer.parseInt(str_oqty);
				ProductDAO pdao = new ProductDAO();
				int result = pdao.addCart(loginuser.getUserid(), pnum, oqty);
//				-> result==1; 성공 / result==0; 실패
				if(result == 1) {
					req.setAttribute("msg", "장바구니 담기 성공!");
					req.setAttribute("loc", "cartList.do");
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
				}
				else {
					req.setAttribute("msg", "장바구니 담기 실패!");
					req.setAttribute("loc", goBackURL);
					
					super.setRedirect(false);
					super.setViewPage("/WEB-INF/msg.jsp");
				}
				
			}
			
			
			
			super.getCategoryList(req);
		}
	}

}
