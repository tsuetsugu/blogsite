/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import static action.PropertiesWithUtf8.loadUtf8Properties;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import model.Article;
import model.Comment;
import model.MasterCode;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
    @Result(name = "login_success", location = "/myPage.jsp"),
    @Result(name = "login_error", location = "/login.jsp"),
    @Result(name = "add_success", location = "/myPage.jsp"),
    @Result(name = "input", location = "/login.jsp")
})
public class SubmitAction extends AbstractDBAction {
    
    private String loginid;
    private String password;
    private ArrayList<MasterCode> mscds = new ArrayList<>();    
    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Article> currentAirticles = new ArrayList<>();

    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);

    /**
     * バリデーション
     */
    public void validate() {
        
        if (loginid == null || loginid.length() == 0) {
            addActionError("ログインIDを入力してください");
        }
        if (password == null || password.length() == 0) {
            addActionError("パスワードを入力してください");
        }
    }

    /**
     * ログイン処理
     *
     * @return
     * @throws Exception
     */
    @Action("/login")
    public String login() throws Exception {

        //メッセージプロパティ取得
        Properties prop = loadUtf8Properties("/message.properties");
        
        User user = getLoginUser();

        //ログイン成功時
        if (user != null) {

            //都道府県マスタ取得
            setMscds(this.mscds);
            setMasterCode("tdfcode", mscds);

            //出身地名を設定
            for (MasterCode mscd : mscds) {
                if (mscd.getCode().equals(user.getTodo_code())) {
                    user.setHome(mscd.getCode_name());
                    break;
                }
            }

            //セッションにユーザ情報を格納
            setCurrentUser(user);

            //記事取得
            getArticles(user);

            //記事数取得
            int artCount = getArtSize();

            //最新の記事取得
            if (artCount != 0) {
                //最新の記事とコメントのリスト作成
                createCurrentAirticles();
            }
            
            setArticles(currentAirticles);
            setComments(comments);
            
            return "login_success";
        } else {
            addActionError(prop.getProperty("login.error"));
            return "login_error";
        }

        //return SUCCESS;
    }

    /**
     * 新規登録処理
     *
     * @return
     * @throws Exception
     */
    @Action("/addUser")
    public String addUser() throws Exception {

        //メッセージプロパティ取得
        Properties prop = loadUtf8Properties("/message.properties");
        // 登録済みユーザを検索
        User user = getRegistUser();
        
        if (user != null) {
            addActionError(MessageFormat.format(prop.getProperty("adduser.exist"), loginid));
            return "login_error";
        }

        //パスワードをハッシュ化
        // ユーザ登録処理
        int result = insertUser();

        //登録に失敗した場合
        if (result == 0) {
            addActionError(prop.getProperty("adduser.error"));
            return "add_success";
            //登録に成功した場合、メッセージを出力し結果画面へ遷移する   
        } else {
            user = getLoginUser();
            //セッションにユーザ情報を格納
            setCurrentUser(user);
            setArticles(currentAirticles);
            setComments(comments);
            
            return "add_success";
        }
    }
    
    public String getLoginid() {
        return loginid;
    }
    
    public void setLoginid(String loginid) {
        this.loginid = loginid;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public int getArtSize() {
        return this.articles.size();
    }
    
    public ArrayList<Article> getArticles() {
        return articles;
    }
    
    public ArrayList<Comment> getComments() {
        return comments;
    }
    
    public ArrayList<Article> getCurrentAirticles() {
        return currentAirticles;
    }
    
    public ArrayList<MasterCode> getMscds() {
        return mscds;
    }

    //出身地のリスト取得
    public void setMscds(ArrayList<MasterCode> mscds) throws Exception {
        gethome();
        
    }

    /**
     * ログインユーザ検索
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private User getLoginUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        PreparedStatement stmt= null;
        try {
            // ログインSQL
            String sql = "SELECT user_id, username,todo_code,intro_myself FROM users WHERE login_id=? and password=?";
            
            stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, loginid);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            
            boolean br = rs.first();
            if (br == false) {
                return null;
            }
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            user.setTodo_code(rs.getString("todo_code"));
            user.setIntro_myself(rs.getString("intro_myself"));
            
            return user;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
    }

    /**
     * 登録済みユーザ検索
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private User getRegistUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {
            // 登録済みユーザ確認SQL
            String sql = "SELECT user_id, username FROM users WHERE login_id=?";
            
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, loginid);
            ResultSet rs = stmt.executeQuery();
            
            boolean br = rs.first();
            if (br == false) {
                return null;
            }
            User user = new User();
            user.setId(rs.getLong("user_id"));
            user.setUsername(rs.getString("username"));
            
            return user;
        } catch (SQLException e) {
            logger.error("SQLException:" + e.getMessage());
            throw e;
        } finally {
            if (statement != null) {
                statement.close();
                statement = null;
            }
        }
    }

    /**
     * 新規ユーザ登録
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private int insertUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {
            // 新規登録SQL
            String sql = "INSERT INTO users (login_id,password) VALUES(?,?)";
            
            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, loginid);
            stmt.setString(2, password);
            int rs = stmt.executeUpdate();
            
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
     * 記事取得
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
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? ORDER BY post_date DESC, post_id ASC";
            
            Connection con = getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql);
            
            stmt.setLong(1, user.getId());
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Article article = new Article();
                article.setPost_id(rs.getLong("post_id"));
                article.setPost_date(rs.getDate("post_date"));
                article.setPost_category(rs.getString("post_category"));
                article.setPost_title(rs.getString("post_title"));
                article.setPost(rs.getString("post"));
                article.setPos_status(rs.getString("post_status"));
                articles.add(article);
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
    
    private void createCurrentAirticles() throws Exception {
        
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        String strDate = "";
        int count = 1;
        for (Article article : articles) {
            if (count == 1) {
                strDate = sdf.format(article.getPost_date());
                currentAirticles.add(article);

                //コメントのリスト作成
                getComments(article);
                count++;
                continue;
            }

            //日付が同じ場合、リストに追加
            if (strDate.equals(sdf.format(article.getPost_date()))) {
                currentAirticles.add(article);
                //コメントのリスト作成
                getComments(article);
            }
            count++;
            
        }
    }
    
    private void gethome() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // 都道府県取得SQL
            String sql = "SELECT code, code_name FROM mastercode WHERE code_id='TDFCD'";
            
            Connection con = getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql);
            
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                MasterCode mscd = new MasterCode();
                mscd.setCode(rs.getString("code"));
                mscd.setCode_name(rs.getString("code_name"));
                mscds.add(mscd);
            }
            
            stmt.close();
        } catch (Exception e) {
            
            throw e;
            
        }
    }
    
}
