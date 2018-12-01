package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import member.model.MemberVO;
import memo.model.MemoDAO;

public class MemoDeleteAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String[] delCheckArr = req.getParameterValues("delCheck");
//		String userid = ((MemberVO)req.getSession().getAttribute("loginuser")).getUserid();
		MemberVO loginuser = super.getLoginUser(req);
		String userid = loginuser.getUserid();
		
		MemoDAO memodao = new MemoDAO();
		memodao.memoDelete(delCheckArr);
		
		super.setRedirect(true);
		
		if("admin".equals(userid))
			super.setViewPage("memoList.do");
		
		else 
			super.setViewPage("myMemoList.do");
		
		
	}

}
