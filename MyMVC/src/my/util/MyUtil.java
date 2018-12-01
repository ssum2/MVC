package my.util;

/*import java.util.HashMap;
import javax.servlet.http.HttpSession;
import member.model.*;*/

import javax.servlet.http.HttpServletRequest;

public class MyUtil {
//	#현재 URL주소(쿼리데이터까지 포함)를 알려주는 메소드
	public static String getCurrentURL(HttpServletRequest request) {
		String currentURL = request.getRequestURL().toString(); // ?이전의 주소
		String queryString = request.getQueryString(); // ? 이후의 데이터주소
		
		currentURL += "?"+queryString;
		
		String ctxPath = request.getContextPath(); // 해당 jsp파일이 있는 상위 경로 (/MyWeb)
		int beginIndex = currentURL.indexOf(ctxPath) + ctxPath.length();
		// currentURL에서 /MyWeb이 시작하는 부분 -> 21 + /MyWeb의 길이 -> 6 ==> 27 -> /MyWeb 이후 시작값
		
		currentURL = currentURL.substring(beginIndex+1); // member/memberList.jsp~~
		
		
		return currentURL;
	} // end of getCurrentURL(HttpServletRequest request);
	
	
/*//	#현재 세션에 객체가 있는지 없는지 알려주는 메소드 (로그인 유무 검사) --> return HashMap
	public static HashMap<String, Object> checkLoginUser(HttpServletRequest req) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		
		HttpSession session = req.getSession();
		MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
		
		if(loginuser == null) {
			String msg = "로그인 후 사용 가능합니다.";
        	String loc = "javascript:history.back();";
        	
        	map.put("msg", msg);
        	map.put("loc", loc);
        	map.put("loginuser", null);
		}
		else {
			map.put("loginuser", loginuser);
		}
		return map;
	} // end of HashMap<String, Object> checkLoginUser(HttpServletRequest req)
*/

//	#검색어, 날짜구간(period)가 포함된 페이지바 만들기
	public static String getSearchPageBar(String url, int currentShowPageNo, int sizePerPage, int totalPage,
			int blockSize, String searchType, String searchWord, int period) {
		String pageBar ="";
		
		int pageNo = 1;
		int loop = 1;
		
		// ※페이지블럭 공식
		pageNo = ( (currentShowPageNo - 1)/blockSize)*blockSize +1;
		
		if(pageNo!=1) {
			pageBar += "<a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord
						+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+(pageNo-1)+"&period="+period+"'>[이전]</a>&nbsp";
		}				
		while(!(pageNo > totalPage || loop > blockSize)) {
			if(pageNo==currentShowPageNo) {
				pageBar += "&nbsp;<span style='color: #B43846; font-size: 13pt; font-weight: bold; text-decoration: underline;'>"+pageNo+"</span>";
			}
			else if(pageNo!=currentShowPageNo) {
				pageBar += "&nbsp;<a href='"+url+"?sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo
							+"&searchType="+searchType+"&searchWord="+searchWord+"&period="+period+"'>"+pageNo+"</a>";
			}
			pageNo++;
			loop++;
		}
		if(pageNo <= totalPage) {
			pageBar += "<a href='"+url+"?searchType="+searchType+"&searchWord="+searchWord
						+"&sizePerPage="+sizePerPage+"&currentShowPageNo="+pageNo+"&period="+period+"'>[다음]</a>&nbsp";
		}
		
		return pageBar;
	}



}
