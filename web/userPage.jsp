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
        <link rel="stylesheet" type="text/css" href="<s:url value="/css/common.css"/>" />
    </head>
    <body>
        <div id="container">
            <div id="header">
                <h1>ユーザページ</h1>
                <s:form action="mv_index">
                    <s:submit value="初期ページ"/>
                </s:form>
            </div>
            <div id="sidebar01">
                <h3>カテゴリ</h3>
                <s:iterator value="%{#session.artCategorys}" var="cat">
                    <s:url id="catarts" action="use_cat_article">
                        <s:param name="code"><s:property value="#cat.code"/></s:param>
                    </s:url>                        
                    <s:a href="%{catarts}"><s:property value="#cat.code_name" />(<s:property value="#cat.count"/>)</s:a><br><br>
                </s:iterator>
            </div>
            <div id="content">
                <s:iterator value="%{#session.currentArticles}" var="art" status="artst">
                    <s:if test="#artst.first">
                        <h4>タイトル：<s:property value="#art.post_title"/></h4>
                        <s:property value="#art.post"/><br><br>

                        <br>    
                        <s:form action="user_comment">
                            コメント
                            <s:submit value="コメントする"/><br>
                            <s:hidden name="post_id" value="%{#art.post_id}"/><br>
                            <s:hidden name="post_title" value="%{#art.post_title}"/><br>
                            <s:iterator value="%{#session.currentComments}" var="com">
                                <s:if test="#art.post_id == #com.post_id" >
                                    <s:property value="#com.username"/>
                                    <s:property value="#com.create_date"/><br>
                                    <s:property value="#com.comment"/><br><br>
                                </s:if>

                            </s:iterator>
                            <s:if test="%{#session.backflg == 1}">
                                <s:url id="urlback" action="userback">
                                    <s:param name="index"><s:property value="%{#session.backindex}"/></s:param>
                                    <s:param name="post_id"><s:property value="#art.post_id"/></s:param>
                                </s:url>                        
                                <s:a href="%{urlback}">前</s:a>
                            </s:if>  

                            <s:if test="%{#session.nextflg == 1}">

                                <s:url id="urlnext" action="usernext">
                                    <s:param name="index"><s:property value="%{#session.nextindex}"/></s:param>
                                    <s:param name="post_id"><s:property value="#art.post_id"/></s:param>
                                </s:url>                        
                                <s:a href="%{urlnext}">次</s:a>
                            </s:if>    


                        </s:form>
                    </s:if>
                </s:iterator>
            </div>

            <div id="sidebar02">
                <s:include value="/calendar.jsp">

                </s:include>

                <br>
                <s:label value="プロフィール"/><br><br>

                <s:label value="名前："/><s:property value="#session.showuser.username"/><br><br>
                <img src="<s:property value = "%{#session.currentuserprofileimage.filepath}"/>" width="175" height="100" /><br><br>
                <s:label value="出身："/><s:property value="#session.showuser.home"/><br><br>
                <s:label value="自己紹介"/><br><br><s:property value="#session.showuser.intro_myself"/><br>
            </div>
            <div id="footer"></div>
        </div>  
    </body>
</html>
