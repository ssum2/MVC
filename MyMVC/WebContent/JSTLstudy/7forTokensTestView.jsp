<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 7forTokensTestView: 우리반 사람들 명단 출력; jstl 사용</title>
</head>
<body>
	<h2>우리반 친구들</h2>
	<br/>
	<ol style="list-style-type: decimal;">
		<c:forTokens var="name" items="${friend}" delims=",">
		<%-- c:forTokens var="변수" items="문자열" delims="구분자"
			==> delims에 정의된 구분자를 기준으로 items에 들어있는 문자열을 잘라서 배열화함 --%>
			<li>${name}</li>
		</c:forTokens>
	</ol>
</body>
</html>