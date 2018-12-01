package memo.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;
import memo.model.MemoDAO;

public class MemoOpenBlindAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String[] delCheckArr = req.getParameterValues("delCheck");
		
		MemoDAO memodao = new MemoDAO();
		String status = req.getParameter("status");
		memodao.memoOpenBlind(delCheckArr, status);
		super.setRedirect(true);
		super.setViewPage("myMemoList.do");
	}

}
