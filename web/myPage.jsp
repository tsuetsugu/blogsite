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
                <h1>マイページ</h1>
                <s:form action="logout">
                    <s:submit id="logout" value="ログアウト"/>
                </s:form>
            </div>
            <div id="sidebar01">
                <h3>カテゴリ</h3>
                <s:iterator value="%{#session.artCategorys}" var="cat">
                    <s:url id="catarts" action="cat_article">
                        <s:param name="code"><s:property value="#cat.code"/></s:param>
                    </s:url>                        
                    <s:property value="#cat.code_name" />(<s:a href="%{catarts}"><s:property value="#cat.count"/></s:a>)<br><br>
                </s:iterator>

                <s:iterator value="%{#session.catArticles}" var="catArt" status="artst">

                    <s:if test="#artst.first">
                        <p>記事一覧</p>
                    </s:if>
                    <s:url id="arts" action="select_article">
                        <s:param name="post_id"><s:property value="%{#catArt.post_id}"/></s:param>
                    </s:url>                        
                    <s:a href="%{arts}"><s:property value="#catArt.post_title" /></s:a><br><br>
                </s:iterator>    


            </div>
            <div id="content">
                <s:form action="write_article">
                    <s:submit value="新規記事登録"/><br>
                </s:form>

                <s:iterator value="%{#session.currentArticles}" var="art" status="artst">
                    <s:if test="#artst.first">
                        <s:form action="editArticle">                   
                            <h4>タイトル：<s:property value="%{#art.post_title}"/></h4>
                            <p class="date"><s:property value="%{#art.post_date}"/></p>
       
                            <s:hidden name="post_id" value="%{#art.post_id}"/><br>
                            <s:property value="%{#art.post}"/><br><br>
                            <s:submit value="記事編集"/>
                        </s:form>
                        <br>    
                        <s:form action="write_comment">
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
                                <s:url id="urlback" action="myback">
                                    <s:param name="index"><s:property value="%{#session.backindex}"/></s:param>
                                    <s:param name="post_id"><s:property value="#art.post_id"/></s:param>
                                </s:url>                        
                                <s:a href="%{urlback}">前</s:a>
                            </s:if>  

                            <s:if test="%{#session.nextflg == 1}">

                                <s:url id="urlnext" action="mynext">
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
                <s:form action="edit_profile">
                    <s:label value="プロフィール"/>
                    <s:submit value="編集"/><br><br>

                    <s:label value="名前："/><s:property value="#session.currentUser.username"/><br><br>
                    <img src="<s:property value = "%{#session.currentprofileimage.filepath}"/>" width="175" height="100" alt="No Image" /><br><br>
                    <s:label value="出身："/><s:property value="#session.currentUser.home"/><br><br>
                    <s:label value="自己紹介"/><br><br><s:property value="#session.currentUser.intro_myself"/><br>

                </s:form>
            </div>
            <div id="footer"></div>
        </div>        
    </body>
</html>
