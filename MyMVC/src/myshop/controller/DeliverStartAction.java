package myshop.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.controller.GoogleMail;
import member.model.MemberVO;
import myshop.model.InterProductDAO;
import myshop.model.ProductDAO;
import myshop.model.ProductVO;

public class DeliverStartAction extends AbstractController {

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
		String[] pnumArr = req.getParameterValues("deliverStartPnum");
		String[] odrcodeArr = req.getParameterValues("odrcode");
		String[] statusArr = req.getParameterValues("deliverStatus");
		
		
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
		int result = pdao.updateDeliverStart(odrcodePnum, odrcodeArr.length);

		if(result==1) {
		/*	List<HashMap<String, String>> deliverOrderList = pdao.getDeliverInfo(odrcodePnum);
			int flag = 1;
			for(int i=0; i<deliverOrderList.size(); i++) {
				String userid = deliverOrderList.get(i).get("userid");
				if(userid == deliverOrderList.get(i+1).get("userid")) {
					flag += 1;
				}
				else {
					flag = 1;
				}
				sb.setLength(0); // StringBuilder reset
				sb.append("<table> <tr style='border: 1px solid gray; border-colapse: colapse;'><td colspan='3' align='center' style='border: 1px solid gray;'> <span style='color: #FF8F00; font-weight: bold;'> 주문코드번호: "+deliverOrderList.get(i).get("odrcode")+"</span></td></tr>");
				sb.append("<tr style='background-color: #cfcfcf; font-weight: bold; border-colapse: colapse;'><td style='text-align: center; border: 1px solid gray;'>제품명</td><td style='text-align: center; border: 1px solid gray;'>가격</td><td style='text-align: center; border: 1px solid gray;'>구매수량</td></tr>");
				for(int j=0; i<flag; j++) {
					sb.append("<tr style='border: 1px solid gray;'>");
					sb.append("<td style='text-align: center; border: 1px solid gray;'><img src='http://192.168.50.48:9090/MyMVC/images/"+deliverOrderList.get(i).get("pimage1")+"' style='width: 130px; height: 100px;' />");
					sb.append("&nbsp; <img src='http://192.168.50.48:9090/MyMVC/images/"+deliverOrderList.get(i).get("pimage2")+"' style='width: 130px; height: 100px;' />");
					sb.append("<br/>"+deliverOrderList.get(i).get("pname")+"</td>");
					sb.append("<td style='text-align: center; border: 1px solid gray;'><span style='color: #FF8F00; font-weight: bold;'>"+deliverOrderList.get(i).get("saleprice")+"</span>원");
					sb.append("<br/><span style='color: #FF8F00; font-weight: bold;'>"+deliverOrderList.get(i).get("point")+"</span>point</td>");
					sb.append("<td style='text-align: center; border: 1px solid gray;'><span style='color: #FF8F00; font-weight: bold;'>"+deliverOrderList.get(i).get("oqty")+"</span>개</td></tr>");
				}
				sb.append("<tr style='border: 1px solid gray; border-colapse: colapse;'><td colspan='3'align='center' style='border: 1px solid gray;'> 이용해주셔서 감사합니다. </td></tr></table>");
				String emailContents = sb.toString();
				String recipient = odrUser.getEmail();
				String name = odrUser.getName();
				
				mail.sendmail_DeliverStart(recipient, name, emailContents);
			}*/
			
		    // === *** 배송을 했다라는 email을 주문을 한 사람(여러명)에게 보내기 시작 *** === //  
			
				// 1. 주문코드에 대한 제품번호 붙여가기(동일한 주문코드에 서로다른 제품번호가 있으므로)
				//    KEY는 주문코드, VALUE 는 여러제품번호들로 한다.  
				HashMap<String, String> odrcodeMap = new HashMap<String, String>(); 
				Set<String> odrcodeMapKeysets = odrcodeMap.keySet();
				
				for(int i=0; i<odrcodeArr.length; i++) {
					if(i==0) {
						odrcodeMap.put(odrcodeArr[i], pnumArr[i]); 
					}
					else {
						boolean flag = false;
						for(String key : odrcodeMapKeysets) {
							if(odrcodeArr[i].equals(key)) {
								odrcodeMap.put(odrcodeArr[i], odrcodeMap.get(key)+","+pnumArr[i]); 
								flag = true;
								break;
							}
						}// end of for--------------
						
						if(flag == false) {
							odrcodeMap.put(odrcodeArr[i], pnumArr[i]); 
						}
					}// end of if~else--------------
				}// end of for----------------------
				
				// **** 콘솔 확인용 **** //
				for(String key : odrcodeMapKeysets) {
					System.out.println("주문코드 : " + key + ", 제품번호 : " + odrcodeMap.get(key)); 
				}
				/*
				 	주문코드 : s20181122-5, 제품번호 : 3
					주문코드 : s20181122-4, 제품번호 : 3,4
					주문코드 : s20181122-3, 제품번호 : 1 
				 */
				
			
				// 2. 주문코드에 대한 제품번호들을 읽어와서   
				//    그 제품번호들에 대한 제품의 정보를 메일내용속에 넣고  
				//    주문코드를 사용하여 사용자의 정보를 읽어와서 
				//    그 사용자정보에서 email 주소와 사용자명을 찾아온다.
				for(String key : odrcodeMapKeysets) { 
					// key 가 주문코드 이다.
					
					String pnumes = odrcodeMap.get(key);
					// odrcodeMap 에서 주문코드에 대한 제품번호들을 읽어온다. 
					
					// **** 콘솔 확인용 **** //
					System.out.println("~~~ 확인용 pnumes : " + pnumes); 
					/*
					     ~~~ 확인용 pnumes : 3
					     ~~~ 확인용 pnumes : 3,4
					     ~~~ 확인용 pnumes : 1
					 */
					
					List<ProductVO> productList = pdao.getOrderProductList(pnumes);
					
					sb.setLength(0); // StringBuilder sb 초기화하기
					
					for(int j=0; j<productList.size(); j++) {
						if(j==0) {
							sb.append("주문코드번호 : <span style='color: blue; font-weight: bold;'>"+key+"</span><br/><br/>");	
							sb.append("<주문상품><br/>");
							sb.append(productList.get(j).getPname()+"&nbsp;");
							sb.append("<img src='http://192.168.50.53:9090/MyMVC/images/"+productList.get(j).getPimage1()+"'/>");  
							sb.append("<br/>");
						}
						else {
							sb.append(productList.get(j).getPname()+"&nbsp;");
							sb.append("<img src='http://192.168.50.53:9090/MyMVC/images/"+productList.get(j).getPimage1()+"'/>");  
							sb.append("<br/>");
						}
					}// end of for--------------------------
					
					sb.append("<br/><br/>우체국택배로 배송했습니다.");
					
					String emailContents = sb.toString();
					
					MemberVO membervo = pdao.getOneUserByOdrcode(key);
					// 해당 전표(영수증번호)의 소유주(회원) 찾아오기
					
					String emailAddress = membervo.getEmail();
					// 해당 전표(영수증번호)의 소유주(회원)의 이메일주소 가져오기 
					
					String name = membervo.getName();
					// 해당 전표(영수증번호)의 소유주(회원)의 성명 가져오기 
					
				// 3. 메일을 발송한다.	
					GoogleMail mail = new GoogleMail();	
					
					mail.sendmail_DeliverStart(emailAddress, name, emailContents); 
					
				}// end of for---------------------
			// === *** 배송을 했다라는 email을 주문을 한 사람(여러명)에게 보내기 종료 *** === //
			
			

			req.setAttribute("msg", "선택하신 주문에 대하여 배송상태가 '배송시작'으로 변경되었습니다.");
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
