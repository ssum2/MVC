package ajax.controller.chap4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class SecondPersonArrAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		String str_jsonArr = req.getParameter("str_jsonArr");
		
		req.setAttribute("str_jsonArr", str_jsonArr);
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/2personArrAjax.jsp");

	}

}
