<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<jsp:include page="../header.jsp"/>
<style type="text/css">
	.th{text-align: center;}
	.td{text-align: center;}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		//#페이지 당 보여줄 개체의 갯수를 정하는 드롭박스를 선택했을 때
		$("#sizePerPage").bind("change", function(){ 
			var frm = document.sizePerPageFrm;
			frm.method = "GET";
			frm.action = "memoList.do";	
			frm.submit();
		});
		
		$("#sizePerPage").val("${sizePerPage}");	
	
		// 전체선택 체크 시 나머지 delCheck 체크/체크해제
		$("#allCheck").click(function(){
			$(".delCheck").prop("checked", $(this).is(":checked"));
			// $(".input체크박스의 클래스명").prop("checked", $(this).is(":checked"))
			// >> 현재 이벤트를 발생시킨 선택자의 checkbox의 상태에 따라서 해당 선택자의 체크유무를 판별함
		});
	});
	
	function msgDel() {
		var flag = false;
		$(".delCheck").each(function (){
			if($(this).is(":checked")){
				flag = true;
				return false;
			}
		});
		if(!flag){
			alert("삭제할 글을 선택하세요.");
			return;
		}
		else {
			var bool = confirm("선택하신 글을 삭제하시겠습니까?");
			if(bool){
				var frm = document.delCheckFrm;
				frm.method = "POST";
				frm.action = "memoDelete.do";
				frm.submit();
			}
			else {
				alert("삭제가 취소되었습니다.");
				$(".delCheck").prop("checked", false);
			}
		}
	}
</script>
<div class="row">
   	<div class="col-md-12">
   	   <h2>::: 한줄 메모장 목록 :::</h2>
   	</div>
</div>
   
<div class="row" style="margin-top: 20px; margin-bottom: 20px;">
	<c:if test="${sessionScope.loginuser.userid == 'admin'}">
	<div class="col-md-2" style="border: 0px solid gray;">
		<button type="button" onClick="msgDel();">메모내용 삭제</button>
	</div>
	<div class="col-md-6 col-md-offset-1" style="border: 0px solid gray;">
	</c:if>  
	<c:if test="${sessionScope.loginuser.userid != 'admin'}">
	<div class="col-md-6 col-md-offset-3" style="border: 0px solid gray;">
	</c:if>
		<form name="sizePerPageFrm">
		<span style="color: maroon; font-weight: bold;">페이지당 글갯수-</span>
		<select name="sizePerPage" id="sizePerPage">
			<option value="10">10</option>
			<option value="5">5</option>
			<option value="3">3</option>
		</select>
		</form>
	</div>
</div>
   
<div class="row">
	<div class="col-md-12" style="border: 0px solid gray;">
		<table style="width: 95%;" class="outline">
			<thead>
				<tr>
					<c:if test="${sessionScope.loginuser.userid == 'admin'}">
					<th width="8%" style="text-align: center;">
						<input type="checkbox" id="allCheck" name="allCheck"/>
						<span style="color: maroon; font-size: 10pt;"><label for="allCheck">전체선택</label></span>
					</th>
					</c:if>
					<th width="5%" style="text-align: center;">글번호</th>
					<th width="10%" style="text-align: center;">작성자</th>
					<th width="50%" style="text-align: center;">글내용</th>
					<th width="17%" style="text-align: center;">작성일자</th>
					<th width="10%" style="text-align: center;">IP주소</th>
				</tr>
			</thead>
			
			<tbody>
				<c:if test="${memoList == null}">
					<tr>
						<td colspan="6">데이터가 없습니다</td>
					</tr>
				</c:if>
				<c:if test="${memoList != null}">
					<form name="delCheckFrm">
					<c:forEach var="map" items="${memoList}">
						<tr>
							<c:if test="${sessionScope.loginuser.userid == 'admin'}">
							<td style="text-align:center;">
								<input type="checkbox" id="delCheck" class="delCheck" name="delCheck" value="${map.idx}"/>
							</td>
							</c:if>
							<%-- ${map.key} ==> DAO에서 HashMap에 넣어줄 때 설정한 key값 --%>
							<td style="text-align:center;">${map.idx}</td>
							<td style="text-align:center;">${map.name}</td>
							<td style="text-align:center;">${fn:replace(map.msg, "<", "&lt;")}</td>
							<td style="text-align:center;">${map.writedate}</td>
							<td style="text-align:center;">${map.cip}</td>
						</tr>
					</c:forEach>
					</form>
				</c:if>
			</tbody>
			
			<thead>
				<tr>
					<c:if test="${sessionScope.loginuser.userid == 'admin'}">
					<th colspan="4" class="th" style="padding-left: 20%;">
						${pageBar}
					</th>
					<th colspan="2" style="text-align: right;">
						현재 [<span style="color: #B43846;">${currentShowPageNo}</span>] 페이지 / 총 [${totalPage}] 페이지&nbsp;
						글 수: 총 ${totalMemoCount}개
					</th>
					</c:if>
					<c:if test="${sessionScope.loginuser.userid != 'admin'}">
					<th colspan="3" class="th" style="padding-left: 20%;">
						${pageBar}
					</th>
					<th colspan="2" style="text-align: right;">
						현재 [<span style="color: #B43846;">${currentShowPageNo}</span>] 페이지 / 총 [${totalPage}] 페이지&nbsp;
						글 수: 총 ${totalMemoCount}개
					</th>
					</c:if>
					
				</tr>
			</thead>
		</table>
	</div>
</div>
<jsp:include page="../footer.jsp"/>