<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
    String ctxPath = request.getContextPath();
	// >> /MyWeb
%>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" type="text/css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>

<style>
	#div_name {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
	}
	
	#div_mobile {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
	}
	
	#div_findResult {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;		
		position: relative;
	}
	
	#div_btnFind {
		width: 70%;
		height: 15%;
		margin-bottom: 5%;
		margin-left: 10%;
		position: relative;
	}
	
</style>

<script type="text/javascript">
	
	$(document).ready(function(){
	// 1) GET 방식으로 들어왔을 때(초기 화면) ID 출력 부분을 감춤
		var method = "${method}"; // 문자열로 받아올 때는 반드시 "~"
		console.log("method: "+method);
		
		if(method=="GET"){
			$("#div_findResult").hide();
		}
		else {
			$("#name").val("${name}");
			$("#mobile").val("${mobile}");
			$("#div_findResult").show();
		}
		
		$("#btnFind").click(function(){
			var name = $("#name").val().trim();
			var mobile = $("#mobile").val().trim();
			
			if(name==""){
				alert("성명을 입력하세요.");
				$("#name").val("");
				$("#name").focus();
				return;
			}
			if(mobile==""){
				alert("연락처를 입력하세요.");
				$("#mobile").val("");
				$("#mobile").focus();
				return;
			}
			
			var regExp1 = /\d{11}/g;
			var regExp2 = /\d{10}/g;
			var isUseMobile1 = regExp1.test(mobile);
			var isUseMobile2 = regExp2.test(mobile);
			if(!isUseMobile1 || !isUseMobile2) {
				alert("10~11자리 숫자만 입력 가능합니다.");
				$("#mobile").val("");
				$("#mobile").focus();
				return;
			}
			
		   var frm = document.idFindFrm
		   frm.action = "<%= ctxPath %>/idFind.do";
		   frm.method = "POST";
		   frm.submit();
	   });
	});
	
</script>

<form name="idFindFrm">
   <div id="div_name" align="center">
      <span style="font-weight: bold; font-size: 12pt; margin-right: 3%; padding-left: 5%;">성명</span>
      <input type="text" name="name" id="name" size="15" placeholder="홍길동" required />
   </div>
   
   <div id="div_mobile" align="center">
   	  <span style="font-weight: bold; font-size: 12pt; margin-right: 3%;">연락처</span>
      <input type="text" name="mobile" id="mobile" size="15" placeholder="-없이 입력하세요" required />
   </div>
   
   <div id="div_findResult" align="center">
	  <span style="font-size: 12pt; font-weight: bold; padding-left: 5%;">[검색 결과]</span><br/>
   	  <span style="color: red; font-size: 14pt; font-weight: bold; padding-left: 5%;">${userid}</span> 
   </div>
   
   <div id="div_btnFind" align="center" style="padding-left: 5%;">
   		<button type="button" class="btn btn-success" id="btnFind">찾기</button>
   </div>
   
</form>