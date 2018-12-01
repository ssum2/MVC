<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<jsp:include page="../header.jsp" />

<style type="text/css" >
	table#tblOrderList {width: 90%;
	                   border: solid gray 0px;
	                   margin-top: 20px;
	                   margin-left: 10px;
	                   margin-bottom: 20px;}
	                   
	table#tblOrderList th {border-top: solid gray 1px;
						  border-bottom: solid gray 1px;}
	table#tblOrderList tr {border-top: dotted gray 1px;
						  border-bottom: dotted gray 1px;} 
	td {
		text-align: center;
	} 	   
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
//		#배송시작 버튼		
		$("#btnDeliverStart").click(function(){
		// 배송시작, 배송완료 체크박스가 모두 체크되었을 경우에도 배송시작만 되도록 제한 (비활성화)
			$(".odrcode").prop("disabled", true);	// odrcode가 담겨있는 input태그 비활성화
			
		// 배송시작 체크박스들이 체크되었는지 확인
			var flag = false;
			$(".deliverStartPnum").each(function(){
				var bool = $(this).is(":checked");
				if(bool){	// 한 개라도 체크가 된 경우 그 체크박스에 딸려있는 odrcode input태그만 활성화
					$(this).next().next().prop("disabled", false);
				// >> 동급 태그이기 때문에 parent로 가서 find하기보다는 next로 찾는게 쉬움
				// checkbox > label > hidden odrcode
					flag=true;
				}
			}); // end of each
			if(!flag){	// 1개 이상 체크가 되지 않은 경우
				alert("배송상태를 변경할 물품을 1개 이상 선택해주세요.");
				return;
			}
			else{ // 1개 이상 체크된 경우 --> odrcode 전송
				var frm = document.frmDeliver;
				frm.method="POST";	// DML은 POST로 처리
				frm.action="deliverStart.do";
				frm.submit();
			}
		});
		
//		#배송완료 버튼		
		$("#btnDeliverEnd").click(function(){
			$(".odrcode").prop("disabled", true);
			
			var flag = false;
			$(".deliverEndPnum").each(function(){
				var bool = $(this).is(":checked");
				if(bool){
					$(this).next().next().prop("disabled", false);
					flag=true;
				}
			}); // end of each
			if(!flag){
				alert("배송상태를 변경할 물품을 1개 이상 선택해주세요.");
				return;
			}
			else{ // 1개 이상 체크된 경우 --> odrcode 전송
				var frm = document.frmDeliver;
				frm.method="POST";
				frm.action="deliverEnd.do";
//				frm.submit();
			}
		});
		
		
		
	}); // end of $(document).ready()
	
//	#전체 체크박스(관리자모드; 배송시작, 배송완료)
	function allCheckBoxStart() {
		var bool = $("#allCheckStart").is(':checked');
		$(".deliverStartPnum").prop('checked', bool)	
	}
	
	function allCheckBoxEnd() {
		var bool = $("#allCheckEnd").is(':checked');
		$(".deliverEndPnum").prop('checked', bool)	
	}
	
//	#사용자 정보 보기(관리자모드; odrcode로 select)
	function openMember(odrcode){
		var url = "memberInfo.do?odrcode="+odrcode;
		
		// 팝업창 띄우기
		window.open(url, "memberInfo", "width=550px, height=600px, top=50px, left=100px");
	}
</script>


<c:set var="userid" value="${sessionScope.loginuser.userid}"/>
<c:if test="${userid eq 'admin'}">
<h2 style="font-weight: bold;">::: 주문 내역 전체 목록 :::</h2>	
</c:if>

<c:if test="${userid ne 'admin'}">
<h2 style="font-weight: bold;">::: ${(sessionScope.loginuser).name} 님[ ${userid} ] 주문 내역 :::</h2>	
</c:if>

<form name="frmDeliver">
<table id="tblOrderList" style="width: 95%;">

	<tr>
		<th colspan="5" style="text-align: right; font-weight: bold; border-right-style: none;"> 주문내역 보기 </th>
		<th colspan="2" style="text-align: right; border-left-style: none;">
			<span style="color: red; font-weight: bold;">페이지당 갯수-</span>
			<select id="sizePerPage" style="width: 60px;">
				<option value="10">10</option>
				<option value="5">5</option>
				<option value="3">3</option>
			</select>
	    </th>
	</tr>

  <c:if test="${userid eq 'admin'}">
	<tr>	
		<td colspan="7" align="right" style="text-align: right;"> 
			<input type="checkbox" id="allCheckStart" onClick="allCheckBoxStart();"><label for="allCheckStart"><span style="color: #FF8F00; font-weight: bold; font-size: 9pt;">전체선택(배송시작)</span></label>&nbsp;
			<input type="button" class="btn btn-warning" name="btnDeliverStart" id="btnDeliverStart" value="배송시작" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
			
			<input type="checkbox" id="allCheckEnd" onClick="allCheckBoxEnd();"><label for="allCheckEnd"><span style="color: green; font-weight: bold; font-size: 9pt;">전체선택(배송완료)</span></label>&nbsp;
			<input type="button" class="btn btn-success" name="btnDeliverEnd" id="btnDeliverEnd" value="배송완료">
		</td>
	</tr>
  </c:if>
		
    <tr bgcolor="#cfcfcf">
		<td align="center" width="15%">주문코드(전표)</td>
		<td align="center" width="15%">주문일자</td>
		<td align="center" width="30%">제품정보</td> <%-- 제품번호,제품명,제품이미지1,판매정가,판매세일가,포인트 --%>
		<td align="center" width="10%">수량</td>
		<td align="center" width="10%">금액</td>   
		<td align="center" width="10%">포인트</td>
		<td align="center" width="10%">배송상태</td>
    </tr>
	<c:if test="${orderList==null || empty orderList}" > 
	  <tr>
		  <td colspan="7" align="center">
		  <span style="color: red; font-weight: bold;">주문내역이 없습니다.</span>
	  </tr>
	</c:if>


	<c:if test="${orderList != null && not empty orderList }">
		<c:forEach var="map" items="${orderList}" varStatus="status">
			<tr>
				<td align="center"> <%-- 주문코드(전표) 출력하기. 
				      만약에 관리자로 들어와서 주문내역을 볼 경우 해당 주문코드(전표)를 클릭하면 
				      주문코드(전표)를 소유한 회원정보를 조회하도록 한다.  --%>
				<c:if test='${userid eq "admin" }'>
					<a href="#" onClick="openMember('${map.odrcode}')">${map.odrcode}</a>
					<%-- 앵커태그에서 링크를 #으로 주면 --> onClick에 있는 함수를 실행 --%>
				</c:if>
				<c:if test='${userid ne "admin"}'>
					${map.odrcode}
				</c:if>	 
				</td>
				<td align="center"> <%-- 주문일자 --%>
					${map.odrdate}
				</td>
				<td align="center">  <%-- === 제품정보 넣기 === --%>
					<img src="images/${map.pimage1} " width="130" height="100" />  <%-- 제품이미지1 --%>
					<br/>제품번호 : ${map.fk_pnum}  <%-- 제품번호 --%>
					<br/>제품명 : ${map.pname}      <%-- 제품명 --%>
					<br/>판매정가 : <span style="text-decoration: line-through;"><fmt:formatNumber value="${map.price}" pattern="###,###" /> 원</span>   <%-- 제품개당 판매정가 --%>
					<br/>판매가 : <span style="color: #FF8F00; font-weight: bold;"><fmt:formatNumber value="${map.saleprice}" pattern="###,###" /></span>원  <%-- 제품개당 판매세일가 --%> 
					<br/>포인트 : <span style="color: #FF8F00; font-weight: bold;"><fmt:formatNumber value="${map.point}" pattern="###,###" /></span>point  <%-- 제품개당 포인트 --%>
				</td>
				<td align="center">    <%-- 수량 --%>
					 ${map.oqty} 개
				</td>
				<td align="center">    <%-- 금액 --%>
				     <c:set var="su" value="${map.oqty}" />
				     <c:set var="danga" value="${map.saleprice}" />
				     <c:set var="totalmoney" value="${su * danga}" />
				     
					 <fmt:formatNumber value="${totalmoney}" pattern="###,###" /> 원
				</td>
				<td align="center">    <%-- 포인트 --%>
				     <c:set var="point" value="${map.point}" />
				     <c:set var="totalpoint" value="${su * point}" />
					 <fmt:formatNumber value="${totalpoint}" pattern="###,###" /> 포인트
				</td>
				<td align="center"> <%-- 배송상태 --%>
				
					<c:choose>
						<c:when test="${map.deliverstatus == '주문완료'}">
							주문완료<br/>
						</c:when>
						<c:when test="${map.deliverstatus == '배송시작'}">
							<span style="color: #FF8F00; font-weight: bold; font-size: 12pt;">배송시작</span><br/>
						</c:when>
						<c:when test="${map.deliverstatus == '배송완료'}">
							<span style="color: green; font-weight: bold; font-size: 12pt;">배송완료</span><br/>
						</c:when>
					</c:choose>
	
					<c:if test='${userid eq "admin" }'>	<%-- 관리자메뉴 --%>
						<br/><br/>
						<c:if test="${map.deliverstatus == '주문완료'}">
							<input type="checkbox" class="deliverStartPnum" name="deliverStartPnum" id="chkDeliverStart${status.index}" value="${map.fk_pnum}"><label for="chkDeliverStart${status.index}">배송시작</label> 
							<input type="hidden" class="odrcode" name="odrcode" value="${map.odrcode}"  />
							<input type="hidden" name="deliverStatus" value="${map.deliverstatus}" />
						</c:if>
						<br/>
						<c:if test="${map.deliverstatus == '주문완료' or map.deliverstatus == '배송시작'}">
							<input type="checkbox" class="deliverEndPnum" name="deliverEndPnum" id="chkDeliverEnd${status.index}" value="${map.fk_pnum}"><label for="chkDeliverEnd${status.index}">배송완료</label>
							<input type="hidden" class="odrcode" name="odrcode" value="${map.odrcode}"  />
							<input type="hidden" name="deliverStatus" value="${map.deliverstatus}" />
						</c:if>
					</c:if>
				</td>
			</tr>
		</c:forEach>
		</c:if>
		
</table>
</form>
   
<jsp:include page="../footer.jsp" />
