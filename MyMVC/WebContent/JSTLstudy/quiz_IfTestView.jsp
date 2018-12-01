<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; quiz_IfTestView: 두 개의 수를 입력 받아서 크기 비교한 결과 출력 화면</title>
</head>
<body>
	더 큰 수?
	<c:if test="${num1 > num2}">
		${param.num1}
	</c:if>
	
	<c:if test="${num1 < num2}">
		${num2}
	</c:if>
	
	<c:if test="${num1 == num2}">
		${num1}와 ${num2}은 같습니다.
	</c:if>
</body>
</html>