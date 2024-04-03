<html>
    <body>
        <h1>Order information!</h1>
        <hr>
        
        <form action="<%=request.getContextPath()%>/buy" method="POST">
            <input type="hidden" value="<%=request.getAttribute("productHashedId")%>" name="productHashedId"/>
            <fieldset>
                <legend>product<legend>
                <label>
                    QUANTITY
                    <input type="number" value="1" name="quantity"/>
                </label>
            </fieldset>
            <fieldset>
                <legend>client<legend>
                <label>
                    NAME
                    <input type="text" placeholder=" your full name " name="name"/>
                </label>
                <hr>
                <label>
                    EMAIL
                    <input type="text" placeholder=" your email address " name="email"/>
                </label>
                <hr>
                <label>
                    PHONE
                    <input type="text" placeholder=" your phone " name="phone"/>
                </label>
                <hr>
            </fieldset>

            <button>PURCHASE</button>

        </form>

    </body>
</html>
