<%-- 
    Document   : login
    Created on : 2015/05/08, 12:14:47
    Author     : lepra25-pc
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags" %>  
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>

        <h1>ログイン画面</h1>
        <s:actionerror />
        <s:form action="login_action">
            <s:label value="ユーザID："/><s:textfield name="userid" value="" size="24" id="userid"/><br/><br/>
            <s:label value="パスワード："/><s:password name="password" value="" size="24" id="password"/><br/><br/>
            <s:submit value="ログイン" id="login" align="center"/>
        </s:form>
        
    </body>
</html>
