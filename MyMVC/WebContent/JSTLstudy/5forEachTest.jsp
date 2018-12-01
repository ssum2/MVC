<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 5forEachTest: 반복문 연습</title>
</head>
<body>
	<c:forEach var="i" begin="1" end="5"> <%-- 1~5까지 5번 반복 --%>
	<%-- <c:forEach var="변수" begin="시작수" end="종료수"> --%>
		<h${i}> 반복문 연습 </h${i}> 
	</c:forEach>
</body>
</html>