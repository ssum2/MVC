<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="member.model.MemberVO" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%-- >> fmt; 어떤 형식으로 출력해줄 것인가? --%>
<style type="text/css">

	table#loginTbl{
		width: 98%;
		border: 1px solid gray;
		border-collapse: collapse;
		
	}
	#th {
		background-color: navy;
		font-size: 14pt;
		color: silver;
	}
	
	table#loginTbl td {
		border: 1px solid gray;
		border-collapse: collapse;
	}

</style>

<script type="text/javascript">
	$(document).ready(function(){
		 
		if(${sessionScope.userid != null}){
			if($("#loginUserid").val().trim() == ""){
				$("#loginUserid").focus();
			}
			else {
				$("#loginPwd").focus();
			}
		}
		// 공통적으로 로그인기능을 처리하기 때문에 함수를 따로 만들어 호출
		$("#btnSubmit").click(function(){
			// login 버튼을 눌렀을 때
			goLogin();
			
		});
		
		$("#loginPwd").keydown(function(event){
			// password까지 다 입력하고 패스워드창에서 엔터를 입력했을 때
			if(event.keyCode==13){ // keyCode 13 ; enter
				goLogin();
			}
		});
		
		$(".myclose").click(function(){
			// alert("닫는다.");
			$("#loginUserid").val("${SessionScope.loginuser.userid}");
			javascript:history.go(0);
			// 현재 페이지를 새로고침을 함으로써 모달창에 입력한 성명과 휴대폰의 값이 텍스트박스에 남겨있지 않고 삭제하는 효과를 누린다. 
			
			/* === 새로고침(다시읽기) 방법 3가지 차이점 ===
			   >>> 1. 일반적인 다시읽기 <<<
			   window.location.reload();
			   ==> 이렇게 하면 컴퓨터의 캐쉬에서 우선 파일을 찾아본다.
			            없으면 서버에서 받아온다. 
			   
			   >>> 2. 강력하고 강제적인 다시읽기 <<<
			   window.location.reload(true);
			   ==> true 라는 파라미터를 입력하면, 무조건 서버에서 직접 파일을 가져오게 된다.
			            캐쉬는 완전히 무시된다.
			   
			   >>> 3. 부드럽고 소극적인 다시읽기 <<<
			   history.go(0);
			   ==> 이렇게 하면 캐쉬에서 현재 페이지의 파일들을 항상 우선적으로 찾는다.
			*/	
		});
	});
	
	
//	#login함수
	function goLogin(){
		var loginUserid = $("#loginUserid").val().trim();
		var loginPwd = $("#loginPwd").val().trim();
		
		if(loginUserid==""){
			alert("아이디를 입력하세요.");
			$("#loginUserid").val("");
			$("#loginUserid").focus();
			return;
		}
		if(loginPwd==""){
			alert("패스워드를 입력하세요.");
			$("#loginPwd").val("");
			$("#loginPwd").focus();
			return;
		}
		
		var frm = document.loginFrm;
		frm.method="post";
		frm.action="loginEnd.do";
		frm.submit();
	}


//	[181107]
//	#로그아웃 함수
	function goLogOut(){
		// logout.do로 페이지 이동만 시켜주면 됨 --> properties에 매핑된 controller로 가서 로그아웃처리
		location.href="<%= request.getContextPath() %>/logout.do";
	}

//	#나의 정보 보기
	function goPersonalInfo(idx){
		var frm = document.idx2Frm;
		frm.idx.value=idx;
		frm.currentURL.value = "index.do";
		frm.method = "POST";
    	frm.action = "memberDetail.do";
    	frm.submit();
	}
	
//	#나의 정보 변경
/* 	function goEditPersonal(idx){
		var url = "memberEdit.do?idx="+idx;
		window.open(url, "memberEdit", "left=150px, top=50px, width=800px, height=650px");
	} */

//	※Payment GateWay 관련 함수
//	#코인구매창 팝업창 띄우기
	function goCoinPurchaseTypeChoice(idx){	
		var url = "coinPurchaseTypeChoice.do?idx="+idx;
		window.open(url, "coinPurchaseTypeChoice", "left=350px, top=100px, width=650px, height=570px");
	}
	
//	#결제 팝업창 띄우기	
	function goCoinPurchaseEnd(idx, coinmoney){
//		alert("idx: "+ idx + ", 충전금액: "+coinmoney);
		var url = "coinPurchaseEnd.do?idx="+idx+"&coinmoney="+coinmoney;
		window.open(url, "coinPurchaseEnd", "left=350px, top=100px, width=820px, height=600px");
	}
	
//	function goCoinUpdate(idx, coinmoney){
	function goCoinUpdate(idx){
		var frm = document.coinUpdateFrm;
		frm.idx.value = idx;
//		frm.coinmoney.value = coinmoney;
		frm.action = "<%= request.getContextPath() %>/coinAddUpdateLoginuser.do";
		frm.method="POST";
		frm.submit();
	}
	
</script>
<% // [181107]; 로그인 전, 로그인 후 화면 구현하기
	MemberVO loginuser = (MemberVO)session.getAttribute("loginuser");
	if(loginuser == null){
	/*
		#로그인 하려고 할 때 WAS는 사용자 컴퓨터 웹브라우저로 부터 전송받은 쿠키를 유효한 쿠키인지 확인한 후 웹브라우저에 적용시킴
		--> 우리는 쿠키 "saveid"가 있으면 로그인 ID 텍스트박스에 아이디 값을 자동적으로 올려주면 된다.
		
		#쿠키는 쿠키의 이름별로 여러개 저장되어 있으므로 쿠키를 가져올 때는 배열타입으로 가져옴
		-> 가져온 쿠키배열에서 개발자가 원하는 쿠키의 이름과 일치하는것을 찾기 위해 쿠키 이름을 하나하나씩 비교해야함
	*/
		Cookie[] cookieArr = request.getCookies(); // return Cookie[]
		
		String cookie_key = "";
		String cookie_value = "";
		boolean flag = false;
		if(cookieArr != null){
//			>> 클라이언트에서 보낸 쿠키 정보가 있는 경우 --> 하나씩 꺼내서 키 값을 대조
			for(Cookie c :cookieArr){
				cookie_key = c.getName(); // 쿠키의 key(name)을 얻어옴
				
				if("saveid".equals(cookie_key)){
					cookie_value = c.getValue(); // 쿠키의 value값을 얻어옴
					flag = true;
					break;	// for문 탈출
				} // end of innerif(2)
			} // end of for

		} // end of innerif(1)
		
//	#로그인 하기 이전 화면
%>
	<form name="loginFrm">
		<table id="loginTbl">
			<thead>
				<tr>
					<th colspan="2" id="th" style="text-align: center;">LOGIN</th>
				</tr>
			</thead>
			
			<tbody>
				<%-- ID/PW 입력 --%>
				<tr>
					<td style="width: 30%; border-bottom: none; border-right: none; padding: 10px;">ID</td>
					<td style="width: 70%; border-bottom: none; border-left: none; padding: 10px;">
					<input type="text" id="loginUserid" name="userid" size="13" class="box" 
					<% if(flag){ %>
						value="<%=cookie_value%>"					
					<% } %>
					/></td>
				</tr>
				<tr>
					<td style="width: 30%; border-top: none; border-right: none; padding: 10px;">PW</td>
					<td style="width: 70%; border-top: none; border-left: none; padding: 10px;">
					<input type="password" id="loginPwd" name="pwd" size="13" class="box"/></td>
				</tr>
				<%-- 아이디 찾기, 비밀번호 찾기 --%>
				<tr>
					<td colspan="2" align="center">
						<a style="cursor: pointer;" data-toggle="modal" data-target="#findUserid" data-dismiss="modal">아이디찾기</a> /
						<a style="cursor: pointer;" data-toggle="modal" data-target="#findPW" data-dismiss="modal">비밀번호찾기</a>
					</td>
				</tr>
				<tr>
					<td colspan="2" align="center"  style="padding: 10px;">
					<%	// #아이디저장 체크 여부에 따라서 체크박스 표시를 달리해줌 --> flag 값 이용
						if(flag == false){ // 체크해제		%>
						<input type="checkbox" id="saveid" name="saveid" style="vertical-align: text-top;"/>
						<label for="saveid" style="margin-right: 20px; vertical-align: middle;">아이디저장</label>
						<%-- style==> vertical-align: text-top, text-bottom, middle; 수직 정렬하기 --%>		
					<% }
						else { // 체크 %>	
						<input type="checkbox" id="saveid" name="saveid" style="vertical-align: text-top;" checked/>
						<label for="saveid" style="margin-right: 20px; vertical-align: middle;">아이디저장</label>
					<% } %>
						
						<button type="button" id="btnSubmit" style="width: 67px; height: 27px; background-image: url('<%=request.getContextPath()%>/images/login.png'); vertical-align: middle; border: none;"></button>
					</td>
				</tr>
			</tbody>
		</table>
	</form>
<%		
	}
	else {
		// 로그인 한 후 화면
%>	

	<table style="width: 95%; height: 130px; margin-left: 2.5%;">
	  <tr style="background-color: #2B3A42;">
	    <td align="center" style="color: #F0F0DF">
	    	<%-- (sessionScope.세션attribute에 있는 객체).get메소드의 이름  ; 괄호쳐주지 않아도 OK--%>
	    	<span style="color: #FF8F00; font-weight: bold;">${(sessionScope.loginuser).name}</span>
	  	   [<span style="color: #BDD3DE; font-weight: bold;">${(sessionScope.loginuser).userid}</span>]님<br/><br/>
	  	    <div align="left" style="padding-left: 27%; line-height: 150%;">
	  	      <span style="font-weight: bold;">코인액 :</span>&nbsp;&nbsp;<fmt:formatNumber value="${(sessionScope.loginuser).coin}" pattern="###,###" /> 원
	  	      <%-- fmt:formatNumber value="나타낼 숫자" pattern="###,### (나타낼 형식)" --%>
	  	  	  <br/>   
	  	      <span style="font-weight: bold;">포인트 :</span>&nbsp;&nbsp;<fmt:formatNumber value="${(sessionScope.loginuser).point}" pattern="###,###" /> POINT
	  	  	</div>
	  	  	<br/>로그인 중...<br/><br/>
	   <%-- [<a href="javascript:goEditPersonal('${(sessionScope.loginuser).idx}');">나의정보</a>]&nbsp;&nbsp; --%>
	  	  	<a style="color: #FF8F00;" href="javascript:goPersonalInfo('${(sessionScope.loginuser).idx}');">나의정보</a>&nbsp;&nbsp;
	  	  	<a style="color: #FF8F00;" href="javascript:goCoinPurchaseTypeChoice('${(sessionScope.loginuser).idx}');">코인충전</a> 
	  	  	<br/><br/>
	  	  	<button type="button" class="btn btn-warning" onClick="goLogOut();" style="cursor: pointer; color: #2B3A42;">로그아웃</button>
	    </td>
	  </tr>
	  
	</table>
	
	<form name="idx2Frm">
	<input type="hidden" name="idx"/>
	<input type="hidden" name="currentURL"/>
	</form>
	
<%		
	}
%>

  <%-- ****** 아이디 찾기 Modal ****** --%>
  <div class="modal fade" id="findUserid" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">아이디 찾기</h4>
        </div>
        <div class="modal-body" style="height: 300px; width: 100%;">
          <div id="idFind">
          	<iframe style="border: none; width: 100%; height: 280px;" src="<%= request.getContextPath() %>/idFind.do">
          	</iframe>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>   


  <%-- ****** 비밀번호 찾기 Modal ****** --%>
  <div class="modal fade" id="findPW" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <div class="modal-header">
          <button type="button" class="close myclose" data-dismiss="modal">&times;</button>
          <h4 class="modal-title">비밀번호 찾기</h4>
        </div>
        <div class="modal-body">
          <div id="pwFind">
          	<iframe style="border: none; width: 100%; height: 350px;" src="<%= request.getContextPath() %>/pwdFind.do">  
          	</iframe>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" class="btn btn-default myclose" data-dismiss="modal">Close</button>
        </div>
      </div>
      
    </div>
  </div>

<%-- #PG에 코인금액을 카드로 결제 후 DB에서 사용자의 코인액을 변경해주는 폼 --%>
<form name="coinUpdateFrm">
	<input type="hidden" name="idx"/>
<%-- <input type="hidden" name="coinmoney"/> --%>	
</form>