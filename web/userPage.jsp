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
                <p class="categoryTitle">カテゴリ</p>
                <s:iterator value="%{#session.artCategorys}" var="cat">
                    <s:url id="catarts" action="use_cat_article">
                        <s:param name="code"><s:property value="#cat.code"/></s:param>
                    </s:url>                        
                    <p class="categoryDetails"><s:property value="#cat.code_name" />(<s:a href="%{catarts}"><s:property value="#cat.count"/></s:a>)</p>
                </s:iterator>

                <s:iterator value="%{#session.catArticles}" var="catArt" status="artst">

                    <s:if test="#artst.first">
                        <p class="articleListTitle">記事一覧</p>
                    </s:if>
                    <s:url id="arts" action="use_select_article">
                        <s:param name="post_id"><s:property value="%{#catArt.post_id}"/></s:param>
                    </s:url>                        
                    <p class="articleList"><s:a href="%{arts}"><s:property value="#catArt.post_title" /></s:a></p>
                </s:iterator>                     





            </div>
            <div id="content">
                <s:iterator value="%{#session.currentArticles}" var="art" status="artst">
                    <div class="article">
                        <s:if test="#artst.first">
                            <p class="articleTitle"><s:property value="#art.post_title"/></p>
                            <p class="date"><s:property value="%{#art.post_date}"/></p>
                            <p class="details"><s:property value="#art.post"/></p>
                        </div>
                        <p></p>
                        <div class="comment">
                            <h4 class="commentTitle">コメント</h4>   
                            <s:form action="user_comment">
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
                                    <s:url id="urlback" action="userback">
                                        <s:param name="index"><s:property value="%{#session.backindex}"/></s:param>
                                        <s:param name="post_id"><s:property value="#art.post_id"/></s:param>
                                    </s:url>                        
                                    <s:a href="%{urlback}"><s:label value="前" cssClass="comback"></s:label></s:a>
                                </s:if>  

                                <s:if test="%{#session.nextflg == 1}">

                                    <s:url id="urlnext" action="usernext">
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
                    <p class="profileLabel"><s:label value="プロフィール"/></p>

                    <p class="profileUsername"><s:label value="名前："/><s:property value="#session.showuser.username"/></p>
                    <p><img src="<s:property value = "%{#session.currentuserprofileimage.filepath}"/>" alt="No Image" width="175" height="100" /></p>
                    <p class="profileHome"><s:label value="出身："/><s:property value="#session.showuser.home"/></p>
                    <p class="profileMyselfLabel"><s:label value="自己紹介"/></p>
                    <p class="profileMyself"> <s:property value="#session.showuser.intro_myself"/></p>
                </div>
            </div>
            <div id="footer"></div>
        </div>  
    </body>
</html>
