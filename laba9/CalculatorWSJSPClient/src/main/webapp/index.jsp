<%-- 
    Document   : index
    Created on : 30.01.2020, 20:00:22
    Author     : Admin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>JSP Application. Zaripov Islam 4441.</h1>
        <%
        try {
            ru.kai.inkrot.calculatorwsjspclient.CalculatorWS_Service service = new
            ru.kai.inkrot.calculatorwsjspclient.CalculatorWS_Service();
            ru.kai.inkrot.calculatorwsjspclient.CalculatorWS port =
            service.getCalculatorWSPort();
            int i = 3;
            int j = 6;
            int result = port.add(i, j);
            out.println("Result = "+result);
        } catch (Exception ex) {
            out.println("exception" + ex);
        }
        %>
    </body>
</html>
