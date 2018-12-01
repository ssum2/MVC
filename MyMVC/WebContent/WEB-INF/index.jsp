<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%--두 jsp파일이 블록처럼 끼워맞춰짐; heade footer를 따로 쓰고 content만 수정할 수 있도록 함 --%>
<jsp:include page="header.jsp"/>
<%-- 내용물 시작 --%>
	<div class="row">
		<div class="col-md-12" style="border: 0px solid gray; height: 150px; padding: 20px;">
			<h1>${result}</h1>
		</div>
	</div>
	<div class="row">
		<div class="col-md-12" style="border: 0px solid gray; height: 150px; padding: 20px;">
			Content2 입니다.
		</div>
	</div>
	<div class="row">
		<div class="col-md-12" style="border: 0px solid gray; height: 150px; padding: 20px;">
			Content3 입니다.
		</div>
	</div>

<%-- 내용물 끝 --%>
<jsp:include page="footer.jsp"/>