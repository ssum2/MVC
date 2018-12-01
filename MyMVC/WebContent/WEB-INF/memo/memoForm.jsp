<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="../header.jsp"/>

<style type="text/css">
    table#tblMemo, table#tblMemo th {border: 0px solid gray;
	               			 border-collapse: collapse;}
	                   
    table#tblMemo td {border: 0px solid gray;
	              border-collapse: collapse;
	              padding: 10px;}	
	                  
    input {height: 30px;}                                     
</style>

<script type="text/javascript">
	$(document).ready(function(){
		$("#msg").focus(); // 해당 페이지에 접근하면 즉각 실행됨
		$("#msg").keydown(function(event){
			if(event.keyCode==13){ // keyCode 13 ; enter
				goSubmit();
			}
		});
	});

	function goSubmit(){
		var msg = $("#msg").val().trim();
		if(msg == ""){
			alert("메모내용을 입력하세요.");
			return;
		}
		var frm = document.memoFrm;
		frm.method = "POST"; // insert, update같은 경우는 데이터를 전송해야하기 때문에 POST방식으로 보냄
		frm.action = "memoWriteEnd.do";
		frm.submit();	
	}
</script>

<div class="row">
	<div class="col-md-12">
		<form name="memoFrm">
		<table id="tblMemo" style="width: 90%;">
		  <thead>
		  	<tr style="line-height: 80px;">
		  		<th colspan="2" style="text-align: center; font-size: 20pt;"> 〓〓 한줄 메모 작성 〓〓</th>
		  	</tr>
		  </thead>
		  
		  <tbody> 
			<tr>	
				<td style="font-weight: bold;">작성자</td>
				<td align="left">
					<%--
				    <input type="hidden" name="fk_userid" size="20" value="${sessionScope.loginuser.userid}" />
				    --%>											<%-- 세션에 저장해둔 객체에서 userid와 name을 가져옴 --%>					
					<input type="text" size="20" value="${sessionScope.loginuser.name}" class="box" readOnly/> 
					<%-- readonly; 두번째 input textbox에는 로그인 시 자동으로 로그인한 유저의 이름이 들어감 --%>
				</td>  
			</tr>
			<tr>
				<td style="font-weight: bold;">메모내용</td>
				<td align="left">
					<input type="text" name="msg" id="msg" size="70" maxlength="100" class="box" />
					<button type="button" style="margin-left: 20px;" onClick="goSubmit();" >메모남기기</button>
				</td>	
			</tr>  	
		  </tbody>
		</table>
		</form>
	</div>
</div>

<jsp:include page="../footer.jsp"/>