<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>

<%
//	#Array를 만들어서 6ArrayTest2View.jsp으로 forward	--> jstl로 출력
	String[] nameArr = {"배수미", "이지민", "최수욱", "김우철", "홍석화"};
	request.setAttribute("friend", nameArr);
	
	RequestDispatcher dispatcher = request.getRequestDispatcher("6ArrayTest2View.jsp");
	dispatcher.forward(request, response);
%>