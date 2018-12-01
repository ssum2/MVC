<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
	String ctxPath = request.getContextPath();
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>간단한 jQuery Ajax 예제1; 서버의 응답을 텍스트로 받아서 text로 출력</title>
<script type="text/javascript" src="<%= ctxPath %>/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$("#btn1").click(function(){
			/*
			$.ajax({
				url: "*.do", 				// 보낼 페이지
				type: "GET" or "POST",  	// method가 아니라 type
				dataType: "xml", 			// 여기서 보내는 데이터의 포맷타입(json/xml/html/script/text)
				success: function(결과물 변수){	// 처리할 이벤트 핸들러
						>> 결과물 변수명은 보통 data, text
					성공했을 때의 이벤트 내용
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
					실패했을 때의 이벤트 내용
				}
			});
			*/
			
			$.ajax({
				url: "simple1.txt",
				type: "GET",
				dataType: "text",
				success: function(data){
					$("#result").empty();	// 해당 선택자의 요소 내용을 지움
					$("#result").text(data);	// 넘겨받은 결과물로 채움
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
				
			});	
		});
		
		$("#btn2").click(function(){
			$.ajax({
				url: "simple2.jsp",
				type: "GET",
				dataType: "html",
				success: function(data){
					$("#result").empty();	
					$("#result").html(data);
				},
				error: function(){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
		});
	});

</script>

</head>
<body>
	<button type="button" id="btn1">simple1.txt</button> &nbsp;&nbsp;
	<button type="button" id="btn2">simple2.jsp</button>
	
	<p>
	여기는 simpleAjax.jsp 페이지 입니다.
	<p>
	<div id="result"></div>
	
</body>
</html>