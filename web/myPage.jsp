<%-- 
    Document   : myPage
    Created on : 2015/05/08, 14:19:55
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
        <h1>マイページ</h1>
        <s:form action="write_article">
            <s:submit value="新規記事登録"/>
        </s:form>
        <s:form action="edit_article">
            <s:submit value="記事編集"/>
        </s:form>
        <s:form action="write_comment">
            <s:submit value="コメントする"/>
        </s:form>
        <s:form action="logout">
            <s:submit value="ログアウト"/>
        </s:form>
        <s:form action="edit_profile">
            <s:label value="プロフィール"/>
            <s:submit value="編集"/>
        </s:form>            
    </body>
</html>
