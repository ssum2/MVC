<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 6ArrayTest2View: 우리반 사람들 명단 출력; jstl 사용</title>
</head>
<body>
	<h2>우리반 친구들</h2>
	<br/>
	<ol style="list-style-type: decimal;">
		<%-- items="배열 또는 리스트" => size, length만큼 반복됨
		 setAttribute의 key값을 EL태그 안에 넣어줌 ${key} --%>
		<c:forEach var="val" items="${friend}"> 
			<li>${val}</li>
		</c:forEach>
	</ol>
</body>
</html>