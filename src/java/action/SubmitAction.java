/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import action.PatternMatches.PatternEnum;
import static action.PropertiesWithUtf8.loadUtf8Properties;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
import com.opensymphony.xwork2.validator.annotations.Validations;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;
import javax.servlet.http.HttpServletRequest;
import model.Article;
import model.ArticleCategory;
import model.Comment;
import model.Image;
import model.MasterCode;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import static constants.Constant.*;

@Results({
    @Result(name = "login_success", location = "/myPage.jsp"),
    @Result(name = "login_error", location = "/login.jsp" ),
    @Result(name = "add_success", location = "/myPage.jsp"),
    @Result(name = "input", location = "/login.jsp")
})
public class SubmitAction extends AbstractDBAction {

    private String loginid;
    private String password;
    private ArrayList<MasterCode> mscds = new ArrayList<>();
    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Article> calarticles = new ArrayList<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<MasterCode> statuscds = new ArrayList<>();
    private ArrayList<MasterCode> categrys = new ArrayList<>();
    private ArrayList<ArticleCategory> artcs = new ArrayList<>();
    private Image img = new Image();
    private ArrayList<Comment> outcomments = new ArrayList<>();
    private Map<String, String> calMap = new HashMap<>();
    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);

    /**
     * バリデーション
     */
    @Override
    public void validate() {

        //メッセージプロパティ取得
        if (loginid == null || loginid.length() == 0) {
            addActionError("ログインIDを入力してください");
        }
        if (password == null || password.length() == 0) {
            addActionError("パスワードを入力してください");
        }

        final Pattern patternId = PatternEnum.ALNUM.toPattern();
        if (!PatternMatches.findMatches(patternId, loginid)) {
            addActionError("ログインIDは半角英数で入力してください");
        }

        final Pattern patternps = PatternEnum.PUNCT.toPattern();
        if (!PatternMatches.findMatches(patternps, password)) {
            addActionError("パスワードは半角英数記号で入力してください");
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

    public ArrayList<MasterCode> getMscds() {
        return mscds;
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
            getMasterCodes(TDFCD,mscds);
            setMasterCode(SESSION_TDFCD, mscds);

            //カテゴリを取得
            getMasterCodes(CATCD,categrys);
            setMasterCode(SESSION_CATCD, categrys);
            //ステータス取得
            getMasterCodes(STCD, statuscds);
            setMasterCode(SESSION_STCD, statuscds);

            //カテゴリに該当する記事の件数を取得
            getArticleCategrys(user);
            //記事のカテゴリ一覧を設定
            setArtCats(artcs);

            //出身地名を設定
            for (MasterCode mscd : mscds) {
                if (mscd.getCode().equals(user.getTodo_code())) {
                    user.setHome(mscd.getCode_name());
                    break;
                }
            }

            //セッションにユーザ情報を格納
            setCurrentUser(user);

            //最新の記事取得
            getArticles(user);

            long firstArt = 0;
            //コメントの取得
            for (Article art : articles) {
                if (firstArt == 0) {
                    firstArt = art.getPost_id();
                }
                getComments(art);
            }

            //セッションに設定
            setArticles(articles);
            setAllComments(comments);

            //カレンダーリンク用データ作成
            createCaldata();

            int count = 0;

            //次へボタンはデフォルト非表示
            setNextflg(0);
            setBackflg(0);
            setComNextIndex(0);
            setComBackIndex(0);

            for (Comment com : comments) {

                //記事IDに一致する場合
                if (firstArt == com.getPost_id()) {
                    count = count + 1;

                    //次の表示(5件)を超えた場合
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
        String hashPass = getSha256(password);

        // ユーザ登録処理
        int result = insertUser(hashPass);

        //登録に失敗した場合
        if (result == 0) {
            addActionError(prop.getProperty("adduser.error"));
            return "add_success";
            //登録に成功した場合、マイページに遷移する   
        } else {
            user = getLoginUser();
            //セッションにユーザ情報を格納
            setCurrentUser(user);
            setArticles(articles);
            setAllComments(comments);
            setShowComments(comments);

            //都道府県マスタ取得
            getMasterCodes(TDFCD,mscds);
            setMasterCode(SESSION_TDFCD, mscds);

            //カテゴリを取得
            getMasterCodes(CATCD,categrys);
            setMasterCode(SESSION_CATCD, categrys);
            //ステータス取得
            getMasterCodes(STCD, statuscds);
            setMasterCode(SESSION_STCD, statuscds);            
            
            
            
            return "add_success";
        }
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
    private User getLoginUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        PreparedStatement stmt = null;
        try {
            // ログインSQL
            String sql = "SELECT user_id, username,todo_code,user_img,img_len,img_name,intro_myself FROM users WHERE login_id=? and password=?";

            stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, loginid);
            stmt.setString(2, getSha256(password));
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

            byte[] bytes = rs.getBytes("user_img");
            //中身が存在している場合のみ画像ファイル実施
            if (bytes != null) {
                //画像ファイル作成
                String realPath = ServletActionContext.getServletContext().getRealPath("/" + user.getId());

                File mdir = new File(realPath);
                mdir.mkdir();

                File createFile = new File(mdir, rs.getString("img_name"));

                //BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(createFile));
                FileOutputStream fs = new FileOutputStream(createFile);

                fs.write(bytes);

                img.setFile(createFile);
                img.setFilename(createFile.getName());
                img.setImagesize(rs.getInt("img_len"));
                img.setUser_img(bytes);
                img.setFilefullpath(realPath);
                img.setFilepath("./" + user.getId() + "/" + createFile.getName());
                setCurrentmyImage(img);
            }
            return user;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
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
    private int insertUser(String hashpass) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {
            // 新規登録SQL
            String sql = "INSERT INTO users (login_id,password,create_date) VALUES(?,?,?)";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, loginid);
            stmt.setString(2, hashpass);
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(3, sdf.format(today));

            int rs = stmt.executeUpdate();

            return rs;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (statement != null) {
                statement.close();

            }
        }
    }

    /**
     * 最新の記事取得
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
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? AND "
                    + "post_date = (SELECT MAX(post_date) FROM posts WHERE user_id=?) ORDER BY post_id DESC";

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


    /**
     * マスターデータ取得
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getMasterCodes(String id,ArrayList<MasterCode> mscds) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // カテゴリ取得SQL
            String sql = "SELECT code, code_name FROM mastercode WHERE code_id=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
 
            stmt.setString(1, id);

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

    /**
     * ハッシュ化SHA-256
     *
     * @param target
     * @return
     */
    public static String getSha256(String target) {
        MessageDigest md = null;
        StringBuffer buf = new StringBuffer();
        try {
            md = MessageDigest.getInstance("SHA-256");
            md.update(target.getBytes());
            byte[] digest = md.digest();

            for (int i = 0; i < digest.length; i++) {
                buf.append(String.format("%02x", digest[i]));
            }

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return buf.toString();
    }

    /**
     * カレンダーリンク作成
     *
     * @throws Exception
     */
    private void createCaldata() throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();

        //年月から月末日を取得
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        //初日作成
        cal.set(Calendar.DAY_OF_MONTH, 1);
        String strFirstDay = sdf.format(cal.getTime());

        int last = cal.getActualMaximum(Calendar.DATE);

        //月末作成
        cal.set(Calendar.DAY_OF_MONTH, last);
        String strLastDay = sdf.format(cal.getTime());

        //日付による記事リストを作成
        getArticles(getCurrentUser(), strFirstDay, strLastDay);

        //日付に記事があるかを設定
        for (int i = 1; i <= last; i++) {

            //値を一旦0で設定
            calMap.put(String.valueOf(i), String.valueOf(0));

            for (Article art : calarticles) {

                cal.setTime(art.getPost_date());
                //一致する日付のデータは、日付を値にセットする
                if (i == cal.get(Calendar.DATE)) {
                    calMap.put(String.valueOf(i), String.valueOf(i));
                }
            }
        }
        //セッションに格納
        setCalArticles(calarticles);

        setCalMap(calMap);

        request.setAttribute("calMap", calMap);

    }

    /**
     * 日付から記事取得
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getArticles(User user, String firstday, String lastday) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // ユーザ記事取得SQL
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_date BETWEEN ? AND ?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setLong(1, user.getId());
            stmt.setString(2, firstday);
            stmt.setString(3, lastday);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article article = new Article();
                article.setPost_id(rs.getLong("post_id"));
                article.setPost_date(rs.getDate("post_date"));
                article.setPost_category(rs.getString("post_category"));
                article.setPost_title(rs.getString("post_title"));
                article.setPost(rs.getString("post"));
                article.setPos_status(rs.getString("post_status"));
                calarticles.add(article);
            }

            stmt.close();
        } catch (Exception e) {

            throw e;

        }
    }

}
