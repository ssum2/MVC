<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>일정시간마다 함수호출을 해주는 setTimeout() 함수 예제; jQuery를 사용하여 시계 만들기</title>
<script type="text/javascript" src="<%= ctxPath %>/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		loopshowNowTime();
	});
	
	// 현재 시각을 알아올 자바스크립트 함수
	function showNowTime(){
	// #날짜
		var now = new Date();
		var strNow = now.getFullYear()+"년 " + (now.getMonth()+1)+"월 " + now.getDate()+"일 ";
		// getMonth()는 0월부터 시작하기 때문에 반드시 +1 해줘야함
		
	// #시간
		// 시는 기본적으로 1~24로 나오기 때문에 AM PM을 붙일 경우
		var hour = "";
		if(now.getHours() >= 12){
			hour = "PM ";
		}
		else {
			hour = "AM ";
		}
		
		if(now.getHours() < 10){
			hour += "0"+now.getHours();
		}
		else{
			hour += now.getHours();
		}
		
		// 시간의 경우 숫자이기 때문에 10미만 수인 경우 0을 앞에 붙여주는 작업이 필요함
		var minutes = "";
		if(now.getMinutes() < 10){
			minute = "0"+now.getMinutes();
		}
		else{
			minutes = now.getMinutes();
		}
		
		var seconds = "";
		if(now.getSeconds() < 10){
			seconds = "0"+now.getSeconds();
		}
		else{
			seconds = now.getSeconds();
		}
					
		strNow += hour + ":"+ minutes+":"+seconds;
		
		console.log(strNow);
	}
	
	//알아온 현재 시각을 일정시간마다 호출하는 메소드
	function loopshowNowTime(){
		/*
		setTimeout(function(){실행할 내용}, 밀리초단위);
		>> 밀리초단위 뒤에 function 안의 실행 내용을 실행해줌
		
		setTimeout(function(){
			alert("ㅋㅋㅋ");
		}, 3000);
		*/
		
		showNowTime();
		setTimeout(function(){
			loopshowNowTime();
		}, 1000);
	}
	
</script>


</head>
<body>
	<span style="color: gray; font-size: 10pt; font-weight: bold;">현재시각</span>
	<span id="clock" style="color: dark-yellow; font-size: 16pt; font-weight: bold;"></span>
</body>
</html>