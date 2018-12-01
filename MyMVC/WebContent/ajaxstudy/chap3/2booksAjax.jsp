<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<% String ctxPath = request.getContextPath(); %>  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>2booksAjax; </title>

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script> 
<style type="text/css">
	h3 {color: blue;}
	ul {list-style: square;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("#btn").click(function(){
			$.ajax({
				url: "2booksXML.do",
				type: "GET",
				dataType: "xml",
				success: function(xml){
					$("#fictioninfo").empty();
					$("#programinginfo").empty();

					var rootElement = $(xml).find(":root");
					var bookArr = $(rootElement).find("book");

					bookArr.each(function(){
						var html = "<li>도서명: "+$(this).find("booktitle").text()+"\/ 작가명: "+$(this).find("author").text()+"\/ 등록일: "+$(this).find("registerday").text()+"</li>";
						var subject = $(this).find("subject").text();
						if(subject == "소설"){
							$("#fictioninfo").append(html);
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
		<ul id="fictioninfo">
		</ul>
	</div>
	<div id="it">
		<h3>프로그래밍</h3>
		<ul id="programinginfo"></ul>
	</div>

</body>
</html>