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
                <p class="categoryTitle">カテゴリ</p>
                <s:iterator value="%{#session.artCategorys}" var="cat">
                    <s:url id="catarts" action="cat_article">
                        <s:param name="code"><s:property value="#cat.code"/></s:param>
                    </s:url>                        
                    <p class="categoryDetails"><s:property value="#cat.code_name" />(<s:a href="%{catarts}"><s:property value="#cat.count"/></s:a>)</p>
                </s:iterator>

                <s:iterator value="%{#session.catArticles}" var="catArt" status="artst">
                    <s:if test="#artst.first">
                        <p class="articleListTitle">記事一覧</p>
                    </s:if>
                    <s:url id="arts" action="select_article">
                        <s:param name="post_id"><s:property value="%{#catArt.post_id}"/></s:param>
                    </s:url>                        
                    <p class="articleList"><s:a href="%{arts}"><s:property value="#catArt.post_title" /></s:a></p>
                </s:iterator>    
            </div>
            <div id="content">
                <s:form action="write_article">
                    <s:submit value="新規記事登録"/>
                    <p></p>
                </s:form>

                <s:iterator value="%{#session.currentArticles}" var="art" status="artst">
                    <s:if test="#artst.count==1">
                        <div class="article">
                            <s:form action="editArticle">                   
                                <p class="articleTitle"><s:property value="%{#art.post_title}"/></p>
                                <p class="date"><s:property value="%{#art.post_date}"/></p>

                                <p><s:hidden name="post_id" value="%{#art.post_id}"/></p>
                                <p class="details"><s:property value="%{#art.post}"/></p>
                                <p class="articleEdit"><s:submit value="記事編集"/></p>
                            </s:form>
                        </div>
                    </s:if>

                    <p></p>
                    <s:if test="#artst.first">
                        <div class="comment">
                            <h4 class="commentTitle">コメント</h4>
                            <s:form action="write_comment">
                                <p><s:hidden name="post_id" value="%{#art.post_id}"/></p>
                                <p><s:hidden name="post_title" value="%{#art.post_title}"/></p>
                                <s:iterator value="%{#session.showComments}" var="com">
                                    <s:if test="#art.post_id == #com.post_id" >
                                        <p class="comment_user"><s:property value="#com.username"/>
                                            <s:property value="#com.create_date"/></p>
                                        <p class="comment_details"><s:property value="#com.comment"/></p>
                                    </s:if>

                                </s:iterator>
                                <s:if test="%{#session.backflg == 1}">
                                    <s:url id="urlback" action="myback">
                                        <s:param name="index"><s:property value="%{#session.backindex}"/></s:param>
                                        <s:param name="post_id"><s:property value="#art.post_id"/></s:param>
                                    </s:url>                        
                                    <s:a href="%{urlback}"><s:label value="前" cssClass="comback"></s:label></s:a>
                                </s:if>  

                                <s:if test="%{#session.nextflg == 1}">

                                    <s:url id="urlnext" action="mynext">
                                        <s:param name="index"><s:property value="%{#session.nextindex}"/></s:param>
                                        <s:param name="post_id"><s:property value="#art.post_id"/></s:param>
                                    </s:url>                        
                                    <s:a href="%{urlnext}"><s:label value="次" cssClass="comnext"></s:label></s:a>
                                </s:if>    
                                <div class="comadd"><s:submit value="コメントする" /></div>
                            </s:form>
                        </div>
                    </s:if>

                </s:iterator>
            </div>
            <div id="sidebar02">

                <div class="calendar">
                    <s:include value="/calendar.jsp">

                    </s:include>
                </div>    

                <br>
                <div id="userprofile">
                    <s:form action="edit_profile">
                        <p class="profileLabel"><s:label value="プロフィール"/>
                            <s:submit value="編集"/></p>

                        <p class="profileUsername"><s:label value="名前："/><s:property value="#session.currentUser.username"/></p>
                        <p><img src="<s:property value = "%{#session.currentprofileimage.filepath}"/>" width="175" height="100" alt="No Image" /></p>
                        <p class="profileHome"> <s:label value="出身："/><s:property value="#session.currentUser.home"/></p>
                        <p class="profileMyselfLabel"><s:label value="自己紹介"/></p>
                        <p class="profileMyself"><s:property value="#session.currentUser.intro_myself"/></p>

                    </s:form>
                </div>
            </div>
            <div id="footer"></div>
        </div>        
    </body>
</html>
