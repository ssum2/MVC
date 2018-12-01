package memo.controller;

import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import common.controller.AbstractController;
import member.model.MemberDAO;
import member.model.MemberVO;
import memo.model.MemoDAO;
import memo.model.MemoVO;
import my.util.MyUtil;

public class MemoListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
//		1. MemoDAO객체 생성
		MemoDAO memodao = new MemoDAO();
		MemberVO loginuser = super.getLoginUser(req);
		
		if(loginuser != null) {
/*	//		#페이징 처리전 데이터 조회 결과물 가져오기
			List<HashMap<String, String>> memoList = memodao.getAllMemo();
			
			req.setAttribute("memoList", memoList);
			*/
			
//		2. 검색어 및 날짜구간을 받아서 검색
//		1) 페이징처리; 페이지 당 보여줄 sizePerPage, 검색타입, 검색어, 기간 받아오기
			String searchType = "";
			String searchWord = "";
			String str_period = "";
			String str_sizePerPage = req.getParameter("sizePerPage");
			
			int period = 0;
			int sizePerPage = 0;
			
	//		2) 초기화면 설정값 정하기 (default)
/*			if(searchType == null) {
				searchType = "";
			}
			if(searchWord == null) {
				searchWord = "";
			}
			if(str_period == null){
				period = 0; // 입력값이 없을 때 default -1
			}*/
			if(str_sizePerPage == null){	// sizePerPage의 값을 설정하지 않았을 때(default)
				sizePerPage = 10;
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
					sizePerPage = 10;
				}
			} catch(NumberFormatException e) {
				// 숫자 외 다른 문자가 들어왔을 때 default값으로 초기화
				sizePerPage = 10;
			}
			
	//		3. 전체 페이지 갯수 알아오기
			int totalMemoCount = 0;
			totalMemoCount = memodao.getTotalCount(searchType, searchWord, period);
	
	//		4. totalPage 구하기; ceil(전체회원수/페이지 당 회원명수)
			int totalPage = (int)Math.ceil((double)totalMemoCount/sizePerPage);
			
	
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
	
//			6. 검색 및 날짜구간 기능까지 포함하여 memberList 셋팅
			List<HashMap<String, String>> memoList = null;
			memoList = memodao.getAllMemo(sizePerPage, currentShowPageNo, searchType, searchWord, period);
			
//			7. 페이징바 처리
			int blockSize = 3;
			String url = "memoList.do";
			String pageBar = MyUtil.getSearchPageBar(url, currentShowPageNo, sizePerPage, totalPage, blockSize, searchType, searchWord, period);
			
	//		8. request attribute에 연산 결과물 셋팅
			req.setAttribute("period", period);
			req.setAttribute("searchType", searchType);
			req.setAttribute("searchWord", searchWord);
	
			req.setAttribute("sizePerPage", sizePerPage);
			req.setAttribute("currentShowPageNo", currentShowPageNo);
			req.setAttribute("totalMemoCount", totalMemoCount);
			req.setAttribute("totalPage", totalPage);
			req.setAttribute("memoList", memoList);
			
			req.setAttribute("pageBar", pageBar);
			
	//		req.setAttribute("currentURL", currentURL);
						
			super.setRedirect(false);
			super.setViewPage("/WEB-INF/memo/memoListJSTL.jsp");
	//		super.setViewPage("/WEB-INF/memo/memoList.jsp");
			return;
		}
		else {
			return;
		}
	}

}
