<?xml version="1.0" encoding="UTF-8" ?>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<books>
	<c:forEach var="bookmap" items="${bookList}">
		<book>
			<subject>${bookmap.subject}</subject>
			<booktitle>${bookmap.title}</booktitle>
			<author>${bookmap.author}</author>
			<registerday>${bookmap.registerday}</registerday>
		</book>
	</c:forEach>

</books>