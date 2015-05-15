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
    @Result(name = "commit_article", location = "/myPage.jsp")
})

public class ArticleEditAction extends AbstractDBAction {

    private static Logger logger = Logger.getLogger(IndexAction.class);
    private ArrayList<MasterCode> catgcds = new ArrayList<>();
    private ArrayList<MasterCode> statuscds = new ArrayList<>();
    private ArrayList<Article> currentAirticles = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private java.util.Date date = new java.util.Date();
    private String post_id;
    private String category;
    private String title;
    private String status;
    private String post;

    public String doInit() throws Exception {
        //カテゴリ取得
        getMasterCode("CATCD", catgcds);
        setMasterCode("categorycode", catgcds);

        //ステータス取得
        getMasterCode("STCD", statuscds);
        setMasterCode("statuscode", statuscds);

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

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
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

            //記事登録の場合
            
            User user = getCurrentUser();
            
            updateArticle(0);                        
            
            //記事の再取得
            getArticles(user);
            
            

        //記事編集の場合
        //updateArticle(1);

        
        setArticles(currentAirticles);
        setComments(comments);
        
        
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
     * 記事登録/更新
     *
     * @param flg
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private int updateArticle(int flg) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {

            String sql = "";
            // 新規登録SQL
            if (flg == 0) {
                sql = "INSERT INTO posts (user_id,post_date,post_category,post_title,post,post_status,create_date) VALUES(?,?,?,?,?,?,?)";
            } else {
                //String sql = "INSERT INTO posts (login_id,password) VALUES(?,?)"; 
            }

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行

            User user = getCurrentUser();

            stmt.setLong(1, user.getId());
            stmt.setDate(2, new java.sql.Date(date.getTime()));
            stmt.setString(3, category);
            stmt.setString(4, title);
            stmt.setString(5, post);
            stmt.setString(6, "00");

            Date today = new Date();
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(7, sdf1.format(today));
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
                       + "post_id=(SELECT MAX(post_id) FROM posts WHERE user_id=?)";
            
            Connection con = getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setLong(1, user.getId());
            stmt.setLong(2, user.getId());
            
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
            
            throw e;
            
        }
    }
    
    
    
}
