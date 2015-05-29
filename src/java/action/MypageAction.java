/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javax.servlet.http.HttpServletRequest;
import model.Article;
import model.Comment;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import static constants.Constant.*;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.InterceptorRefs;
import org.apache.struts2.convention.annotation.ParentPackage;

/**
 *
 * @author lepra25-pc
 */
@Results({
    @Result(name = "success", location = "/articleEdit.jsp"),
    @Result(name = "error", location = "/login.jsp"),
    @Result(name = "logout", location = "/index.jsp"),
    @Result(name = "login", location = "/login.jsp")
})

@ParentPackage("test")
@InterceptorRefs({
    @InterceptorRef("blogStack")
})

public class MypageAction extends AbstractDBAction {

    private static Logger logger = Logger.getLogger(IndexAction.class);
    private ArrayList<Article> currentAirticles = new ArrayList<>();
    private Article article = new Article();
    private long post_id;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Comment> outcomments = new ArrayList<>();
    private int index;
    private String code;

    
    
    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Action("/doInit")
    public String doInit() {
        return "success";
    }

    /**
     * 押下されたカテゴリの記事一覧表示
     *
     * @return
     */
    public String showCatArticles() throws Exception {

        currentAirticles.clear();

        User user = new User();
        user = getCurrentUser();

        //記事取得
        getArticles(user, code);

        //カテゴリ毎の記事をセッションに設定
        setSCatArticles(currentAirticles);

        return "success";
    }

    /**
     * カテゴリ記事一覧で押下された記事表示
     *
     * @return
     */
    public String showCatArticle() throws Exception {

        currentAirticles.clear();
        comments.clear();
        outcomments.clear();

        ArrayList<Article> showArts = new ArrayList<>();

        //記事取得
        currentAirticles = getSCatArticles();

        for (Article article : currentAirticles) {

            if (article.getPost_id() == post_id) {

                showArts.add(article);
                //コメントのリスト作成
                getComments(article);
            }
        }

        //現在の記事としてセッションに設定
        setArticles(showArts);

        //次へボタンはデフォルト非表示
        setNextflg(0);
        setBackflg(0);
        setComNextIndex(0);
        setComBackIndex(0);
        int count = 0;
        for (Comment com : comments) {

            if (post_id == com.getPost_id()) {
                count = count + 1;

                //次の表示を超えた場合
                if (count > BODER) {
                    //次へボタン表示
                    setNextflg(1);
                    setComNextIndex(count - 1);
                    break;
                }

                if (count <= BODER) {
                    outcomments.add(com);
                }
            }

        }

        //表示用のコメントをセッションに格納
        setShowComments(outcomments);

        return "success";
    }

    /**
     * 記事編集
     *
     * @return
     */
    @Action("/editArticle")
    public String editArticle() {

        currentAirticles.clear();

        //セッションから記事情報の取得
        currentAirticles = getArticles();

        //画面から取得した記事IDに該当する記事情報を取得
        for (Article art : currentAirticles) {
            if (post_id == art.getPost_id()) {
                setPostId(art.getPost_id());
                setArticle(art);
                break;
            }
        }

        return "success";
    }

    /**
     * ログアウト
     *
     * @return
     */
    @Action("/logout")
    public String logout() {

        //ユーザディレクトリ削除
        DeleteFile del = new DeleteFile();

        if (getCurrentmyImage() != null) {
            logger.error("カレントイメージ：");
            if (getCurrentmyImage().getFilefullpath() != null) {
                logger.error("カレントイメージ：" + getCurrentmyImage().getFilefullpath());
                File file = new File(getCurrentmyImage().getFilefullpath());
                del.delFile(file);
            }
        }
        
        if (getUploadImage() != null) {
            logger.error("アップロードイメージ：");
            if (getUploadImage().getFilefullpath() != null) {
                logger.error("アップロードイメージ：" + getUploadImage().getFilefullpath());
                File file = new File(getUploadImage().getFilefullpath());

                del.delFile(file);
            }
        }
        delSession();

        return "logout";
    }

    /**
     * コメント次へボタン処理
     *
     * @return
     */
    public String next() {

        outcomments.clear();
        comments.clear();

        //全体のコメントリスト取得
        comments = getAllComments();

        //次へボタンを非表示、前ボタンを表示
        setNextflg(0);
        setBackflg(1);

        int count = 0;

        //前のIndex取得
        int backIndex = ((index / BODER) - 1) * BODER;
        int currentIndex = index;

        //コメントの表示制御
        for (Comment com : comments) {

            if (post_id == com.getPost_id()) {
                count = count + 1;

                //次の表示を超えた場合
                if (count > currentIndex + BODER) {
                    setNextflg(1);
                    break;
                }

                if (count > currentIndex) {
                    outcomments.add(com);
                }
            }

        }

        //INDEXをセッションに保持
        setComNextIndex(count - 1);
        setComBackIndex(backIndex);

        //表示用のコメントに設定
        setShowComments(outcomments);

        return "mypage";
    }

    /**
     * 前ボタン押下時の処理
     *
     * @return
     */
    public String back() {

        outcomments.clear();
        comments.clear();
        //全体のコメントリスト取得
        comments = getAllComments();

        //次へボタンを表示、前ボタンを非表示
        setNextflg(1);
        setBackflg(0);

        int count = 0;

        int firstIndex = index;

        int backIndex = 0;

        //最初の表示でなければ前へのボタンを表示
        if (firstIndex != 0) {
            backIndex = ((index / BODER) - 1) * BODER;
            setBackflg(1);
        }

        for (Comment com : comments) {

            if (post_id == com.getPost_id()) {
                count = count + 1;

                //次の表示を超えた場合
                if (count > firstIndex + BODER) {
                    setNextflg(1);
                    break;
                }

                if (count > firstIndex) {
                    outcomments.add(com);
                }
            }
        }
        //INDEXをセッションに保持
        setComNextIndex(count - 1);
        setComBackIndex(backIndex);
        //表示用のコメントに設定
        setShowComments(outcomments);

        return "mypage";
    }

    /**
     * 記事取得(カテゴリ条件)
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getArticles(User user, String code) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // ユーザ記事取得SQL
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_category=? ORDER BY post_date DESC, post_id DESC";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, user.getId());
            stmt.setString(2, code);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article article = new Article();
                article.setPost_id(rs.getLong("post_id"));
                article.setPost_date(rs.getDate("post_date"));
                article.setPost_category(rs.getString("post_category"));
                article.setPost_title(rs.getString("post_title"));
                article.setPost(rs.getString("post"));
                article.setPos_status(rs.getString("post_status"));
                currentAirticles.add(article);
            }

            stmt.close();
        } catch (Exception e) {
            logger.error(e.getStackTrace());

        }
    }

    /**
     * コメント取得
     *
     * @param art
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getComments(Article art) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // 記事のコメント取得SQL
            String sql = "SELECT comment_id, post_id,username,create_date,comment FROM comments WHERE post_id=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, art.getPost_id());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setComment_id(rs.getLong("comment_id"));
                comment.setPost_id(rs.getLong("post_id"));
                comment.setUsername(rs.getString("username"));
                comment.setCreate_date(rs.getDate("create_date"));
                comment.setComment(rs.getString("comment"));
                comments.add(comment);
            }

            stmt.close();
        } catch (Exception e) {

            throw e;

        }
    }

}
