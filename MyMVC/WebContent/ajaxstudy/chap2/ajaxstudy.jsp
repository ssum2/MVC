<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<% String ctxPath = request.getContextPath(); %>  
  
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>jQuery Ajax 예제2(서버의 응답을 HTML로 받아서 HTML로 출력하는 예제)</title>

<link rel="stylesheet" type="text/css" href="<%=ctxPath %>/ajaxstudy/chap2/css/style.css" />

<script type="text/javascript" src="<%=ctxPath %>/js/jquery-3.3.1.min.js"></script> 

<script type="text/javascript">
	
	$(document).ready(function(){
		startAjaxCalls();
		
		$("#btn1").click(function(){
			var form_data ={searchname:$("#name").val()};	// key:value
						// {searchname:이순신} == ".do?searchname=이순신"
			/*
				{key1:value1
				,key2:value2
				,key3:value3 ....};
			*/
			$.ajax({
				url: "memberHTML.do",
				type: "GET",
				data: form_data, 	// 파라미터로 넘길 데이터
				dataType: "html",
				success: function(html){
					// data 항목 때문에 아래 success의 function 파라미터에 dataType을 변수명으로 사용하는게 일반적임
					$("#memberInfo").empty().html(html);
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
		});
		
		$("#btn2").click(function(){
			$.ajax({
				url: "imageHTML.do",
				type: "GET",
				dataType: "html",
				success: function(html){
					$("#imgInfo").empty().html(html);
				},
				error: function(request, status, error){
					alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
				}
			});
			
		});
		
		
		$("#btn3").click(function(){
			$("#imgInfo").empty();
			$("#memberInfo").empty();
			$("#name").val('');
		});
		
	});// end of $(document).ready();------------------
	
	function getNewsTitle(){
		$.ajax({
			url: "newsTitleHTML.do",
			type: "GET",
			dataType: "html",
			success: function(data){	// data를 html타입으로 받아온 것
				// console.log(data);
				$("#newsTitle").html(data);
			},
			error: function(request, status, error){
				alert("code: "+request.status+"\n"+"message: "+request.responseText+"\n"+"error: "+error);
			}
			
		});
	}
	
	<%-- newsTitle.do가 들어가는 메소드 --%>
	function startAjaxCalls(){
		getNewsTitle();
		setTimeout(function(){
			startAjaxCalls();
		}, 10000);
	}
</script>

</head>
<body>
    <h2>이곳은 MyMVC/ajaxstudy.do 페이지의 데이터가 보이는 곳입니다.</h2>
    <br/><br/>    
	<div align="center">
		<form name="searchFrm">
			회원명 : <input type="text" name="name" id="name" />
		</form>
		<br/>
		<button type="button" id="btn1">회원명단보기</button> &nbsp;&nbsp;
		<button type="button" id="btn2">사진보기</button> &nbsp;&nbsp;
		<button type="button" id="btn3">지우기</button> 
	</div>
	
	<%-- DB에 있는 신문기사 타이틀을 불러오는 곳 --%>
	<div id="newsTitle" style="margin-top: 20px; margin-bottom: 20px;"></div> 
	
	<%-- DB에 있는 회원정보 및 이미지를 불러오는 곳 --%>
	<div id="container">
		<div id="memberInfo"></div>
		<div id="imgInfo"></div>
	</div>

</body>
</html>




