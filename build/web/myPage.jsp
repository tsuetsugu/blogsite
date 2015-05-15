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
            <s:submit value="新規記事登録"/><br>
        </s:form>

        <s:iterator value="%{#session.currentArticles}" status="st">
            <s:form action="editArticle">                   
                <h4>タイトル：<s:property value="post_title"/></h4>
                <s:hidden name="post_id" value="%{post_id}"/><br>
                <s:property value="post"/><br><br>
                <s:submit value="記事編集"/>
            </s:form>
            <br>    
            <s:form action="write_comment">
                コメント
                <s:submit value="コメントする"/><br>
                <s:hidden name="post_id" value="%{post_id}"/><br>
                <s:hidden name="post_title" value="%{post_title}"/><br>
                <s:iterator value="%{#session.currentComments}">
                    <s:property value="username"/>
                    <s:property value="create_date"/><br>
                    <s:property value="comment"/><br><br>
                </s:iterator>
            </s:form>
        </s:iterator>

        <br>
        <s:form action="logout">
            <s:submit value="ログアウト"/>
        </s:form>
        <br>
        <s:form action="edit_profile">
            <s:label value="プロフィール"/>
            <s:submit value="編集"/><br><br>

            <s:label value="名前："/><s:property value="#session.currentUser.username"/><br><br>
            <s:label value="出身："/><s:property value="#session.currentUser.home"/><br><br>
            <s:label value="自己紹介"/><br><br><s:property value="#session.currentUser.intro_myself"/><br>

        </s:form>            
    </body>
</html>
