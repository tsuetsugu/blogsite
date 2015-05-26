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
    <h1>記事登録編集画面</h1>
    <s:actionerror />
    <s:form action="updatepost">
        <table width="100%"  border="1" cellpadding="3" cellspacing="1" bordercolor="#CCCCCC">
            <s:if test="%{#session.currentPostid == 0}">
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="日付"/></td>
                    <td valign="top"><s:textfield value="%{date}" name="date"/></td>
                </tr>
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="カテゴリ："/></td>
                    <td valign="top">
                        <s:select list="%{#session.categorycode}" name="category" listKey = "code" listValue="code_name" value="code"></s:select>
                        </td>
                    </tr>
                    <tr>    
                        <td valign="top" bgcolor="#F2F2F2"><s:label value="タイトル："/></td>
                    <td valign="top">
                        <s:textfield name="title" value="" size="24" id="title"/>
                    </td>
                </tr>
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="ステータス："/></td>
                    <td valign="top">
                        <s:select list="%{#session.statuscode}" name="status" listKey = "code" listValue="code_name"></s:select>
                        </td>
                    </tr>
                    <tr>
                        <td width="22%" valign="top" bgcolor="#F2F2F2"><s:label value="記事"/></td>
                    <td width="78%" valign="top">    
                        <s:textarea name="post" rows="20" cols="70" id="article"/>
                    </td>

                </tr>
            </s:if>

            <s:elseif test="%{#session.currentPostid != 0}">
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="日付"/></td>
                    <td valign="top"><s:textfield value="%{#session.currentArticle.post_date}" name="date"/></td>
                </tr>
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="カテゴリ"/></td>
                    <td valign="top"><s:select list="%{#session.categorycode}" name="category" listKey = "code" listValue="code_name" value="%{#session.currentArticle.post_category}"></s:select></td>
                    </tr>
                    <tr>
                        <td valign="top" bgcolor="#F2F2F2"><s:label value="タイトル"/></td>
                    <td valign="top"><s:textfield name="title" value="%{#session.currentArticle.post_title}" size="24" id="title"/><td>
                </tr>
                <tr>
                    <td valign="top" bgcolor="#F2F2F2"><s:label value="ステータス"/></td>
                    <td valign="top"><s:select list="%{#session.statuscode}" name="status" listKey = "code" listValue="code_name" value="%{#session.currentArticle.pos_status}"></s:select></td>
                    </tr>
                    <tr>
                        <td width="22%" valign="top" bgcolor="#F2F2F2"><s:label value="記事"/></td>
                    <td width="78%" valign="top">
                        <s:textarea name="post" rows="30" cols="70" id="article" value="%{#session.currentArticle.post}"/>
                    </td>
                </tr>
            </s:elseif>
            <tr align="center" bgcolor="#FFFFFF">
                <td height="40" colspan="2">
                    <s:submit value="更新" id="commit" align="center"/>

                </s:form>
                <s:form action="cancel_article">

                    <s:submit value="キャンセル" id="cancel" align="center"/>

                </s:form>
            </td>
        </tr>
    </table>

</html>
