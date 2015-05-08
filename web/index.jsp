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
        <s:form action="search_use">
            <s:textfield name="search" size="24" id="search"/>
            <s:submit value="検索"/>
        </s:form>
    
        <br>    
            
        <s:iterator value="items">
            <s:property value="id"/>
            <s:property value="name"/><br>
     
        </s:iterator>

        
    </body>
</html>
