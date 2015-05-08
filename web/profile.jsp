<%-- 
    Document   : profile
    Created on : 2015/05/08, 15:04:36
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
        <s:form action="commit_profile">
            <s:textfield name="username" value="" size="24" id="user_name" label="ユーザ名"/><br><br>
            <s:textfield name="category" label="出身地"/><br><br>
            <s:label value="画像"/><s:textfield name="title"/><br><br>
            <s:label value="アップロード"/><s:textfield name="upload"/><br><br>  
            <s:label value="自己紹介"/><br>
            <s:textarea rows="20" name="myself"/><br>          
            <s:submit value="更新"/>
        </s:form>
        <s:form action="cancel_profile">
            <s:submit value="キャンセル"/>
        </s:form>
    </body>
</html>
