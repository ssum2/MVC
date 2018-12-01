<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
	String ctxPath = request.getContextPath();
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title> [ ID 중복 검사 ] </title>

<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/style.css"/>
<script type="text/javascript" src="<%= ctxPath %>/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript">

	$(document).ready(function(){
		$("#error").hide();
		
		$("#userid").keydown(function(event){
			if(event.keyCode==13){ // keyCode 13 ; enter
				goCheck();
			}
		});
		
		
	}); // end of ready
	
//	#아이디 중복검사
	function goCheck(){
		var userid = $("#userid").val().trim();
		if(userid == ""){
			$("#error").show();
			$("#userid").val("");
			$("#userid").focus();
			return;	
		}
		else {
			$("#error").hide();
			
			var frm = document.frmIdcheck;
			frm.method = "POST";
			frm.action = "idDuplicateCheck.do";
			frm.submit();
		}
	} // end of function goCheck();

//	#팝업창 -> 부모창(메인폼)으로 값을 전달(2); jquery를 사용한 방법
	function setUserID(userid){
		$(opener.document).find("#userid").val(userid);
		$(opener.document).find("#pwd").focus();
		self.close();
	}
</script>


</head>
<body style="background-color: #fff0f5;">
	<span style="font-size: 10pt; font-weight: bold;">${requestScope.method}</span>
	 <c:if test='${requestScope.method == "GET" }'>
	 <form name="frmIdcheck" style="text-align: center;">
			<table style="width: 95%; height: 100px;">
				<tr>
					<td>
						아이디를 입력하세요<br style="line-height: 200%"/>
						<input type="text" id="userid" name="userid" class="box" size="20" /><br style="line-height: 300%"/>
						<span id="error" style="color: red; font-size: 12pt; font-weight: bold;"> 아이디를 입력하세요! </span><br/>
						<button type="button" class="box" onClick="goCheck();">확인</button>
					</td>
				</tr>
			</table>
		</form>
	</c:if>
	
	<c:if test='${requestScope.method == "POST" && isUseUserid == true}'>
		<div align="center">
			<br style="line-height: 300%;"/>
			입력하신 [ <span style="color: blue; font-weight: bold;">${userid}</span> ]은 ID로 사용 가능합니다.
			<br/><br/><br/>
			<button type="button" class="box" onClick="setUserID('${userid}');">ID사용하기</button>
		</div>
	</c:if>			
	<c:if test='${requestScope.method == "POST" && isUseUserid == false}'>
		<div align="center">
			입력하신 [ <span style="color: red; font-weight: bold;">${userid}</span> ]은 이미 존재합니다.
			<br/>
			다른 아이디를 입력하세요.
			<br/><br/>
			<hr style="border: 1px solid gray;">
			<form name="frmIdcheck" style="text-align: center;">
				<table style="width: 95%; height: 100px; align: center;">
					<tr>
						<td>
							아이디를 입력하세요<br style="line-height: 200%"/>
							<input type="text" id="userid" name="userid" class="box" size="20" /><br style="line-height: 300%"/>
							<span id="error" style="color: red; font-size: 12pt; font-weight: bold;"> 아이디를 입력하세요! </span><br/>
							<button type="button" class="box" name="submit" onClick="goCheck();">확인</button>
						</td>
					</tr>
				</table>
			</form>
			<%-- <hr style="border: 1px solid gray;">
			<button type="button" onClick="setUserID('${userid}');">ID사용하기</button> --%>
		</div>
	 </c:if>
</body>
</html>