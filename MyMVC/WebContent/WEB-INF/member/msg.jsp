<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<script type="text/javascript">
	// EL을 이용하여 memberDelete에서 dispatcher로 보낸 값을 불러옴 
	alert("${msg}");
	
	location.href="${loc}";

</script>