<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%

	String str_num1 = request.getParameter("num1");
	String str_num2 = request.getParameter("num2");

	try{
		int num1 = Integer.parseInt(str_num1);
		int num2 = Integer.parseInt(str_num2);
		
		request.setAttribute("num1", num1);
		request.setAttribute("num2", num2);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("quiz_IfTestView.jsp");
		dispatcher.forward(request, response);
		
	} catch(NumberFormatException e){
		request.setAttribute("msg", "숫자만 입력하세요");
		request.setAttribute("loc", "javascript:hisotry.back();");
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("quiz_IfTestError.jsp");
		dispatcher.forward(request, response);
	}
%>
