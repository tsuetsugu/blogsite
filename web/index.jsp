<%-- 
    Document   : index
    Created on : 2015/05/08, 13:11:17
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
        <h1>ブログへようこそ</h1>
        <s:form action="myPage">
            <s:submit value="マイページ"/><br/><br/>
        </s:form>
        <s:form action="search">
            <s:textfield name="username" value="" size="24" id="username"/>
            <s:submit value="検索"/>
        </s:form>
        
        
    </body>
</html>
