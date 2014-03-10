<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<!-- Credit Pankaj Kumar
http://www.journaldev.com/1907/java-servlet-session-management-tutorial-with-examples-of-cookies-httpsession-and-url-rewriting -->
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Logout Page</title>
</head>
<body>
	<%
	  //allow access only if session exists
	  if (session.getAttribute("user") == null) {
	    response.sendRedirect("login.html");
	  }
	  String userName = null;
	  String sessionID = null;
	  Cookie[] cookies = request.getCookies();
	  if (cookies != null) {
	    for (Cookie cookie : cookies) {
	      if (cookie.getName().equals("user"))
	        userName = cookie.getValue();
	    }
	  }
	%>
	<h3>
		Hi
		<%=userName%>, do the checkout.
	</h3>
	<br>
	<form action="/BankApplication1/logout" method="post">
		<input type="submit" value="Logout">
	</form>
</body>
</html>