<html>
<body>
<h1>Welcome to our digital store! (updated)</h1>
<hr>
<h2><%=request.getAttribute("productName")%></h2>
<img src="<%=request.getContextPath()%>/images/<%=request.getAttribute("productImage")%>"/>
<p><%=request.getAttribute("productPriceAmount")%><%=request.getAttribute("productPriceCurrency")%></p>
<a href="<%=request.getContextPath()%>/buy/<%=request.getAttribute("productHashedId")%>">BUY</a>
</body>
</html>
