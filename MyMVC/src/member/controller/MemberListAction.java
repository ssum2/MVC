package member.controller;

import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;
import my.util.MyUtil;

public class MemberListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser != null) {
//		1. MemberDAO객체 생성
			MemberDAO memberdao = new MemberDAO();
	
	//		2. 검색어 및 날짜구간을 받아서 검색
	//		1) 페이징처리; 페이지 당 보여줄 sizePerPage, 검색타입, 검색어, 기간 받아오기
			String searchType = req.getParameter("searchType");
			String searchWord = req.getParameter("searchWord");
			String str_period = req.getParameter("period");
			String str_sizePerPage = req.getParameter("sizePerPage");
			
			int period = 0;
			int sizePerPage = 0;
			
	//		2) 초기화면 설정값 정하기 (default)
			if(searchType == null) {
				searchType = "name";
			}
			if(searchWord == null) {
				searchWord = "";
			}
			if(str_period == null){
				period = -1; // 입력값이 없을 때 default -1
			}
			if(str_sizePerPage == null){	// sizePerPage의 값을 설정하지 않았을 때(default)
				sizePerPage = 5;
			}
			
	//		#GET방식으로 넘어오는 경우이므로 사용자가 임의로 url데이터쿼리를 바꾸지 못하게 함
			if(!"name".equals(searchType) && !"userid".equals(searchType) && !"email".equals(searchType)) {
				searchType = "name";
			}
			
			try{
				period = Integer.parseInt(str_period);
				if( period != 1 && period != 3 && period != 10 && period != 30 && period != 60){
					period = -1;
				}
			} catch(NumberFormatException e){ // 숫자 외 다른 값을 입력했을 때
				period = -1;
			}
	
			try{
				sizePerPage = Integer.parseInt(str_sizePerPage);
				
				if(sizePerPage != 3 && sizePerPage != 5 && sizePerPage !=10){
					// 지정된 숫자외 임의의 숫자를 입력했을 때 default값으로 초기화
					sizePerPage = 5;
				}
			} catch(NumberFormatException e) {
				// 숫자 외 다른 문자가 들어왔을 때 default값으로 초기화
				sizePerPage = 5;
			}
			
	//		3. 전체 페이지 갯수 알아오기
			int totalMemberCount = 0;
			if("admin".equalsIgnoreCase(loginuser.getUserid())) {
				totalMemberCount = memberdao.getTotalCount(searchType, searchWord, period);
			}
			else {
				totalMemberCount = memberdao.getTotalCountMember(searchType, searchWord, period);
			}
			
	//		4. totalPage 구하기; ceil(전체회원수/페이지 당 회원명수)
			int totalPage = (int)Math.ceil((double)totalMemberCount/sizePerPage);
			
	
	//		5. 사용자가 선택한 페이지 번호 가져오기; 임의의 값 입력 방지
			String str_currentShowPageNo = req.getParameter("currentShowPageNo");
			int currentShowPageNo = 0;
	
			if(str_currentShowPageNo == null){
				currentShowPageNo = 1;
			}
			else {
				try{
					currentShowPageNo = Integer.parseInt(str_currentShowPageNo);
					
					if(currentShowPageNo<1 || currentShowPageNo > totalPage){ 
						// 지정된 숫자외 임의의 숫자를 입력했을 때 default값으로 초기화
						currentShowPageNo = 1;
					}
				} catch(NumberFormatException e) {
					// 숫자 외 다른 문자가 들어왔을 때 default값으로 초기화
					currentShowPageNo = 1;
				}
			}
	
	//		6. 검색 및 날짜구간 기능까지 포함하여 memberList 셋팅
			List<MemberVO> memberList = null;
			memberList = memberdao.getAllMember(sizePerPage, currentShowPageNo, searchType, searchWord, period);
		
			
//			7. 페이징바 처리
			int blockSize = 10;
			String url = "memberList.do";
			String pageBar = MyUtil.getSearchPageBar(url, currentShowPageNo, sizePerPage, totalPage, blockSize, searchType, searchWord, period);

			
			String currentURL = MyUtil.getCurrentURL(req);
			System.out.println("currentURL: "+currentURL);
			
	//		8. request attribute에 연산 결과물 셋팅
			req.setAttribute("period", period);
			req.setAttribute("searchType", searchType);
			req.setAttribute("searchWord", searchWord);
	
			req.setAttribute("sizePerPage", sizePerPage);
			req.setAttribute("currentShowPageNo", currentShowPageNo);
			req.setAttribute("totalMemberCount", totalMemberCount);
			req.setAttribute("totalPage", totalPage);
			req.setAttribute("memberList", memberList);
			
			req.setAttribute("pageBar", pageBar);
			
			req.setAttribute("currentURL", currentURL);
			
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/member/memberList.jsp");
		}
		else {
			return;
		}
	}

}
