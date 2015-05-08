<%-- 
    Document   : articleEdit.jsp
    Created on : 2015/05/08, 14:31:35
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
        <s:form action="commit_article">
            <s:label value="日付："/><s:textfield name="date" value="" size="10" id="date"/><br><br>
            <s:label value="カテゴリ："/><s:textfield name="category" value="" size="24" id="category"/><br><br>
            <s:label value="タイトル："/><s:textfield name="title" value="" size="24" id="title"/><br><br>
            <s:label value="記事"/><br><s:textarea name="article" rows="20" id="article"/><br>
            <s:submit value="更新" id="commit" align="center"/>
        </s:form>
        <s:form action="cancel_article">
            <s:submit value="キャンセル" id="cancel" align="center"/>
        </s:form>
    </body>
</html>
