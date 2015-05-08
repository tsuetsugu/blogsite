<%-- 
    Document   : comment
    Created on : 2015/05/08, 14:59:02
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
        <s:form action="commit_comment">
            <s:label label="タイトル" value="XXXXX" name="title"/><br><br>
            <s:textfield name="userName" label="名前" id="userName"/><br><br>
            <s:label value="コメント"/><br>
            <s:textarea rows="20" name="comment"/><br>
            <s:submit value="登録" id="insert" align="center"/>
        </s:form>
        <s:form action="cancel_comment">
            <s:submit value="キャンセル" id="cancel" align="center"/>
        </s:form>    
    </body>
</html>
