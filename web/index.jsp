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
        <link rel="stylesheet" type="text/css" href="<s:url value="/css/common.css"/>" />
    </head>

    <body>
        <div id="container">
            <div id="header">
                <h1>ブログへようこそ</h1>
                <s:form action="myPage">
                    <s:submit value="ログインページ"/><br/><br/>
                </s:form>
            </div>
            <div id="content">
                <br>
                <s:form action="search_use">
                    ユーザ名で検索<s:textfield name="search" size="24" id="search"/>
                    <s:submit value="検索"/>
                </s:form>

                <br>    


                <s:iterator value="users" status="rs" var="user">
                    <s:if test="#rs.first">
                        <h3>ユーザID：ユーザ名</h3>
                    </s:if> 
                    <s:url id="mvuser" action="move_userpage">
                        <s:param name="id"><s:property value="#user.id"/></s:param>
                    </s:url>
                    <s:a href="%{mvuser}"><s:property value="#user.id"/>：<s:property value="#user.username"/></s:a><br>

                </s:iterator>
                <s:actionerror />
            </div>
            <div id="footer"></div>
        </div>

    </body>
</html>
