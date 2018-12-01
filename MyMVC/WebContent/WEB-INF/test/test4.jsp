<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>View; test4.do의 내용</title>
</head>
<body>
<%-- view에선 디자인만 적용하고, controller에서 정해둔 key값만 변형을 주면 됨 --%>
	<h2>test4.do의 내용</h2>
	<p>
	${result} <br/> <%-- ${key값} --%>
	>> <span style="color: blue; font-size: 16pt;"> ${name} </span>
</body>
</html>