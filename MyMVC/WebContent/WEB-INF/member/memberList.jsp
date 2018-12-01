<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="../header.jsp"/>

<style type="text/css">
	.th{text-align: center;}
	.td{text-align: center;}
	.namestyle {
		background-color: #153E5C;
		font-weight: bold;
		font-size: 11pt;
		color: #F2BE54;
		cursor: pointer;
	}
</style>

<script type="text/javascript">
	$(document).ready(function(){
		
//		#페이지 당 보여줄 개체의 갯수를 정하는 드롭박스를 선택했을 때
		$("#sizePerPage").bind("change", function(){ 
						// >> dropbox에서의 이벤트는 "change"
			var frm = document.memberFrm	// 페이지 당 회원명수 정보를 담은 form
			frm.method = "GET"; // >> 보안X, 속도 빠름
			frm.action = "memberList.do";	// 자기 자신에게 보냄 => 처음부터 다시 실행됨
			frm.submit();
		});

		$("#sizePerPage").val("${sizePerPage}");	
		// 					>> request에 담겨있는 attribute를 가져올 때는 requestScope를 생략 가능
//		#날짜구간 form 전달하기		
		$("#period").bind("change", function(){
			var frm = document.memberFrm
			frm.method = "GET";
			frm.action = "memberList.do";
			frm.submit();
		});
		
		$("#period").val("${period}");
		
// 검색 드롭박스, 텍스트박스에 value값 넣어주기
		$("#searchType").val("${searchType}");
		$("#searchWord").val("${searchWord}");

//	name에 마우스 올렸을 때 스타일 변경
		$(".name").hover(function(){
			$(this).addClass("namestyle");
		}, function(){
			$(this).removeClass("namestyle");
		});
		
//	#검색어를 입력하고 enter를 입력한 경우	
		$("#searchWord").keydown(function(event){
			if(event.KeyCode == 13){
				// >> event.KeyCode == 엔터 했을 때의 경우(13)
				goSearch();
			}
		});
	}); // end of ready

// #검색하기 버튼을 눌렀을 때 폼 제출
	function goSearch() {
		var searchWord = $("#searchWord").val().trim();
		
		if(searchWord == ""){
			alert("검색어를 입력하세요.");
			return;
			
		}
		else {
			var frm = document.memberFrm;
			frm.method= "GET";
			frm.action= "memberList.do";
			frm.submit();
		}
	}

//	#회원정보 수정하기
 	function goEdit(idx){
		var url = "memberEdit.do?idx="+idx;
		window.open(url, "memberEdit", "left=150px, top=50px, width=800px, height=650px");
		
	}
	
//	#회원 활성화/비활성화
	function goEnable(idx, currentURL){
		var bool = confirm(idx + "번 회원을 활성화/비활성화 하시겠습니까?"); 
	    if(bool) {
	    	var frm = document.idxFrm;
	    	frm.idx.value=idx;
	    	frm.currentURL.value = currentURL;
	    	frm.method = "POST";
	    	frm.action = "memberIdleClear.do";
	    	frm.submit();
	    }	
	}

//	#회원 삭제 하기
	function goDel(idx, currentURL) {
		//		idx값과 돌아갈 페이지 url주소를 파라미터로 받아옴
		var bool = confirm(idx + "번 회원을 정말로 삭제하시겠습니까?"); 
	//	alert(bool); ==> 확인이면 true, 취소이면 false
	// confirm; yes / no 팝업창
	
	    if(bool) {
	    	var frm = document.idxFrm;
	    	frm.idx.value=idx; // hidden input태그 안에 받아온 idx값을 넣어줌
	    	frm.currentURL.value = currentURL;
	    	frm.method = "POST";
	    	frm.action = "memberDelete.do";
	    	frm.submit();
	    }		
	}// end of goDel(idx, currentURL)

//	#회원 복원 하기
	function goRecovery(idx, currentURL) {
		var bool = confirm(idx + "번 회원을 복원하시겠습니까?"); 
	    if(bool) {
	    	var frm = document.idxFrm;
	    	frm.idx.value=idx;
	    	frm.currentURL.value = currentURL;
	    	frm.method = "POST";
	    	frm.action = "memberRecovery.do";
	    	frm.submit();
	    }		
	}// end of goRecovery(idx, currentURL)	
	
//	#회원 상세정보 페이지로 이동	
	function goDetail(idx, currentURL){
		var frm = document.idxFrm;
	    frm.idx.value = idx;
	    frm.currentURL.value = currentURL;
		frm.method = "GET";
    	frm.action = "memberDetail.do";
    	frm.submit();
	}
</script>

<div class="row">
	<div class="col-md-12">
		<h2 style="margin-bottom: 40px;">::: 회원 전체 정보 보기 :::</h2>
		<form name="memberFrm">
			<%-- 회원명, 아이디, 이메일 키워드 검색 --%>
			<select id="searchType" name="searchType"><%-- id는 javascript, jquery에서 사용하기 위해 반드시 부여 --%>
				<option value="name">회원명</option>
				<option value="userid">아이디</option>
				<option value="email">이메일</option>
			</select>
			<input type="text" id="searchWord" name="searchWord" size="25" class="box" style="margin-left: 10px; margin-right: 10px;"/>
			<button type="button" onClick="goSearch();">검색</button>
			
			<div style="margin-top:20px; margin-bottom: 20px;">
				<div style="display: inline; margin-right: 10px;">
					<%-- display: block(기본값; 아래로 배치) / inline(옆으로 배치) --%>
					<span style="color: #F25652; font-weight: bold;">▶페이지당 회원명수</span>&nbsp;
					<select id="sizePerPage" name="sizePerPage">
						<option value="10">10</option>
						<option value="5">5</option>
						<option value="3">3</option>
					</select>
				</div>
				
				<%-- 날짜구간 검색 --%>
				<div style="display: inline; margin-left: 20px;"> 
					<select id="period" name="period">
						<option value="-1">전체</option>
						<option value="3">최근 3일이내</option>
						<option value="10">최근 10일이내</option>
						<option value="30">최근 30일이내</option>
						<option value="60">최근 60일이내</option>
					</select>
				</div>
			</div>
			
		</form>
		<table class="outline">
			<thead> <%-- 타이틀 --%>
				<tr>
					<th class="th">회원번호</th>
					<th class="th">회원명</th>
					<th class="th">아이디</th>
					<th class="th">이메일</th>
					<th class="th">휴대폰</th>
					<th class="th">가입일자</th>
					<c:if test="${sessionScope.loginuser.userid == 'admin'}">
					<th class="th">마지막 로그인 일시</th>
					<th class="th">마지막 암호변경 일시</th>
					<th class="th">회원관리</th>
					</c:if>
				</tr>
			</thead>
			
			<tbody>	<%-- 테이블 본 내용 --%>
			
			<%-- #memberList에 받아온 값이 null이 아닐 때(회원정보가 존재할 때) 정보 출력 --%>
			<c:if test="${not empty memberList}">
			<%-- 	 >> ${memberList != null} --%>
				<c:forEach var="membervo" items="${memberList}">
					<c:if test="${sessionScope.loginuser.userid == 'admin'}">
						<%--#status의 상태에 따라서 출력할 회원의 배경색을 다르게 해줌 --%>				
						<c:if test="${membervo.status == 0}"> <%-- VO객체.get이하 이름 , HashMap.key값 --%>
							<tr style="background-color: darkgray;">
						</c:if>
						<c:if test="${membervo.status == 1 && membervo.requireCertify == false}">
							<tr>
						</c:if>
						<c:if test="${membervo.status == 1 && membervo.requireCertify == true}">
							<tr style="background-color: yellow;">
						</c:if>
						<td class="td">${membervo.idx}</td>
						<td class="td name" onClick= "goDetail('${membervo.idx}', '${currentURL}')">${membervo.name}</td>
											<%-- '${~~}' 문자열로 잡아줌 
											 => 자바스크립트는 해당 데이터의 타입에 따라서 변수타입도 정해지고,
											 	DB에서는 문자열타입이여도 number컬럼을 조회 하기 때문에 '~'을 사용해String타입으로 치환해줌
											
											>> javascript:location.href='사용할 jsp파일?넘겨줄 데이터'
											  "javascript:location.href='memberDetail.do?idx=${membervo.idx}&goBackURL=${currentURL}'" --%>
						<td class="td">${membervo.userid}</td>
						<td class="td">${membervo.email}</td>
						<td class="td">${membervo.allHp}</td>
						<td class="td">${membervo.registerday}</td>
						<td class="td">${membervo.lastlogindate}</td>
						<td class="td">${membervo.lastpwdchangedate}</td>
						
						<td class="td">
						<c:if test="${membervo.requireCertify && membervo.status==1}">
						<a href="javascript:goEnable('${membervo.idx}', '${currentURL}');">휴면해제</a>&nbsp;
						</c:if>
						<c:if test="${!membervo.requireCertify && membervo.status==1}">
						<a href="javascript:goEdit('${membervo.idx}', '${currentURL}');">정보수정</a>&nbsp;
						</c:if>
						
						<c:if test="${membervo.status==1}">
						<a href="javascript:goDel('${membervo.idx}', '${currentURL}');">삭제</a>
						</c:if>
						<c:if test="${membervo.status==0}">
						<a href="javascript:goRecovery('${membervo.idx}', '${currentURL}');">복원</a>
						</c:if>
						</td> 			
						</tr>
				</c:if>
				<c:if test="${sessionScope.loginuser.userid != 'admin'}">			
					<c:if test="${membervo.status != 0 && !membervo.requireCertify}">
					<tr>
						<td class="td">${membervo.idx}</td>
						<td class="td name" onClick= "goDetail('${membervo.idx}', '${currentURL}')">${membervo.name}</td>
						<td class="td">${membervo.userid}</td>
						<td class="td">${membervo.email}</td>
						<td class="td">${membervo.allHp}</td>
						<td class="td">${membervo.registerday}</td>			
					</tr>
					</c:if>
				</c:if>		
			</c:forEach>
		</c:if>
			<c:if test="${empty memberList}">
					<tr>
						<td colspan="7" style="text-align: center; color: red; font-weight: bold;">가입된 회원이 없습니다.</td>
					</tr>
			</c:if>
			</tbody>
			
			<%-- 페이지바 만들기(1블럭 당 10개체) --%>
			<thead>
				<tr>
				<c:if test="${sessionScope.loginuser.userid == 'admin'}">
					<th colspan="6" class="th">
						${pageBar}
					</th> 
					<th colspan="3">
				</c:if>
				<c:if test="${sessionScope.loginuser.userid != 'admin'}">
					<th colspan="4" class="th">
						${pageBar}
					</th> 
					<th colspan="3">
				</c:if>
					<%-- 전체페이지가 총 몇페이지인지 보여주는 부분 --%>
						현재 [<span style="color: #B43846;">${currentShowPageNo}</span>] 페이지 / 총 [${totalPage}] 페이지&nbsp;
						회원수: 총 ${totalMemberCount}명
					
					</th>
				</tr>
			</thead>
		</table>
	</div>
</div>

<%-- 회원정보 삭제 및 특정 회원 상세조회를 위한 폼 생성 --%>
<form name="idxFrm">
	<input type="hidden" name="idx"/>
	<%-- 웹화면에는 보이지 않지만 값을 저장할 수 있는 공간을 만듦 --%>
	<input type="hidden" name="currentURL"/>
</form>

<jsp:include page="../footer.jsp"/>