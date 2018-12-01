<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="no1" value="${param.num1}"/>
<c:set var="no2" value="${param.num2}"/>
<c:set var="result" value="${no1*no2}"/>



<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 3IfTest: 두 개의 수를 입력 받아서 크기 비교한 결과 출력 화면</title>
</head>
<body>
	더 큰 수?
	<c:if test="${param.num1 > param.num2}">
	<%-- <c:if test="if 조건절"> test조건절이 참일 때 결과물
		 else if가 없기 때문에 각 조건을 다 나열해서 작성해야함 --%>
		${param.num1}
	</c:if>
	
	<c:if test="${param.num1 < param.num2}">
		${param.num2}
	</c:if>
	
	<c:if test="${param.num1 == param.num2}">
		${param.num1}와 ${param.num2}은 같습니다.
	</c:if>
</body>
</html>