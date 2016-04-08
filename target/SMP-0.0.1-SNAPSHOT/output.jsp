<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Insert title here</title>
</head>
<body>
	<h1>Your first and last name is:</h1>

	<% 
		String firstName = (String) request.getAttribute("firstname");
		String lastName = (String) request.getAttribute("lastname");

	%>

	<table border="0">
		<tr>
			<td>First name:</td><td><%= firstName %></td>
		</tr>
		<tr>
			<td>Last name:</td><td><%= lastName %></td>
		</tr>
	</table>
</body>
</html>