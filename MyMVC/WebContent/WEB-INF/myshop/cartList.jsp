<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 

<jsp:include page="../header.jsp" />

<style type="text/css" >
	table#tblCartList {width: 90%;
	                   border: solid gray 0px;
	                   margin-top: 20px;
	                   margin-left: 10px;
	                   margin-bottom: 20px;}
	                   
	table#tblCartList th {border-top: solid gray 1px;
						  border-bottom: solid gray 1px;}
	table#tblCartList tr {border-top: dotted gray 1px;
						  border-bottom: dotted gray 1px;} 
	
	.delcss {background-color: cyan;
	         font-weight: bold;
	         color: red;}
	  	   
	.ordershoppingcss {background-color: cyan;
	  	         font-weight: bold;
	  	         color: blue;}
	td {
		text-align: center;
	} 	   
</style>

<script type="text/javascript">

	$(document).ready(function(){
		
		$(".spinner").spinner( {
		   spin: function(event, ui) {	  
			   if(ui.value > 100) {
				   $(this).spinner("value", 100);
				   return false;
			   }
			   else if(ui.value < 0) {
				   $(this).spinner("value", 0);
				   return false;
			   }
		   }
			   
		});// end of $("#spinner").spinner({});
		  
/*		  
		$(".del").hover(function(){
			$(this).addClass("delcss");
		}, function(){
			$(this).removeClass("delcss");
		});
*/	
		$(".ordershopping").hover(function(){
			$(this).addClass("ordershoppingcss");
		}, function(){
			$(this).removeClass("ordershoppingcss");
		});
/*
	var sumtotalprice = 0;
	var sumtotalpoint = 0;
	$(".checkedPnum").on("click", function(){
		$(".checkedPnum").each( function(){
			if($(this).is(':checked')){
				// 선택자의 부모(td)의 부모(tr)태그에서 id값을 찾아 해당 태그의 text만 가져옴
				var totalprice = $(this).parent().parent().find("#totalprice").text().trim();
				var totalpoint = $(this).parent().parent().find("#totalpoint").text().trim();
				
				// 가져온 string값을 ',' 구분자로 쪼개 배열에 담음	
					var strArr = totalprice.split(',');
					var strArr2 = totalpoint.split(',');
				for(var i=0; i<strArr.length; i++){
					alert(strArr[i]);
				}
				
				// 배열에 담긴 값을 붙임
				var str = strArr.join('');
				var str2 = strArr2.join('');
		
				// 주문 체크한 물품의 수량*판매가 값을 총액 변수에 누적
				sumtotalprice += parseInt(str);
				sumtotalpoint += parseInt(str2);
			}
			else{
				
			}
			
			document.getElementById("sumtotalprice").innerHTML=sumtotalprice;
			document.getElementById("sumtotalpoint").innerHTML=sumtotalpoint;
		});
//		alert("주문총액: "+sumtotalprice);
//		alert("주문총포인트: "+sumtotalpoint);
	}); */
	


	}); // end of $(document).ready()
	
//	#수량변경	
	function goOqtyEdit(oqtyID, cartnoID) {
		var cartno = $("#"+cartnoID).val();
		var oqty = $("#"+oqtyID).val();
		
//		alert("장바구니 번호: "+cartno +", 주문량: "+ oqty);
		var regExp= /^[0-9]+$/g;
		var bool = regExp.test(oqty);
		if(!bool){	// 숫자가 아닌 경우
			alert("물품 수량을 0개 이상 입력해주세요.");
			location.href="cartList.do";
			return;
		}
		else{
			var frm = document.updateOqtyFrm;
			frm.cartno.value=cartno;
			frm.oqty.value=oqty;
			frm.method="POST";
			frm.action="cartEdit.do";
			frm.submit();
		}		
	}// end of goOqtyEdit(oqtyID, cartnoID)
	
	
//	#장바구니 물품 삭제	
	function goDel(cartno) {
		var frm = document.deleteFrm;
		frm.cartno.value=cartno;
		frm.method="POST";
		frm.action="cartEdit.do";
		frm.submit();
		
	}// end of goDel(cartno)
	
	
//	#체크박스 모두선택 및 모두해제 되기 위한 함수
	function allCheckBox() {
		var bool = $("#allCheckOrNone").is(':checked');
		// >> 체크되었을 때 true, 체크 해제시 false
		$(".checkedPnum").prop('checked', bool)	
	}// end of allCheckBox()
	
	
	
//  #주문하기
	function goOrder() {
//		#체크박스들을 모두 검사해서 체크된 박스가 있는지 확인 --> 있는 경우에 flag값에 true를 반환
		var bool = false;
		$(".checkedPnum").each( function(){
			 if($(this).is(':checked')){
				 bool = true;
				 return false;
			 }
		});
		if(!bool){
			alert("주문하실 물품을 1개 이상 선택해주세요.");
			return;
		}
		else{
//			1) 주문확인 컨펌
			var yn = confirm("선택한 제품을 주문하시겠습니까?");
			if(!yn){
				return;
			}
			else{
//			2) 체크된 물품들의 주문 총액, 총 포인트 가져오기
				var sumtotalprice = 0;
				var sumtotalpoint = 0;
				$(".checkedPnum").each( function(){
					if($(this).is(':checked')){
						// 선택자의 부모(td)의 부모(tr)태그에서 id값을 찾아 해당 태그의 text만 가져옴
						var totalprice = $(this).parent().parent().find("#totalprice").text().trim();
						var totalpoint = $(this).parent().parent().find("#totalpoint").text().trim();
						
						// 가져온 string값을 ',' 구분자로 쪼개 배열에 담음	
 						var strArr = totalprice.split(',');
 						var strArr2 = totalpoint.split(',');
					/*	for(var i=0; i<strArr.length; i++){
							alert(strArr[i]);
						}  */
						
						// 배열에 담긴 값을 붙임
						var str = strArr.join('');
						var str2 = strArr2.join('');

						// 주문 체크한 물품의 수량*판매가 값을 총액 변수에 누적
						sumtotalprice += parseInt(str);
						sumtotalpoint += parseInt(str2);
					}
				});
//				alert("주문총액: "+sumtotalprice);
//				alert("주문총포인트: "+sumtotalpoint);

//			3) 재고수량과 주문량 비교
				var index = 0;
				var flag = false;
				var pname = "";
			   $(".checkedPnum").each(function(){
				   if($(this).is(':checked')) {
					   var oQty = parseInt($("#oqty"+index).val());
					   var pQty = parseInt($("#pqty"+index).val());
					   pname = $("#pname"+index).text();
					   
					   if(oQty > pQty) {
						   flag = true;
						   return;
					   } 
				   }
				   index++;
			   });
			   if(flag){
				   alert(pname + " 제품의 재고량이 주문량 보다 적어서 주문이 불가합니다.");
				   return;
			   }
//			4) 체크된 행만 활성화, 나머지 비활성화
//			--> 체크된 개체의 value만 action 파라미터로 넘어가도록 함
				index = 0;
				$(".checkedPnum").each(function(){
					if(!$(this).is(':checked')) {	// 체크되지 않은 경우
						/* $(this).attr("disabled", true);
				   		$("#saleprice"+index).attr("disabled", true);
				   		$("#oqty"+index).attr("disabled", true);
				   		$("#cartno"+index).attr("disabled", true); */
				   		$(this).parent().parent().find(":input").attr("disabled", true);
				   		
					}
					index++;
				});


//			5) 코인잔액과 주문총액을 비교
				var coin = ${sessionScope.loginuser.coin};
				if(sumtotalprice>coin){	// 코인잔액보다 주문총액이 많은 경우
					var coinYN = confirm("코인 잔액이 부족합니다. 코인을 충전 하시겠습니까?");
					if(!coinYN){
						return;
					}
					else{
						var url = "coinPurchaseTypeChoice.do?idx="+${(sessionScope.loginuser).idx};
						window.open(url, "coinPurchaseTypeChoice", "left=350px, top=100px, width=650px, height=570px");
					}
				}
				else{	// 코인잔액이 충분한 경우
					var frm = document.orderFrm;
					frm.sumtotalprice.value = sumtotalprice;
					frm.sumtotalpoint.value = sumtotalpoint;
					
					frm.method="POST";
					frm.action="orderAdd.do";
					frm.submit();
				}
			}
		}
	
	}// end of goOrder()

</script>


<h2 style="font-weight: bold;">::: ${(sessionScope.loginuser).name} 님[ ${(sessionScope.loginuser).userid} ] 장바구니 목록 :::</h2>	

<%-- 장바구니에 담긴 제품목록을 보여주고서 실제 주문을 하도록 form 생성한다. --%>    
<form name="orderFrm">
<table id="tblCartList" >
	<thead>
		<tr style="border-style: hidden;">
			<th style="border-right-style: none;">
				<input type="checkbox" id="allCheckOrNone" onClick="allCheckBox();" />
				<span style="font-size: 10pt;"><label for="allCheckOrNone">전체선택</label></span>
			</th>
			<th colspan="5" style="border-left-style: none; font-size: 12pt; padding-left: 27%;">
				주문하실 제품을 선택하신후 주문하기를 클릭하세요
			</th>
		</tr>
 
		<tr style="background-color: #cfcfcf;">
			<th style="width:10%; text-align: center; height: 30px;">제품번호</th>
			<th style="width:23%; text-align: center;">제품명</th>
			<th style="width:17%; text-align: center;">수량</th>
			<th style="width:20%; text-align: center;">판매가/포인트(개당)</th>
			<th style="width:20%; text-align: center;">총액</th>
			<th style="width:10%; text-align: center;">삭제</th>
		</tr>	
	</thead>

	<tbody>
	<c:if test="${cartList == null || empty cartList}">
		<tr>
			<td colspan="6" align="center">
			<span style="color: red; font-weight: bold;">
			장바구니에 담긴 상품이 없습니다.
			</span>
			</td>	
		</tr>
	</c:if>	

	<c:if test="${cartList != null && not empty cartList}">
	<%-- 장바구니 전체 총 금액, 총 포인트(누적) 변수 선언 및 초기치 선언 --%>
	<c:set var="cartTotalPrice" value="0" />
	<c:set var="cartTotalPoint" value="0" />
	<c:forEach var="cartvo" items="${cartList}" varStatus="status" >
		<%-- 장바구니 전체 총 금액, 총 포인트 변수에 누적시키기 --%>
		<c:set var="cartTotalPrice" value="${cartTotalPrice + cartvo.item.totalPrice}"/>
		<c:set var="cartTotalPoint" value="${cartTotalPoint + cartvo.item.totalPoint}"/>
		<tr>
			<td>	<%-- 체크박스 및 제품번호 --%>
				<input type="checkbox" class="checkedPnum" name="pnum" id="checkedPnum${status.index}" value="${cartvo.fk_pnum}" />&nbsp;
				<label for="checkedPnum${status.index}">${cartvo.fk_pnum}</label>
			</td>
			<td>
				<img src="images/${cartvo.item.pimage1}" style="width: 130px; height: 100px; cursor: pointer;" onClick="javascript:location.href='<%= request.getContextPath() %>/prodView.do?pnum=${cartvo.fk_pnum}'"/> &nbsp;
				<img src="images/${cartvo.item.pimage2}" style="width: 130px; height: 100px; cursor: pointer;" onClick="javascript:location.href='<%= request.getContextPath() %>/prodView.do?pnum=${cartvo.fk_pnum}'"/>
				<br/>
				<span id="pname${status.index}" style="font-weight: bold; cursor: pointer;" onClick="javascript:location.href='<%= request.getContextPath() %>/prodView.do?pnum=${cartvo.fk_pnum}'">${cartvo.item.pname}</span>
			</td>
			<td>
				<%-- 수량 ;status.index를 이용해 id값을 고유값으로 설정 --%>
				<input class="spinner" name="oqty" id="oqty${status.index}" value="${cartvo.oqty}" style="width: 30px; height: 20px;">
				<input type="hidden" id="pqty${status.index}" value="${cartvo.item.pqty}"/>
				
				<%-- 장바구니번호(cartno) --%>
				<input type="hidden" name="cartno" id="cartno${status.index}" value="${cartvo.cartno}" />
				<button type="button" class="btn" onClick="goOqtyEdit('oqty${status.index}', 'cartno${status.index}');">수정</button>
			</td>
			
			<td>
				<%-- 실제 판매 단가 및 포인트 --%>
				<span style="font-weight: bold; color: #FF8F00;"><fmt:formatNumber pattern="###,###" value="${cartvo.item.saleprice}"/></span>원
				<input type="hidden" name="saleprice" id="saleprice${status.index}" value="${cartvo.item.saleprice}"/>
				<br/>
				<span style="font-weight: bold;"><fmt:formatNumber pattern="###,###" value="${cartvo.item.point}"/></span>point
				<input type="hidden" name="point" value="${cartvo.item.point}"/>
			</td>
			<td>
				<%-- 총 판매 단가 및 총 포인트 --%>
				<span id="totalprice" style="font-weight: bold; color: #FF8F00;"><fmt:formatNumber pattern="###,###">${cartvo.item.totalPrice}</fmt:formatNumber></span>원
				<input type="hidden" name="totalprice" value="${cartvo.item.totalPrice}"/>
				<br/>
				<span id="totalpoint" style="font-weight: bold;"><fmt:formatNumber pattern="###,###">${cartvo.item.totalPoint}</fmt:formatNumber></span>point
				<input type="hidden" name="totalpoint" value="${cartvo.item.totalPoint}"/>
			</td>
			<td>
				<button type="button" class="btn btn-warning del" onClick="goDel('${cartvo.cartno}');">삭제</button>
			</td>
		</tr>
	</c:forEach>  
	
	</c:if>	

		<tr style="border-top: solid gray 1px; border-bottom: hidden; padding-bottom: 10%;">
			<td colspan="6" align="right" style="text-align: right; padding-right: 3%;">
				<ul style="list-style-type: none;">
					<li>
					<span style="font-weight: bold;">장바구니 총액 : </span>
					<span style="font-weight: bold; color: #FF8F00;"><fmt:formatNumber value="${cartTotalPrice}" pattern="###,###"/></span>원
					</li>
					<li>
					<span style="font-weight: bold;">총 포인트 : </span>
					<span style="font-weight: bold; color: #FF8F00;"><fmt:formatNumber value="${cartTotalPoint}" pattern="###,###"/></span>point
					</li>
				</ul>
			</td>
		</tr>
		
		<tr style="border-bottom: hidden;">
			<td colspan="6" align="right" style="text-align: right; padding-right: 3%;">
			<button type="button" class="ordershopping btn btn-warning" style="cursor: pointer;" onClick="goDel();">선택삭제</button>&nbsp;&nbsp;
			<button type="button" class="ordershopping btn btn-primary" style="cursor: pointer;" onClick="goOrder();">주문하기</button>&nbsp;&nbsp;
			<button type="button" class="ordershopping btn" style="cursor: pointer;" onClick="javascript:location.href='<%= request.getContextPath() %>/mallHome.do'">계속쇼핑</button>
			</td>
		</tr>
	</tbody>
</table> 
</form>          



<%-- 장바구니에 담긴 제품수량을 수정하는 form --%>
<form name="updateOqtyFrm">
	<input type="hidden" name="cartno" />
	<input type="hidden" name="oqty" />
</form>

<%-- 장바구니에 담긴 제품을 삭제하는 form --%>
<form name="deleteFrm">
	<input type="hidden" name="cartno" />
	<input type="hidden" name="oqty" value="0"/>
</form>
 
   
<jsp:include page="../footer.jsp" />
