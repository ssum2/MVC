<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
<!DOCTYPE html>
<html>
<head>
<title>:::회원정보수정:::</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">

<link rel="stylesheet" type="text/css" href="/MyWeb/bootstrap-3.3.7-dist/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/MyWeb/css/style.css" />
<script type="text/javascript" src="/MyWeb/js/jquery-3.3.1.min.js"></script>
<script type="text/javascript" src="/MyWeb/bootstrap-3.3.7-dist/js/bootstrap.min.js"></script>

<style>
   table#tblMemberRegister {
   	    width: 93%;
   	    border: hidden;
   	    margin: 10px;
   }  
   
   table#tblMemberRegister #th {
   		height: 40px;
   		text-align: center;
   		background-color: silver;
   		font-size: 14pt;
   }
   
   table#tblMemberRegister td {
   		line-height: 30px;
   		padding-top: 8px;
   		padding-bottom: 8px;
   }
   
   #error_passwd { color: red; padding-left: 10px; margin-bottom: 5px;}
   
   .star { color: red;
           font-weight: bold;
           font-size: 13pt;
   }
</style> 

<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<script type="text/javascript">
	
	$(document).ready(function(){
		
		$(".error").hide();
		$("#error_passwd").hide();
		$(".requiredInfo").each(function(){
			
			$(this).blur(function(){
				var data = $(this).val().trim();
				if(data.length == 0) {	
					$(this).parent().find(".error").show();
					$(":input").attr("disabled",true).addClass("bgcol");
					$("#btnUpdate").attr("disabled",true); 
					$(this).attr("disabled",false).removeClass("bgcol");
				}
				else{
					$(this).parent().find(".error").hide();
					$(":input").attr("disabled",false).removeClass("bgcol");
					$("#btnUpdate").attr("disabled",false); 
				}
			});
			
		});// end of $(".requiredInfo").each()

		
		$("#pwd").blur(function() {
			var passwd = $(this).val();
			
			var regexp_passwd = new RegExp(/^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g); 
			
			var bool = regexp_passwd.test(passwd);

			if(!bool) {
				$("#error_passwd").show();
				$(":input").attr("disabled",true).addClass("bgcol");
				$("#btnUpdate").attr("disabled",true); 
				$(this).attr("disabled",false).removeClass("bgcol");
			}   
			else {
				$("#error_passwd").hide();
				$(":input").attr("disabled",false).removeClass("bgcol");
				$("#btnUpdate").attr("disabled",false); 
				$("#pwdcheck").focus();
			}
			
		});// end of $("#pwd").blur()
		
		
		$("#pwdcheck").blur(function(){
			var passwd = $("#pwd").val();
			var passwdCheck = $(this).val();
			
			if(passwd != passwdCheck) {
				$(this).parent().find(".error").show();
				$(":input").attr("disabled",true).addClass("bgcol");
				$("#btnUpdate").attr("disabled",true);
				
				$(this).attr("disabled",false).removeClass("bgcol");
			}
			else {
				$(this).parent().find(".error").hide();
				$(":input").attr("disabled",false).removeClass("bgcol");
				$("#btnUpdate").attr("disabled",false);
			}
			
		});// end of $("#pwdcheck").blur()		
		
		$("#hp2").blur(function(){
			
			var hp2 = $(this).val().trim();
			
			if(hp2 != "") {
				
				var regexp_hp2 = /\d{3,4}/g; 
				
				var bool = regexp_hp2.test(hp2);
				
				if(!bool) {
					$(this).parent().find(".error").show();
					$(":input").attr("disabled",true).addClass("bgcol");
					$("#btnUpdate").attr("disabled",true);
					
					$(this).attr("disabled",false).removeClass("bgcol");
				}
				else {
					$(this).parent().find(".error").hide();
					$(":input").attr("disabled",false).removeClass("bgcol");
					$("#btnUpdate").attr("disabled",false);
				}
				
			}
			else {
				$(this).parent().find(".error").show();
				$(":input").attr("disabled",true).addClass("bgcol");
				$("#btnUpdate").attr("disabled",true);
				
				$(this).attr("disabled",false).removeClass("bgcol");
			}
			
		});// end of $("#hp2").blur()
		
		
		$("#hp3").blur(function(){
			var hp3 = $(this).val().trim();
			
			if(hp3 != "") {
				
				var regexp_hp3 = /\d{4}/g; 	
				
				var bool = regexp_hp3.test(hp3);
				
				if(!bool) {
					$(this).parent().find(".error").show();
					$(":input").attr("disabled",true).addClass("bgcol");
					$("#btnUpdate").attr("disabled",true);
					
					$(this).attr("disabled",false).removeClass("bgcol");
				}
				else {
					$(this).parent().find(".error").hide();
					$(":input").attr("disabled",false).removeClass("bgcol");
					$("#btnUpdate").attr("disabled",false);
				}
				
			}
			else {
				$(this).parent().find(".error").show();
				$(":input").attr("disabled",true).addClass("bgcol");
				$("#btnUpdate").attr("disabled",true);
				
				$(this).attr("disabled",false).removeClass("bgcol");
			}
			
		});// end of $("#hp3").blur()
		
		$("#email").blur(function() {
			var email = $(this).val();
			
			var regexp_email = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
	         
			var bool = regexp_email.test(email);
	         
	        if(!bool) {
	        	$(this).parent().find(".error").show();
				$(":input").attr("disabled",true).addClass("bgcol");
				$("#btnUpdate").attr("disabled",true);
				
				$(this).attr("disabled",false).removeClass("bgcol");
	        } 
	        else {
	        	$(this).parent().find(".error").hide();
				$(":input").attr("disabled",false).removeClass("bgcol");
				$("#btnUpdate").attr("disabled",false);
	        }	
		});	// end of $("#email").blur()

	    
		$("#zipcodeSearch").click(function() {
			new daum.Postcode({
				oncomplete: function(data) {
					if(data.postcode1 == ""){
						$("#post1").val(data.zonecode);
						$("#post2").val("");
					}
					else {
						$("#post1").val(data.postcode1);  // 우편번호의 첫번째 값     예> 151
						$("#post2").val(data.postcode2);  // 우편번호의 두번째 값     예> 019
					}
				    $("#addr1").val(data.address);   // 큰주소                        예> 서울특별시 종로구 인사로 17 
				    $("#addr2").focus();			// 직접 입력 항목
				}
			}).open();
		});
	    
	    $("#hp1").val("${membervo.hp1}");
		
	});// end of $(document).ready()
	
	
	function updateInfo(event) {
		var flagBool = false;
		$(".requiredInfo").each(function(){
			var data = $(this).val().trim();
			if(data == "") {
				flagBool = true;
				return false;
			}
		});
		
		if(flagBool) {
			alert("필수입력란은 모두 입력하셔야 합니다.");
			event.preventDefault();
			return;
		}
		else {
			var bool = confirm('회원정보를 수정하시겠습니까?');
			if(bool){
				var frm = document.editFrm;
				frm.method = "post";
				frm.action = "memberEditEnd.do";
				frm.submit(); 
			}
		}
	}// end of goEdit(event)
	
</script>
</head>

<body>
<div class="row">
	<div class="col-md-12" align="center">
		<form name="editFrm">
		
		<table id="tblMemberRegister">
			<thead>
			<tr>
				<th colspan="2" id="th">::: 회원수정 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>) :::</th>
			</tr>
			</thead>
			<tbody>
			<tr>
				<td style="width: 20%; font-weight: bold;">성명&nbsp;<span class="star">*</span></td>
				<td style="width: 80%; text-align: left;">
				    <input type="hidden" name="idx" value="${membervo.idx}" readonly />
				    <input type="text" name="name" id="name" value="${membervo.name}" class="requiredInfo" required /> 
					<span class="error">성명은 필수입력 사항입니다.</span>
				</td>
			</tr>
			<tr>
				<td style="width: 20%; font-weight: bold;">비밀번호&nbsp;<span class="star">*</span></td>
				<td style="width: 80%; text-align: left;"><input type="password" name="pwd" id="pwd" class="requiredInfo" required />
					<span id="error_passwd">암호는 영문자,숫자,특수기호가 혼합된 8~15 글자로만 입력가능합니다.</span>
				</td>
			</tr>
			<tr>
				<td style="width: 20%; font-weight: bold;">비밀번호확인&nbsp;<span class="star">*</span></td>
				<td style="width: 80%; text-align: left;"><input type="password" id="pwdcheck" class="requiredInfo" required /> 
					<span class="error">암호가 일치하지 않습니다.</span>
				</td>
			</tr>
			<tr>
				<td style="width: 20%; font-weight: bold;">이메일&nbsp;<span class="star">*</span></td>
				<td style="width: 80%; text-align: left;"><input type="text" name="email" id="email" value="${membervo.email}" class="requiredInfo" placeholder="abc@def.com" /> 
				    <span class="error">이메일 형식에 맞지 않습니다.</span>
				</td>
			</tr>
			<tr>
				<td style="width: 20%; font-weight: bold;">연락처</td>
				<td style="width: 80%; text-align: left;">
				   <select name="hp1" id="hp1" style="width: 75px; padding: 8px;">
						<option value="010" selected>010</option>
						<option value="011">011</option>
						<option value="016">016</option>
						<option value="017">017</option>
						<option value="018">018</option>
						<option value="019">019</option>
					</select>&nbsp;-&nbsp;
				    <input type="text" name="hp2" id="hp2" value="${membervo.hp2}" size="6" maxlength="4" />&nbsp;-&nbsp;
				    <input type="text" name="hp3" id="hp3" value="${membervo.hp3}" size="6" maxlength="4" />
				    <span class="error error_hp">휴대폰 형식이 아닙니다.</span>
				</td>
			</tr>
			<tr>
				<td style="width: 20%; font-weight: bold;">우편번호</td>
				<td style="width: 80%; text-align: left;">
				   <input type="text" name="post1" value="${membervo.post1}" id="post1" size="6" maxlength="5" />
				   &nbsp;-&nbsp;
				   <input type="text" name="post2" value="${membervo.post2}" id="post2" size="6" maxlength="3" />&nbsp;&nbsp;
				   <!-- 우편번호 찾기 -->
				   <img id="zipcodeSearch" src="/MyMVC/images/b_zipcode.gif" style="vertical-align: middle;" />
				   <span class="error error_post">우편번호 형식이 아닙니다.</span>
				</td>
			</tr>
			<tr>
				<td style="width: 20%; font-weight: bold;">주소</td>
				<td style="width: 80%; text-align: left;">
				   <input type="text" id="addr1" name="addr1" value="${membervo.addr1}" size="60" maxlength="100" /><br style="line-height: 200%"/>
				   <input type="text" id="addr2" name="addr2" value="${membervo.addr2}" size="60" maxlength="100" />
				</td>
			</tr>
			<tr>
				<td colspan="2">
					<button type="button" id="btnUpdate" style="margin-left: 40%; margin-top: 5%; width: 80px; height: 40px;" onClick="updateInfo(event);"><span style="font-size: 15pt;">확인</span></button>
				</td>
			</tr>
			</tbody>
		</table>
		
		</form>
	</div>
</div>
</body>
</html>
