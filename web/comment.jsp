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
        <h1>コメント追加画面</h1>
        <s:actionerror />
        <s:form action="add_comment">
            <s:token/>
           
            <table width="100%"  border="1" cellpadding="3" cellspacing="1" bordercolor="#CCCCCC">
                <tr>
                    <td valign="top" bgcolor="#F2F2F2">タイトル</td>
                    <td valign="top">
                        <s:property value="post_title"/>
                    </td>
                </tr>
                <tr>
                    <td valign="top" bgcolor="#F2F2F2">
                        <s:label value="ユーザ名"/>
                    </td>
                    <td valign="top">
                        <s:textfield name="userName" id="userName" value="%{#session.currentUser.username}"/>
                    </td>
                </tr>
                <tr>
                    <td width="22%" valign="top" bgcolor="#F2F2F2">
                        <s:label value="コメント"/></td>
                    </td>
                    <td width="78%" valign="top">
                        <s:textarea rows="10" cols="70" name="comment"/>
                    </td>
                </tr>
                <tr align="center" bgcolor="#FFFFFF">
                    <td height="40" colspan="2">
                        <s:submit value="登録" id="insert" align="center"/>
                    </s:form>
                    <s:form action="cancel_comment">
                        <s:submit value="キャンセル" id="cancel" align="center"/>
                    </s:form>
                </td>
        </table>

    </body>
</html>
