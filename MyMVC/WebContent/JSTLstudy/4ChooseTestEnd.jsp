<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 4ChooseTestEnd: 입력받은 숫자가 1,3일 때는 남자, 2,4일 때는 여자로 나타낸 결과물</title>
</head>
<body>
	<c:choose> <%-- swich-case문 또는 oracle의 case when then else end와 흡사한 기능 --%>
		<c:when test="${param.num == '1'}">
			<span style="color: blue;">남자</span>입니다. <br/>
		</c:when>
		<c:when test="${param.num == '3'}">
			<span style="color: blue;">남자</span>입니다.<br/>
		</c:when>
		<c:when test="${param.num == '2'}">
			<span style="color: green;">여자</span>입니다.<br/>
		</c:when>
		<c:when test="${param.num == '4'}">
			<span style="color: green;">여자</span>입니다.<br/>
		</c:when>
		<c:otherwise>
			1,2,3,4 중 하나만 입력하세요. <br/>
		</c:otherwise>
	
	</c:choose>
</body>
</html>