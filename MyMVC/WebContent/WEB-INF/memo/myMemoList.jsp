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
			frm.action = "myMemoList.do";	
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
	
//	#나의 메모 삭제 메소드
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
	
//	#나의 메모 공개/비공개 메소드
	function msgOpenBlind(btn) {
		var flag = false;
		var blindmsg ="비";
		var msg1 ="공개할 글을 선택하세요.";
		var msg2 ="공개로 전환하시겠습니까?";
		var msg3 ="공개 전환이 취소되었습니다.";
		var statusVal = "1";
		
		$(".delCheck").each(function (){
			if($(this).is(":checked")){
				flag = true;
				return false;
			}
		});
		if(btn=="0"){
			msg1 = blindmsg+msg1;
			msg2 = blindmsg+msg2;
			msg3 = blindmsg+msg3;
			statusVal = "0";
		}
		if(!flag){
			alert(msg1);
			return;
		}
		else {
			var bool = confirm(msg2);
			if(bool){
				var frm = document.delCheckFrm;
				frm.status.value = statusVal;
				frm.method = "POST";
				frm.action = "memoOpenBlind.do";
				frm.submit();	
			}
			else {
				alert(msg3);
				$(".delCheck").prop("checked", false);
			}
		}
	}
	
	
//	# 메모 공개/비공개 메소드
/* 	function msgOpen() {
		var flag = false;
		$(".delCheck").each(function (){
			if($(this).is(":checked")){
				flag = true;
				return false;
			}
		});
		
		if(!flag){
			alert("공개할 글을 선택하세요.");
			return;
		}
		else {
			var bool = confirm("선택하신 글을 공개로 전환하시겠습니까?");
			if(bool){
				var frm = document.delCheckFrm;
				frm.status.value = "1";
				frm.method = "POST";
				frm.action = "memoOpenBlind.do";
				frm.submit();
			}
			else {
				alert("공개 전환이 취소되었습니다.");
				$(".delCheck").prop("checked", false);
			}
		}
	}
	
	function msgBlind() {
		var flag = false;
		$(".delCheck").each(function (){
			if($(this).is(":checked")){
				flag = true;
				return false;
			}
		});
		
		if(!flag){
			alert("비공개할 글을 선택하세요.");
			return;
		}
		else {
			var bool = confirm("선택하신 글을 비공개로 전환하시겠습니까?");
			if(bool){
				var frm = document.delCheckFrm;
				frm.status.value = "0";
				frm.method = "POST";
				frm.action = "memoOpenBlind.do";
				frm.submit();
				
			}
			else {
				alert("비공개 전환이 취소되었습니다.");
				$(".delCheck").prop("checked", false);
			}
		}
	} */
	
</script>
<div class="row">
   	<div class="col-md-12">
   	   <h2>::: 나의 한줄 메모장 목록 :::</h2>
   	</div>
</div>
   
<div class="row" style="margin-top: 20px; margin-bottom: 20px;">
	<div class="col-md-4" style="border: 0px solid gray;">
		<button type="button" onClick="msgDel();">메모내용 삭제</button>&nbsp;
		<button type="button" onClick="msgOpenBlind(1);">메모내용 공개</button>&nbsp;
		<button type="button" onClick="msgOpenBlind(0);">메모내용 비공개</button>&nbsp;
	</div>
	<div class="col-md-3" style="border: 0px solid gray;">
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
					<th width="8%" style="text-align: center;">
						<input type="checkbox" id="allCheck" name="allCheck"/>
						<span style="color: maroon; font-size: 10pt;"><label for="allCheck">전체선택</label></span>
					</th>
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
						<input type="hidden" name="status" />
					<c:forEach var="memovo" items="${memoList}">
						<c:if test="${memovo.status==0}">
						<tr style="background-color: darkgray;">
						</c:if>
						<c:if test="${memovo.status==1}">
						<tr>
						</c:if>
							<td style="text-align:center;">
								<input type="checkbox" id="delCheck" name="delCheck" class="delCheck" value="${memovo.idx}"/>
														<%-- 체크박스에 idx를 밸류로 부여하여 form을 제출했을 때 가져올수 있게 함 --%>
							</td>
							<%-- ${memovo.get~} ==> VO의 get이하 이름 --%>
							<td style="text-align:center;">${memovo.idx}</td>
							<td style="text-align:center;">${memovo.name}</td>
							<td style="text-align:center;">${fn:replace(memovo.msg, "<", "&lt;")}</td>
							<td style="text-align:center;">${memovo.writedate}</td>
							<td style="text-align:center;">${memovo.cip}</td>
						</tr>
					</c:forEach>
					</form>
				</c:if>
			</tbody>
			
			<thead>
				<tr>
					<th colspan="4" class="th" style="padding-left: 20%;">
						${pageBar}
					</th>
					<th colspan="2" style="text-align: right;">
						현재 [<span style="color: #B43846;">${currentShowPageNo}</span>] 페이지 / 총 [${totalPage}] 페이지&nbsp;
						글 수: 총 ${totalMemoCount}개
					</th>

					
				</tr>
			</thead>
		</table>
	</div>
</div>

<jsp:include page="../footer.jsp"/>