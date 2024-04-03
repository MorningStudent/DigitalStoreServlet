<html>
<body>
<h1>Your order <%=request.getAttribute("orderHashedId")%> was paid and confirmed!</h1>
<hr>
<a href="<%=request.getContextPath()%>/home">Go back to home page</a>
</body>
</html>
