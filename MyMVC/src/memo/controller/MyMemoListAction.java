package memo.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import memo.model.MemoDAO;
import memo.model.MemoVO;
import my.util.MyUtil;

public class MyMemoListAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		MemoDAO memodao = new MemoDAO();
		MemberVO loginuser = super.getLoginUser(req);
		if(loginuser == null) return;
		
		String searchType = "";
		String searchWord = "";
		String str_period = "";
		String str_sizePerPage = req.getParameter("sizePerPage");
		
		String[] idxArr = req.getParameterValues("idx"); // 체크박스가 여러개이기 때문에 파라미터의 밸류는 배열타입으로 받아옴
		
		int period = 0;
		int sizePerPage = 0;
		
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
		
//		#해당 회원의 전체 메모 갯수 구하기
		int totalMemoCount = 0;
		totalMemoCount = memodao.getTotalCount(loginuser.getUserid(), searchType, searchWord, period);

		int totalPage = (int)Math.ceil((double)totalMemoCount/sizePerPage);
		
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

		List<MemoVO> memoList = null;
		memoList = memodao.getAllMemo(loginuser.getUserid(), sizePerPage, currentShowPageNo, searchType, searchWord, period);

		int blockSize = 3;
		String url = "myMemoList.do";
		String pageBar = MyUtil.getSearchPageBar(url, currentShowPageNo, sizePerPage, totalPage, blockSize, searchType, searchWord, period);

		req.setAttribute("period", period);
		req.setAttribute("searchType", searchType);
		req.setAttribute("searchWord", searchWord);

		req.setAttribute("sizePerPage", sizePerPage);
		req.setAttribute("currentShowPageNo", currentShowPageNo);
		req.setAttribute("totalMemoCount", totalMemoCount);
		req.setAttribute("totalPage", totalPage);
		req.setAttribute("memoList", memoList);
		
		req.setAttribute("pageBar", pageBar);
					
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/memo/myMemoList.jsp");

		return;
	}
}