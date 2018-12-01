package myshop.controller;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.controller.GoogleMail;
import member.model.MemberDAO;
import member.model.MemberVO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class OrderAddAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		super.getCategoryList(req);
		System.out.println("OrderAddAction 1/8 success");
		HttpSession session = req.getSession();
		String method = req.getMethod();
		String goBackURL = req.getParameter("goBackURL");
		int result = 0; // addOrder 메소드의 결과물을 담을 변수
	
		
		if(!"POST".equals(method)) {	// GET방식으로 들어왔을 때
			req.setAttribute("msg", "권한이 없습니다.");
			req.setAttribute("loc", "javascript:history.back();");
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/msg.jsp");
			
			return;
		}
		else {	// POST방식으로 들어왔을 때
//			#로그인 유무 검사; 로그인하지 않은 상태에서 주문하기를 시도한 경우에 다시 그 제품페이지로 돌아가야함 --> goBackURL
			System.out.println("OrderAddAction 2/8 success");
			MemberVO loginuser = super.getLoginUser(req);
			if(loginuser == null) {
//				#돌아갈 페이지를 파라미터로 받아와서 세션에 저장
				session.setAttribute("returnPage", goBackURL);
				return;
			}
			else {
//				#로그인 한 경우 주문 개요 테이블(jsp_order), 주문 상세 테이블(jsp_order_detail)에 insert		
//				>> 제품상세페이지, 장바구니페이지 모두 호환되도록 ParameterValues로 받아옴
				System.out.println("OrderAddAction 3/8 success");
				String[] pnumArr = req.getParameterValues("pnum");
				String[] oqtyArr = req.getParameterValues("oqty");
				String[] salepriceArr = req.getParameterValues("saleprice");
				String[] cartnoArr = req.getParameterValues("cartno");

				String str_sumtotalprice = req.getParameter("sumtotalprice");
				String str_sumtotalpoint = req.getParameter("sumtotalpoint");
				int sumtotalprice = Integer.parseInt(str_sumtotalprice);
				int sumtotalpoint = Integer.parseInt(str_sumtotalpoint);

				/*
				for(int i=0; i<pnumArr.length; i++) {
					System.out.println("제품번호: "+pnumArr[i]);
					System.out.println("실판매단가: "+salepriceArr[i]);	
				}
				System.out.println("주문총액: "+sumtotalprice);
				System.out.println("주문총포인트: "+sumtotalpoint);
				*/
				
				if(cartnoArr != null) {
					for(int i=0; i<cartnoArr.length; i++) {
						System.out.println("장바구니 번호: "+ cartnoArr[i]);
					}
				}
				ProductDAO pdao = new ProductDAO();
/*				#DAO에서 Transaction 처리
				1. 주문개요 테이블(jsp_order)에 insert
				2. 주문상세 테이블(jsp_order_detail)에 insert
				3. 구매하는 사용자의 coin컬럼을 가격만큼 마이너스, point컬럼 값은 구매한 포인트만큼 플러스 update
				4. 주문한 제품(jsp_product)의 재고(pqty)는 주문량(oqty)만큼 감소(update)
				5. 주문완료 후 장바구니에 해당 물품 삭제(본래 delete이지만 status==0으로 update)
				6. return result

				addOrder(
					jsp_order insert; odrcode, fk_userid, odrtotalPrice, odrtotalPoint,
					jsp_order_detail insert; pnum(pnumArr), oqty(oqtyArr), odrprice(salepriceArr)
					jsp_cart update; cartno(cartnoArr)
				)
 */
//				#주문코드odrcode 채번하기; 주문코드 형식 : s(회사코드)+날짜+sequence
				String odrcode = getOdrcode();
		
				result = pdao.addOrder(odrcode, loginuser.getUserid(), sumtotalprice, sumtotalpoint, pnumArr, oqtyArr, salepriceArr, cartnoArr);
//				-> result==1; 성공 / result==0; 실패
				System.out.println("OrderAddAction 5/8 success");

				if(result == 1) {
//					7. 주문완료 시 email 보내기
//						1) 세션에 저장된 loginuser 객체를 갱신
						MemberDAO mdao = new MemberDAO();
						loginuser = mdao.getOneMember(loginuser.getIdx());
						session.setAttribute("loginuser", loginuser);
						System.out.println("OrderAddAction 6/8 success");
						
						
//						2) email 보내기
						GoogleMail mail =new GoogleMail();
						
//						a. 주문완료된 제품의 제품번호 가져오기(in ()에 넣는 용도로 가공)
						StringBuilder sb = new StringBuilder();
						for(int i=0; i<pnumArr.length; i++) {
							String comma = (i < pnumArr.length-1)? ",": "";
							sb.append("\'"+pnumArr[i]+"\'"+comma);  
				        }
						String pnumes = sb.toString().trim();
						System.out.println(pnumes);
						
//						b. 제품번호를 파라미터로 넘겨 제품정보 가져오기
						List<ProductVO> orderProductList = pdao.getOrderProductList(pnumes);
						
//						c. 제품번호를 넣어뒀던 Stringbuilder를 초기화하고 메일 내용 삽입
						sb.setLength(0); // StringBuilder reset
						
						sb.append("<table> <tr style='border: 1px solid gray; border-colapse: colapse;'><td colspan='3' align='center' style='border: 1px solid gray;'> <span style='color: #FF8F00; font-weight: bold;'> 주문코드번호: "+odrcode+"</span></td></tr>");
						sb.append("<tr style='background-color: #cfcfcf; font-weight: bold; border-colapse: colapse;'><td style='text-align: center; border: 1px solid gray;'>제품명</td><td style='text-align: center; border: 1px solid gray;'>가격</td><td style='text-align: center; border: 1px solid gray;'>구매수량</td></tr>");
						for(int i=0; i<orderProductList.size(); i++) {
							sb.append("<tr style='border: 1px solid gray;'>");
							sb.append("<td style='text-align: center; border: 1px solid gray;'><img src='http://192.168.50.48:9090/MyMVC/images/"+orderProductList.get(i).getPimage1()+"' style='width: 130px; height: 100px;' />");
							sb.append("&nbsp; <img src='http://192.168.50.48:9090/MyMVC/images/"+orderProductList.get(i).getPimage2()+"' style='width: 130px; height: 100px;' />");
							sb.append("<br/>"+orderProductList.get(i).getPname()+"</td>");
							sb.append("<td style='text-align: center; border: 1px solid gray;'><span style='color: #FF8F00; font-weight: bold;'>"+orderProductList.get(i).getSaleprice()+"</span>원");
							sb.append("<br/><span style='color: #FF8F00; font-weight: bold;'>"+orderProductList.get(i).getPoint()+"</span>point</td>");
							sb.append("<td style='text-align: center; border: 1px solid gray;'><span style='color: #FF8F00; font-weight: bold;'>"+oqtyArr[i]+"</span>개</td></tr>");
						}
						sb.append("<tr style='border: 1px solid gray; border-colapse: colapse;'><td colspan='3'align='center' style='border: 1px solid gray;'> 이용해주셔서 감사합니다. </td></tr></table>");
						String emailContents = sb.toString();
						String recipient = loginuser.getEmail();
						String name = loginuser.getName();
						
						mail.sendmail_OrderFinish(recipient, name, emailContents);
						System.out.println("OrderAddAction 7/8 success");

						
//					8. view로 전송	
						req.setAttribute("msg", "주문 완료");
						req.setAttribute("loc", "orderList.do"); // 주문내역페이지로 이동
						
						super.setRedirect(false);
						super.setViewPage("/WEB-INF/msg.jsp");
						System.out.println("OrderAddAction 8/8 success");
					}
					else {
						req.setAttribute("msg", "주문 실패");
						req.setAttribute("loc", "javascript:history.back();"); // 주문내역페이지로 이동
						
						super.setRedirect(false);
						super.setViewPage("/WEB-INF/msg.jsp");
						
						return;
					}
			} // end of inner if~else

		} // end of outer if~else
	} // end of execute()
	
	
//	#주문코드 생성 메소드
	private String getOdrcode() throws SQLException {
		String odrcode =""; // s(회사코드)+날짜+sequence
//		#현재 날짜 구하기
		Date now = new Date();
		SimpleDateFormat smdatefm = new SimpleDateFormat("yyyyMMdd");
		String today = smdatefm.format(now);
		
		ProductDAO pdao = new ProductDAO();
		int seq = pdao.getSeq_jsp_order(); 	// 주문코드시퀀스를 채번하는 메소드
		odrcode = "s"+today+"-"+seq;
		System.out.println("OrderAddAction 4/8 success");
		
		return odrcode;
	}
	
} // end of class
