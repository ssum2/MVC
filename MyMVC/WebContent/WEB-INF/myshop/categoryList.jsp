<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:if test="${requestScope.categoryList != null}"> 
<%-- 카테고리목록 메소드로 목록을 가져온 경우에만 출력 --%>
<div style="width: 95%; border: 1px solid gray;  text-align: center; margin-left: 2.5%;">
	<span style="color: navy; font-size: 14pt; font-weight: bold; text-align: center;">
		Category List
	</span><br/>
	<div style="border: 0px solid gray; border-top: hidden; text-align: left; margin: 10%;">
	<ul style="list-style-type: square;">
		<li><a href="<%=request.getContextPath()%>/mallHome.do">전체</a></li>
		<c:forEach var="cvo" items="${categoryList}">
			<li><a href="<%=request.getContextPath()%>/mallByCategory.do?code=${cvo.code}">${cvo.cname}</a></li>
		</c:forEach>
	</ul>
	</div>

</div>
</c:if>
