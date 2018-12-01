<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="../header.jsp"/>
<style>
   table#tblMemberRegister {
   	    width: 93%;
   	    
   	    /* 선을 숨기는 것 */
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
   		/* border: solid 1px gray;  */
   		line-height: 30px;
   		padding-top: 8px;
   		padding-bottom: 8px;
   }
   
   .star { color: red;
           font-weight: bold;
           font-size: 13pt;
   }
</style> 

<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>

<script type ="text/javascript">
	$(document).ready(function(){
		var dt = new Date();
		var year = dt.getFullYear();
		
		// 에러메세지 감추기
		$(".error").hide();
		$("#error_passwd").hide();
		
		// 첫번째 입력사항(성명)에 포커스
		$("#name").focus();
		
// 		#포커스가 넘어갔을 때의 태스크를 반복문으로 처리; 필수입력사항의 공통 클래스 requiredInfo
		$(".requiredInfo").each(function(){
			$(this).blur(function(){
				var data = $(this).val().trim();
				if(data==""){ // 입력하지 않거나 공백만 입력했을 경우 
					$(this).parent().find(".error").show();
					$(":input").attr("disabled", true).addClass("bgcol");
					$(this).attr("disabled", false).removeClass("bgcol");
					$(this).focus();
				}
				else {	// 공백이 아닌 글자를 입력했을 경우
					$(this).parent().find(".error").hide(); // 에러 없앰
					$(":input").attr("disabled", false).removeClass("bgcol");
					$(this).next().focus();
				}
			});
		});

//		#아이디 중복검사 
		$("#userid").bind("keyup", function(){
			alert("아이디 중복확인 해주세요.");
			$(this).val("");
			$(this).focus();
		});
		
//		#아이디 중복검사; 팝업창 띄우기
		$("#idcheck").click(function(){
			var url = "idDuplicateCheck.do";
			window.open(url, "idcheck", "left=500px, top=100px, width=300px, height=230px");
		});

//		#패스워드 유효성 검사
		$("#pwd").blur(function(){
			var passwd = $(this).val();
			var regExp_pw = /^.*(?=^.{8,15}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[^a-zA-Z0-9]).*$/g;
			var isUsePasswd = regExp_pw.test(passwd);
			if(!isUsePasswd){
				$("#error_passwd").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol");
				$(this).val("");
				$("#pwd").focus();
			}
			else{
				$("#error_passwd").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
				$("#pwdcheck").focus();
			}
		});
		
		$("#pwdcheck").blur(function(){
			var passwd = $("#pwd").val();
			var passwdck = $(this).val();
			
			if(passwd != passwdck){ // 암호가 일치하지 않을 때
				$(this).parent().find(".error").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol");
				$("#pwd").attr("disabled", false).removeClass("bgcol");
				$(this).val("");
				$("#pwdcheck").focus();
			}
			else{
				$("#error_passwd").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
			}	
		});
		
		$("#email").blur(function(){
			var email = $(this).val();
			var regExp_EMAIL = /^[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_\.]?[0-9a-zA-Z])*\.[a-zA-Z]{2,3}$/i; 
			var isUseEmail = regExp_EMAIL.test(email);
			if(!isUseEmail){
				$(this).parent().find(".error").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol");
				$(this).val("");
				$(this).focus();
			}
			else{
				$("#error_passwd").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
			}	
		});
		
		$("#hp2").blur(function(){
			var hp2 = $(this).val();
			var isUseHp2a = false;
			var regExp_HP2a = /[2-9][0-9][0-9]/g;
			isUseHp2a = (hp2.length == 3) && regExp_HP2a.test(hp2);

			var isUseHp2b = false;
			var regExp_HP2b = /[2-9][0-9][0-9][0-9]/g;
			isUseHp2b = (hp2.length == 4) && regExp_HP2b.test(hp2);
			
			if(!isUseHp2a && !isUseHp2b) {
				$(this).parent().find(".error").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol");
				$(this).val("");
				$(this).focus();
			}
			else{
				$(".error_hp").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
			}	
		});
		
		$("#hp3").blur(function(){
			var hp3 = $(this).val();
			var regExp_HP3 = /\d{4}/g;
			var isUseHp3 = regExp_HP3.test(hp3);
			if(!isUseHp3) {
				$(this).parent().find(".error").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol");
				$(this).val("");
				$(this).focus();
			}
			else{
				$(".error_hp").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
			}
		});
		
		$("#zipcodeSearch").click(function() {
			new daum.Postcode({
				oncomplete: function(data) {
					if(data.postconde1 == ""){
						$("#post1").val(data.zonecode);
					}
					else {
						$("#post1").val(data.postcode1);
						$("#post2").val(data.postcode2);
					}
				    $("#addr1").val(data.address);
				    $("#addr2").focus();
				}
			}).open();
		});
		
		$(".address").blur(function(){
			var address = $(this).val().trim();
			if(address==""){
				$(this).parent().find(".error").show();
				$(":input").attr("disabled", true).addClass("bgcol");
				$(this).attr("disabled", false).removeClass("bgcol");
				$(this).val("");
			}
			else{
				$(this).parent().find(".error").hide();
				$(":input").attr("disabled", false).removeClass("bgcol");
			}
		});
		
	  $("#birthday").datepicker({
		dateFormat:"yy/mm/dd",
		dayNamesMin:["일", "월", "화", "수", "목", "금", "토"],
		monthNames:["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
		onSelect:function( d ){
 			var arr = d.split("/");
			$("#birthday").text(arr[0]);
			$("#birthday").append(arr[1]);
			$("#birthday").append(arr[2]);
		}
		});

	});
	
//	#최종적으로 체크박스/라디오 체크된 다음 submit
	function goRegister(){
		var isCheckedGender = $("input:radio[name=gender]").is(":checked");
		if(!isCheckedGender){
			alert("성별을 선택하세요.");
			return;
		}
		var isCheckedAgree = $("input:checkbox[id=agree]").is(":checked");
		if(!isCheckedAgree){
			alert("이용약관에 동의하셔야 가입 가능합니다.");
			return;
		}
		var frm = document.registerFrm;
		frm.method = "POST";
		frm.action = "memberRegisterEnd.do";
		frm.submit();
	}
</script>


<div class="row">
	<div class="col-md-12" align="center">
	<form name="registerFrm">
	
	<table id="tblMemberRegister">
		<thead>
		<tr>
			<th colspan="2" id="th">::: 회원가입 (<span style="font-size: 10pt; font-style: italic;"><span class="star">*</span>표시는 필수입력사항</span>) :::</th>
		</tr>
		</thead>
		<tbody>
		<tr>
			<td style="width: 20%; font-weight: bold;">성명&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;">
			    <input type="text" name="name" id="name" class="requiredInfo" required /> 
				<span class="error">성명은 필수입력 사항입니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">아이디&nbsp;<span class="star">*</span></td>
			<td style="width: 80%; text-align: left;">
			    <input type="text" name="userid" id="userid" class="requiredInfo" required />&nbsp;&nbsp;
			    <!-- 아이디중복체크 -->
			    <img id="idcheck" src="/MyMVC/images/b_id_check.gif" style="vertical-align: middle;" />
			    <span class="error">아이디는 필수입력 사항입니다.</span>
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
			<td style="width: 80%; text-align: left;"><input type="text" name="email" id="email" class="requiredInfo" placeholder="abc@def.com" /> 
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
			    <input type="text" name="hp2" id="hp2" size="6" maxlength="4" />&nbsp;-&nbsp;
			    <input type="text" name="hp3" id="hp3" size="6" maxlength="4" />
			    <span class="error error_hp">휴대폰 형식이 아닙니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">우편번호</td>
			<td style="width: 80%; text-align: left;">
			   <input type="text" id="post1" name="post1" size="6" maxlength="5" />
			   &nbsp;-&nbsp;
			   <input type="text" id="post2" name="post2" size="6" maxlength="3" />&nbsp;&nbsp;
			   <img id="zipcodeSearch" src="/MyMVC/images/b_zipcode.gif" style="vertical-align: middle;" />
			   <span class="error error_post">우편번호 형식이 아닙니다.</span>
			</td>
		</tr>
		<tr>
			<td style="width: 20%; font-weight: bold;">주소</td>
			<td style="width: 80%; text-align: left;">
			   <input type="text" id="addr1" class="address" name="addr1" size="60" maxlength="100" /><br style="line-height: 200%"/>
			   <input type="text" id="addr2" class="address" name="addr2" size="60" maxlength="100" />
			   <span class="error error_addr">상세 주소를 입력하세요.</span>
			</td>
		</tr>
		
		<tr>
			<td style="width: 20%; font-weight: bold;">성별</td>
			<td style="width: 80%; text-align: left;">
			   <input type="radio" id="male" name="gender" value="1" /><label for="male" style="margin-left: 2%;">남자</label>
			   <input type="radio" id="female" name="gender" value="2" style="margin-left: 10%;" /><label for="female" style="margin-left: 2%;">여자</label>
			</td>
		</tr>
		
		<tr>
			<td style="width: 20%; font-weight: bold;">생년월일</td>
			<td style="width: 80%; text-align: left;">
				<input type="text" id="birthday" name="birthday" required/>		
			</td>
		</tr>
		
		<tr>
			<td colspan="2">
				<label for="agree">이용약관에 동의합니다</label>&nbsp;&nbsp;<input type="checkbox" id="agree" />
			</td>
		</tr>
		<tr>
			<td colspan="2" style="text-align: center; vertical-align: middle;">
				<iframe src="/MyMVC/agree.html" width="85%" height="150px" class="box" ></iframe>
			</td>
		</tr>
		<tr>
			<td colspan="2" style="line-height: 90px;">
				<button type="button" id="btnRegister" style="background-image:url('/MyMVC/images/join.png'); border:none; width: 135px; height: 34px; margin-left: 30%;" onClick="goRegister();"></button> 
			</td>
		</tr>
		</tbody>
	</table>
	</form>
	</div>
</div>
	
<jsp:include page="../footer.jsp"/>