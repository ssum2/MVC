package common.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import common.controller.AbstractController;

public class indexController extends AbstractController {

	@Override
	public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
		req.setAttribute("result", "MVC패턴에 대해 공부를 잘해서 꼭 취업을 합시다! 제발요...");
		
		super.setRedirect(false);
		super.setViewPage("/WEB-INF/index.jsp");
	}
}
