<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%-- 
	#JSTL; 태그를 통해 jsp코드를 관리하는 라이브러리
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
	
	#변수의 선언 방법
	<c:set var="변수명" value="변수의 값"/>	
					--> form에서 보낸 값을 가져올 때는 ${param.name}
					--> 태그라이브러리로 만든 변수를 가져올 때는 ${변수명}
--%>
<c:set var="no1" value="${param.num1}"/>
<c:set var="no2" value="${param.num2}"/>
<c:set var="result" value="${no1*no2}"/>


<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 1MultiplyEnd: 두개의 수를 입력받아 곱셈한 결과 출력 화면</title>
</head>
<body>
	${no1}와 ${no2}의 곱은? ${result} 입니다. <br/><br/>
	${param.num1}와 ${param.num2}의 곱은? ${param.num1 * param.num2} 입니다.
</body>
</html>