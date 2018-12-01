<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%-- JSTL; 2MultiplyCalc: 두개의 수를 입력받아 곱셈연산을 처리
	;dispatcher forward로 2MultiplyView.jsp에 결과물을 넘겨줌 					--%>
	
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="no1" value="${param.num1}" scope="request"/>
<%-- scope의 default값이 page이기 때문에  forward한 페이지에서도 사용하려면 변수사용범위를 request로 설정해줘야 함 --%>
<c:set var="no2" value="${param.num2}" scope="request"/>
<c:set var="result" value="${no1*no2}" scope="request"/>

<jsp:forward page="2MultiplyView.jsp" />