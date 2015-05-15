/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.util.ArrayList;
import model.Article;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 *
 * @author lepra25-pc
 */
@Results({
    @Result(name = "success", location = "/articleEdit.jsp"),
    @Result(name = "error", location = "/login.jsp")
})
public class MypageAction extends AbstractDBAction {

    private static Logger logger = Logger.getLogger(IndexAction.class);
    private ArrayList<Article> currentAirticles = new ArrayList<>();
    private Article article = new Article();
    private String post_id;

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    @Action("/doInit")
    public String doInit() {
        return "success";
    }

    @Action("/editArticle")
    public String editArticle() {

        //セッションから記事情報の取得
        currentAirticles = getArticles();

        //画面から取得した記事IDに該当する記事情報を取得
        for (Article art : currentAirticles) {
            if (post_id.equals(art.getPost_id())) {
                setPostId(art.getPost_id());
                setArticle(art);
                break;
            }
        }

        return "success";
    }

    public String logout() {

        return "logout";
    }

}
