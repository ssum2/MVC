<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
    String ctxPath = request.getContextPath();
	// >> /MyWeb
%>
<!DOCTYPE html>
<html>
<head>

<title>:::HOMEPAGE:::</title>

<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/css/style.css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>

<link rel="stylesheet" href="http://code.jquery.com/ui/1.12.1/themes/base/jquery-ui.css" type="text/css" />
<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.3.1/jquery.min.js"></script>
<script src="http://code.jquery.com/ui/1.12.1/jquery-ui.min.js"></script>
<link rel="stylesheet" type="text/css" href="<%= ctxPath %>/jquery-ui-1.11.4.custom/jquery-ui.min.css" /> 
<script type="text/javascript" src="<%= ctxPath %>/jquery-ui-1.11.4.custom/jquery-ui.min.js"></script> 

<script type="text/javascript">
	$(document).ready(function(){
		
		var vhtml = "";
		for(var i=0; i<15; i++) {
			vhtml += (i+1)+".내용물<br/>";
		}
		
//		$("#sidecontent").html(vhtml);
		
		$("#sideinfo").css('height', $("#content").height());
		
	});
	
</script>

</head>
<body>

<div id="mycontainer">

	<div id="headerImg">
		<div class="row">
			<div class="col-md-2">1. 로고이미지/네비게이터</div>
			<div class="col-md-2"><a href="http://www.samsung.com"><img src="<%= ctxPath %>/images/logo1.png"/></a></div>
			<div class="col-md-2"><img src="<%= ctxPath %>/images/logo2.png"/></div>
		</div>
	</div>
	
	<div id="headerLink">
		<div class="row">
			<div class="col-md-1">
				<a href="<%= ctxPath %>/index.do">HOME</a>
			</div>
			<div class="col-md-1">
				<a href="<%= ctxPath %>/memberRegister.do">회원가입</a>
			</div>
		<c:if test="${sessionScope.loginuser!=null}">
		<%-- requestScope.key or sessionScope.key ~~ 해당 영역 Attribute에 저장된 객체 사용 
				loginuser가 null이 아닐 때; 로그인 된 상태일 때만 해당 메뉴를 출력 --%>
			<div class="col-md-1">
				<a href="<%= ctxPath %>/memberList.do">회원목록</a>
			</div>	
			<div class="col-md-1">
				<a href="<%= ctxPath %>/memo.do">한줄메모쓰기</a>
			</div>
			<div class="col-md-1">
				<a href="<%= ctxPath %>/memoList.do">메모조회(HashMap)</a>
			</div>
		
			<div class="col-md-1">
				<a href="<%= ctxPath %>/myMemoList.do">나의 메모조회(VO)</a>
			</div>
		</c:if>
			<div class="col-md-1">
				<a href="<%= ctxPath %>/mallHome.do">쇼핑몰 홈</a>
			</div>
		<c:if test="${sessionScope.loginuser!=null}">
			<div class="col-md-1">
				<a href="<%= ctxPath %>/orderList.do">주문내역</a> 
			</div>
		</c:if>
		<c:if test="${sessionScope.loginuser!=null && sessionScope.loginuser.userid ne 'admin'}">
			<div class="col-md-1">
				<a href="<%= ctxPath %>/cartList.do">장바구니</a>
			</div>
		</c:if>
			<div class="col-md-1">
				<a data-toggle="modal" data-target="#searchStore" data-dismiss="modal">매장찾기</a>
				<%-- #searchStore 에 대한 내용은 footer.jsp에 있음 --%>
			</div>	
		<c:if test="${sessionScope.loginuser!=null && sessionScope.loginuser.userid eq 'admin'}">
			<div class="col-md-1">
				<a href="<%= ctxPath %>/admin/productRegister.do">제품등록</a> 
				<%-- 경로에 폴더명까지 명시해준 것은 관리자모드라는 것을 명시한 것, properties에 경로를 폴더명까지 붙여야함 --%>
			</div>
		</c:if>

		</div>
	</div>
	
	<div id="sideinfo" >
		<div class="row">
			<div class="col-md-12" style="height: 50px; text-align: left; padding: 20px;">
				2. 로그인<br/><br/>
				<%@ include file="/WEB-INF/login/login.jsp" %>
				<%-- >> jsp 지시어는 html 태그 내에서도 사용 가능 --%>	
			</div>
		</div>
		<%-- <div class="row">
			<div class="col-md-12" id="sidecontent" style="text-align: left; padding: 20px;">
			</div>
		</div> --%>
		<div class="row" style="margin-top: 65%;">
			<div class="col-md-12" style="height: 50px; text-align: left; padding: 20px;">
				3. 카테고리 메뉴<br/><br/>
				<%@ include file="/WEB-INF/myshop/categoryList.jsp" %>
				<%-- >> jsp 지시어는 html 태그 내에서도 사용 가능 --%>	
			</div>
		</div>
	</div>
	
	<div id="content" align="center">