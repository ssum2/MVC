package ajax.controller.chap4;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class FirstPersonAction extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {

		String str_personJSON = req.getParameter("str_personJSON");
		
		super.setRedirect(false);
		super.setViewPage("/ajaxstudy/chap4/1personAjax.jsp");
		
	}

}
