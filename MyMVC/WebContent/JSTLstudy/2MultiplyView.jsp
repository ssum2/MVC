<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSTL; 2MultiplyView: 두개의 수를 입력받아 곱셈한 결과를 넘겨받아 출력</title>
</head>
<body bgcolor="pink">
	첫번째 입력한 수: ${no1} <br/>
	두번째 입력한 수: ${no2} <br/>
	곱한 결과 값1: ${result} <br/>
	곱한 결과 값2: ${no1*no2} <br/>
	곱한 결과 값3: ${param.num1 * param.num2} <br/>
	
</body>
</html>