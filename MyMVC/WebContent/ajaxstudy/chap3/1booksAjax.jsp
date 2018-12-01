<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>1booksAjax; </title>

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script> 
<style type="text/css">
	h3 {color: blue;}
	ul {list-style: square;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("#btn").click(function(){
			$.ajax({
				url: "1booksXML.do",
				dataType: "xml",
				success: function(xml){
					$("#fictioninfo").empty();
					$("#programinginfo").empty();
					
					/*
					#XML타입의 데이터를 넣는 법
					1) xml형태의 데이터 내에서 최상위 엘리먼트 가져오기
						$(데이터변수).find(":root"); 
					2) 최상위 엘리먼트의 태그명 가져오기
						$($(데이터변수).find(":root")).prop("tagName"));
					3) 배열 형태로 특정 엘리먼트들을 찾아서 받기
						$(최상위 엘리먼트).find("특정엘리먼트태그명");
					4) 반복문으로 배열속의 엘리먼트 값을 가져오기
						Arr.each(function(){
							$(this).find("찾을 엘리먼트태그명").text(); 
						});
					*/
					var rootElement = $(xml).find(":root");
				//	alert("최상위 엘리먼트의 태그명: "+$(rootElement).prop("tagName"));
				// >> 최상위 엘리먼트의 태그명: books
					
					var bookArr = $(rootElement).find("book");
				/*	>> 아래 형태의 book 엘리먼트태그로 묶여있는 것들을 배열 형태로 다 가져옴
					<book>
						<subject>소설</subject>
						<title>마션</title>
						<author>앤디위어</author>
					</book>
				*/
				//	반복문으로 배열 속에 담겨있는 행들을 꺼내오기
					bookArr.each(function(){
						var html = "<li>도서명: "+$(this).find("title").text()+", 작가명: "+$(this).find("author").text()+"</li>";
						var subject = $(this).find("subject").text();
						if(subject == "소설"){
							
							$("#fictioninfo").append(html);
							// 반복문이기 때문에 html()이나 text()를 쓰면 새로운 데이터로 덮어씌우기 때문에 append를 사용
						}
						else if(subject == "프로그래밍"){
							$("#programinginfo").append(html);
						}
					}); 
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
				
			});
		});
	});
</script>
</head>
<body>

	<div align="center">
		<button type="button" id="btn">도서출력</button>
	</div>
	<div id="fiction">
		<h3>소설</h3>
		<ul id="fictioninfo"></ul>
	</div>
	<div id="it">
		<h3>프로그래밍</h3>
		<ul id="programinginfo"></ul>
	</div>

</body>
</html>