<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<jsp:include page="../../header.jsp" />


<style>
	table#tblProdInput {border-bottom: solid gray 1px; 
	                    border-collapse: collapse; }
	
	table#tblProdInput tr { border-bottom: solid gray 1px;
							border-top: solid gray 1px; 
							 }                   
    table#tblProdInput td {border: solid gray 0px; 
	                       padding-left: 10px;
	                       height: 50px;
	                        }
	                       
    .prodInputName {background-color: #FAFAFA; 
                    font-weight: bold; }	                       	                    
	
	.error {color: red; font-weight: bold; font-size: 9pt;}
	
	.prodInput {
		border: solid gray 1px; 
	    border-collapse: collapse;
	}
</style>


<script type="text/javascript">
	$(document).ready(function(){
		// 에러메시지 숨기기
		$(".error").hide();
		
		// 제품 수량 스피너
		$("#spinnerPqty").spinner({
			spin: function(event, ui){
				if(ui.value > 100){
					// Max값 100으로 한정
					$(this).spinner("value", 100); 
					return false;
				}
				else if(ui.value < 1){
					// Min값 1으로 한정
					$(this).spinner("value", 1); 
					return false;
				}
			}
		});
		
		// 파일 개수 스피너
		$("#spinnerImgQty").spinner({
			spin: function(event, ui){
				if(ui.value > 10){
					// Max값 100으로 한정
					$(this).spinner("value", 10); 
					return false;
				}
				else if(ui.value < 0){
					// Min값 1으로 한정
					$(this).spinner("value", 0); 
					return false;
				}
			}
		});
		
		// 파일 개수 스피너에서 개수를 선택했을 때 첨부한 파일 넣어주기
		$("#spinnerImgQty").bind("spinstop", function(){
			// 스피너는 이벤트가 "change"가 아니라 "spinstop"이다.
			var html ="";
			var spinnerImgQtyVal = $("#spinnerImgQty").val();
			
			if(spinnerImgQtyVal == "0"){
				$("#divfileattach").empty();
				$("#attachCount").val("");
				return;
			}
			else {
				for(var i=0; i<parseInt(spinnerImgQtyVal); i++){
					html += "<br/>";
					html += "<input type='file' name='attach"+i+"' class='btn btn-default' />";
				}
				$("#divfileattach").empty();
				$("#divfileattach").append(html);
				$("#attachCount").val(spinnerImgQtyVal);
			}

		});
		
		
		// 상품등록하기 버튼을 눌렀을 때
		$("#btnRegister").bind("click", function(){
			var flag = false;
			$(".infoData").each(function(){ // 클래스가 infoData인 것들을 각각 검사하여 값이 채워졌는지 확인
				var val = $(this).val();
				if(val == ""){
					$(this).next().show();	// 선택된 개체의 다음 개체를 보여줌 (에러메세지)
					flag = true;
					return false;
				}
			});
			
			if(flag){
				event.preventDefault(); // 이벤트를 가로막는다(아래 form에 기재되어 있는 action을 취하지 않음)
				return;
			}
			else {
				var frm = document.prodInputFrm;
				frm.submit();
			}
		});
		
	});
</script>

<div align="center" style="margin-bottom: 20px;">

<div style="border: solid navy 2px; width: 200px; margin-top: 20px; padding-top: 10px; padding-bottom: 10px; border-left: hidden; border-right: hidden;">       
	<span style="font-size: 15pt; font-weight: bold;">제품등록[ADMIN]</span>	
</div>
<br/>
<form name="prodInputFrm" action="<%= request.getContextPath() %>/admin/productRegisterEnd.do" method="POST" enctype="multipart/form-data" >
<%-- ※중요: 파일 업로드시 method는 POST, enctype="multipart/form-data"을 기재해야 form에 담긴 파일을 전송해줌 
	/admin/productRegisterEnd.do ==> 파일 업로드, DB에 insert; multipart	--%> 

      
<table id="tblProdInput" style="width: 80%;">
<tbody>
	<tr>
		<td width="25%" class="prodInputName" style="padding-top: 10px;">카테고리</td>
		<td width="75%" class="prodInput" align="left" style="padding-top: 10px;" >
			<select name="pcategory_fk" class="infoData">
				<option value="">:::선택하세요:::</option>
				<c:forEach var="cmap" items="${categoryList}">
				<option value="${cmap.code}">${cmap.cname}</option>
				</c:forEach>
				<!-- 
				<option value="100000">전자제품</option>
				<option value="200000">의  류</option>
				<option value="300000">도  서</option> 
				-->				
			</select>
			<span class="error">필수입력</span>
		</td>	
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품명</td>
		<td width="75%" class="prodInput" align="left" >
			<input type="text" style="width: 300px;" name="pname" class="box infoData" />
			<span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제조사</td>
		<td width="75%" class="prodInput" align="left">
			<input type="text" style="width: 300px;" name="pcompany" class="box infoData" />
			<span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품이미지</td>
		<td width="75%" class="prodInput" align="left">
			<input type="file" name="pimage1" class="infoData btn btn-default" /><span class="error">필수입력</span>
			<input type="file" name="pimage2" class="infoData btn btn-default" /><span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품수량</td>
		<td width="75%" class="prodInput" align="left">
           	<input id="spinnerPqty" name="pqty" value="1" style="width: 30px; height: 20px;"> 개
			<span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품정가</td>
		<td width="75%" class="prodInput" align="left">
			<input type="text" style="width: 100px;" name="price" class="box infoData" /> 원
			<span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품판매가</td>
		<td width="75%" class="prodInput" align="left">
			<input type="text" style="width: 100px;" name="saleprice" class="box infoData" /> 원
			<span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품스펙</td>
		<td width="75%" class="prodInput" align="left">
			<select name="pspec" class="infoData">
				<option value="">:::선택하세요:::</option>
				<c:forEach var="specmap" items="${specList}">
				<option value="${specmap.sname}">${specmap.sname}</option>
				</c:forEach>
			</select>
			<span class="error">필수입력</span>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName">제품설명</td>
		<td width="75%" class="prodInput" align="left">
			<textarea name="pcontent" rows="5" cols="60"></textarea> 
			<%-- textarea: input text와 달리 입력값을 많이 쓸 수 있음 
					rows="칸의 길이(행)", cols="칸 너비(열)"	--%>
		</td>
	</tr>
	<tr>
		<td width="25%" class="prodInputName" style="padding-bottom: 10px;">제품포인트</td>
		<td width="75%" class="prodInput" align="left" style="padding-bottom: 10px;">
			<input type="text" style="width: 100px;" name="point" class="box infoData" /> POINT
			<span class="error">필수입력</span>
		</td>
	</tr>
	
	<%-- ==== 첨부파일 타입 추가하기 ==== --%>
    <tr>
       	<td width="20%" class="prodInputName" style="padding-bottom: 10px;">추가이미지파일(선택)</td>
       	<td class="prodInput">
       		<label for="spinnerImgQty">파일갯수 : </label>
		    <input id="spinnerImgQty" value="0" style="width: 30px; height: 20px;">
       		<div id="divfileattach"></div>
       		<input type="hidden" name="attachCount" id="attachCount" /> 
       	</td>
    </tr>
	
	<tr style="height: 70px;">
		<td class="prodInput" colspan="2" align="center" style="border-left: hidden; border-bottom: hidden; border-right: hidden;">
		    <input type="button" value="제품등록" id="btnRegister" style="width: 80px;" /> 
		    &nbsp;
		    <input type="reset" value="취소"  style="width: 80px;" />	
		</td>
	</tr>
</tbody>
</table>
</form>
</div>

<jsp:include page="../../footer.jsp" />    