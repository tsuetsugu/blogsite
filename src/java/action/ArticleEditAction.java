/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Article;
import model.ArticleCategory;
import model.Comment;
import model.MasterCode;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * 記事登録編集画面での処理
 *
 * @author lepra25-pc
 */
@Results({
    @Result(name = "success", location = "/articleEdit.jsp"),
    @Result(name = "cancel_article", location = "/myPage.jsp"),
    @Result(name = "commit_article", location = "/myPage.jsp"),
    @Result(name = "input", location = "/articleEdit.jsp")
})

public class ArticleEditAction extends AbstractDBAction {

    private static Logger logger = Logger.getLogger(IndexAction.class);
    private ArrayList<MasterCode> catgcds = new ArrayList<>();
    private ArrayList<MasterCode> statuscds = new ArrayList<>();
    private ArrayList<Article> currentAirticles = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<ArticleCategory> artcs = new ArrayList<>();
    private java.util.Date date = new java.util.Date();
    private long post_id;
    private String category;
    private String title;
    private String status;
    private String post;

    /**
     * バリデーション
     */
    public void validate() {
        if (date == null) {
            addActionError("日付を入力してください");
        }
    }

    public String doInit() throws Exception {
        //カテゴリ取得
        getMasterCode("CATCD", catgcds);
        setMasterCode("categorycode", catgcds);

        //ステータス取得
        getMasterCode("STCD", statuscds);
        setMasterCode("statuscode", statuscds);

        setPostId(0);

        return "success";
    }

    public ArrayList<MasterCode> getCatgcds() {
        return catgcds;
    }

    public void setCatgcds(ArrayList<MasterCode> catgcds) {
        this.catgcds = catgcds;
    }

    /**
     * 日付取得
     *
     * @return
     */
    public java.util.Date getDate() {
        return date;
    }

    /**
     *
     * @param date
     */
    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    /**
     * 更新ボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    @Action("/updatepost")
    public String updatepost() throws Exception {

        ArrayList<Comment> outcomments = new ArrayList<>();

        User user = getCurrentUser();

        long l_post_id = getPostId();

        //新規記事登録の場合
        if (l_post_id == 0) {
            addArticle();
            
                        //カテゴリに該当する記事の件数を取得
            getArticleCategrys(user);
            //カテゴリ一覧を設定
            setArtCats(artcs);

            //記事編集の場合   
        } else {
            updateArticle(l_post_id);

        }

        //編集記事の再取得
        getArticles(user);

        //セッションに設定
        setArticles(currentAirticles);
        setAllComments(comments);

        //次へボタンはデフォルト非表示
        setNextflg(0);
        setBackflg(0);
        setComNextIndex(0);
        setComBackIndex(0);

        int count = 0;

        for (Comment com : comments) {

            count = comments.indexOf(com);

            //次の表示(5件)を超えた場合
            if (count == 5) {
                //次へボタン表示
                setNextflg(1);
                setComNextIndex(count);
                break;
            }

            if (count <= 5) {
                outcomments.add(com);
            }
        }

        //表示用のコメントをセッションに格納
        setShowComments(outcomments);
        //更新処理
        return "commit_article";
    }

    /**
     * キャンセルボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    public String cancel() throws Exception {

        //セッションの削除
        delSession("currentPostid");
        return "cancel_article";
    }

    /**
     * マスタコード取得
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getMasterCode(String code_id, ArrayList<MasterCode> mscds) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            logger.error("code_id");
            // カテゴリ取得SQL
            String sql = "SELECT code, code_name FROM mastercode WHERE code_id=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setString(1, code_id);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MasterCode mscd = new MasterCode();
                mscd.setCode(rs.getString("code"));
                mscd.setCode_name(rs.getString("code_name"));
                mscds.add(mscd);
            }

            stmt.close();
            //con.close();
        } catch (Exception e) {
            logger.error(e.toString());
            throw e;

        }
    }

    /**
     * 記事登録
     *
     * @param flg
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private int addArticle() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {

            String sql = "";
            // 新規登録SQL
            sql = "INSERT INTO posts (user_id,post_date,post_category,post_title,post,post_status,create_date) VALUES(?,?,?,?,?,?,?)";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行

            logger.error("getuser");

            User user = getCurrentUser();

            logger.error("getuserOK");

            stmt.setLong(1, user.getId());
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            stmt.setString(3, category);
            stmt.setString(4, title);
            stmt.setString(5, post);
            stmt.setString(6, status);

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(7, sdf.format(today));
            int rs = stmt.executeUpdate();
            // SQL実行
                        /*
             statement = getConnection().createStatement();
             int num = statement
             .executeUpdate("INSERT INTO tbl_user (user_id,password) VALUES('" + loginid + "','" +
             password + "')");
             */

            return rs;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        }
    }

    /**
     * 記事編集
     *
     * @param flg
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private int updateArticle(long post_id) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {

            String sql = "";
            // 記事更新
            sql = "UPDATE posts SET post_date=?,post_category=?,post_title=?,post=?,post_status=?,update_date=? WHERE post_id=?";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行

            stmt.setDate(1, new java.sql.Date(date.getTime()));
            stmt.setString(2, category);
            stmt.setString(3, title);
            stmt.setString(4, post);
            stmt.setString(5, status);

            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(6, sdf.format(today));
            stmt.setLong(7, post_id);
            int rs = stmt.executeUpdate();
            // SQL実行
                        /*
             statement = getConnection().createStatement();
             int num = statement
             .executeUpdate("INSERT INTO tbl_user (user_id,password) VALUES('" + loginid + "','" +
             password + "')");
             */

            return rs;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        }
    }

    /**
     * 最新記事取得
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getArticles(User user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // ユーザ記事取得SQL
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and + "
                    + "post_date=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

            stmt.setLong(1, user.getId());
            stmt.setString(2, sdf.format(date));

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

                getComments(article);
            }

            stmt.close();
        } catch (Exception e) {

            throw e;

        }
    }

    private void getComments(Article art) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // 記事のコメント取得SQL
            String sql = "SELECT comment_id, post_id,username,create_date,comment FROM comments WHERE post_id=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            logger.error(art.getPost_id());
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

    /**
     * カテゴリ毎の記事数を取得
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getArticleCategrys(User user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // カテゴリ取得SQL
            String sql = "SELECT T0.code,T0.code_name,T1.count FROM mastercode AS T0 INNER JOIN (SELECT post_category,Count(post_id) As count from posts WHERE user_id=? group by post_category) AS T1 ON T0.code = T1.post_category WHERE code_id = 'CATCD'";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, user.getId());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                ArticleCategory artc = new ArticleCategory();
                artc.setCode(rs.getString("code"));
                artc.setCode_name(rs.getString("code_name"));
                artc.setCount(rs.getLong("count"));
                artcs.add(artc);
            }

            stmt.close();
        } catch (Exception e) {

            throw e;

        }
    }    
    
    
}
