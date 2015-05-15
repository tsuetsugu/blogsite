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

        <h1>パスワード変更画面</h1>
        <s:actionerror />
        <s:form action="changePass">
            <s:label value="ユーザID："/><s:textfield name="loginid" value="" size="24" id="userid"/><br/><br/>
            <s:label value="新規パスワード："/><s:password name="password" value="" size="24" id="password"/><br/><br/>
            <s:submit value="変更" />
        </s:form>
        <s:form action="cance_chpass">
            <s:submit value="キャンセル"/>
        </s:form>    
 
    </body>
</html>
