/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import static com.opensymphony.xwork2.Action.SUCCESS;
import static constants.Constant.BODER;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.Article;
import model.ArticleCategory;
import model.Comment;
import model.Image;
import model.MasterCode;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author lepra25-pc
 */
public class UserPageAction extends AbstractDBAction {

    private String id;
    private String code;
    private long post_id;
    private int index;
    private ArrayList<MasterCode> mscds = new ArrayList<>();
    private ArrayList<MasterCode> categrys = new ArrayList<>();
    private ArrayList<ArticleCategory> artcs = new ArrayList<>();
    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Article> calarticles = new ArrayList<>();
    private Image img = new Image();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Comment> outcomments = new ArrayList<>();
    private Map<String, String> calMap = new HashMap<>();

    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public ArrayList<MasterCode> getMscds() {
        return mscds;
    }

    //出身地のリスト取得
    public void setMscds(ArrayList<MasterCode> mscds) throws Exception {
        gethome();
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    // ユーザを検索して一覧を取得
    public String show() throws Exception {

        //都道府県マスタ取得
        setMscds(this.mscds);
        setMasterCode("tdfcode", mscds);

        //カテゴリを取得
        getCategrys();

        //最新記事取得用
        ArrayList<Article> articles = new ArrayList<>();
        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<Comment> outcomments = new ArrayList<>();
        //ユーザ情報取得
        User user = new User();

        user = getUser();

        //出身地名を設定
        for (MasterCode mscd : mscds) {
            if (mscd.getCode().equals(user.getTodo_code())) {
                user.setHome(mscd.getCode_name());
                break;
            }
        }

        //カテゴリに該当する記事の件数を取得
        getArticleCategrys(user);
        //カテゴリ一覧を設定
        setArtCats(artcs);

        //表示ユーザを設定する
        setShowtUser(user);

        //最新の記事取得
        articles = getArticles(user);

        //コメント取得
        for (Article art : articles) {
            getComments(art, comments);
        }

        //記事とコメントを設定
        setArticles(articles);
        setAllComments(comments);

        //カレンダーリンク用データ作成
        createCaldata();

        //次へボタンはデフォルト非表示
        setNextflg(0);
        setBackflg(0);
        setComNextIndex(0);
        setComBackIndex(0);

        int count = 0;

        for (Comment com : comments) {

            count = comments.indexOf(com);

            //次の表示を超えた場合
            if (count == BODER) {
                //次へボタン表示
                setNextflg(1);
                setComNextIndex(count);
                break;
            }

            if (count <= BODER) {
                outcomments.add(com);
            }
        }

        //表示用のコメントをセッションに格納
        setShowComments(outcomments);

        return SUCCESS;
    }

    /**
     * コメント次へボタン処理
     *
     * @return
     */
    public String next() {

        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<Comment> outcomments = new ArrayList<>();
        //全体のコメントリスト取得
        comments = getAllComments();

        //次へボタンを非表示、前ボタンを表示
        setNextflg(0);
        setBackflg(1);

        int count = 0;

        //次のIndex取得
        int backIndex = ((index / BODER) - 1) * BODER;
        int currentIndex = index;

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

        return "userpage";
    }

    /**
     * 前ボタン押下時の処理
     *
     * @return
     */
    public String back() {

        ArrayList<Comment> comments = new ArrayList<>();
        ArrayList<Comment> outcomments = new ArrayList<>();

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

        return "userpage";
    }

    /**
     * 初期表示画面移動
     *
     * @return
     */
    public String moveindex() {

        //テンポラリ削除
        DeleteFile del = new DeleteFile();
        if (getCurrentuserImage() != null) {

            if (getCurrentuserImage().getFilefullpath() != null) {

                File file = new File(getCurrentuserImage().getFilefullpath());
                del.delFile(file);
            }
        }

        delSession();

        return "success";
    }

    /**
     * 押下されたカテゴリの記事一覧表示
     *
     * @return
     */
    public String showCatArticles() throws Exception {

        articles.clear();
        User user = new User();
        user = getShowUser();

        //記事取得
        articles = getArticles(user, code);

        //カテゴリ毎の記事をセッションに設定
        setSCatArticles(articles);

        return "success";
    }

    /**
     * カテゴリ記事一覧で押下された記事表示
     *
     * @return
     */
    public String showCatArticle() throws Exception {

        articles.clear();
        comments.clear();
        outcomments.clear();

        ArrayList<Article> showArts = new ArrayList<>();

        //記事取得
        articles = getSCatArticles();

        for (Article article : articles) {

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
     * 都道府県コード取得
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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

    /**
     * カテゴリ取得
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getCategrys() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // カテゴリ取得SQL
            String sql = "SELECT code, code_name FROM mastercode WHERE code_id='CATCD'";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                MasterCode mscd = new MasterCode();
                mscd.setCode(rs.getString("code"));
                mscd.setCode_name(rs.getString("code_name"));
                categrys.add(mscd);
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
            String sql = "SELECT T0.code,T0.code_name,T1.count FROM mastercode AS T0 INNER JOIN (SELECT post_category,Count(post_id) As count from posts WHERE user_id=? AND post_status = '01' group by post_category) AS T1 ON T0.code = T1.post_category WHERE code_id = 'CATCD'";

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
     * ユーザ検索
     *
     * @return
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private User getUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        PreparedStatement stmt = null;
        try {
            // ログインSQL
            String sql = "SELECT user_id, username,todo_code,user_img,img_len,img_name,intro_myself FROM users WHERE user_id=?";

            stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, id);
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

                Date today = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");

                String tmpdir = "/tmp/" + sdf.format(today) + "/" + user.getId();

                logger.error(tmpdir);

                //画像ファイル作成
                String realPath = ServletActionContext.getServletContext().getRealPath(tmpdir);

                File mdir = new File(realPath);
                mdir.mkdirs();

                File createFile = new File(mdir, rs.getString("img_name"));

                //BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(createFile));
                FileOutputStream fs = new FileOutputStream(createFile);

                fs.write(bytes);

                img.setFile(createFile);
                img.setFilename(createFile.getName());
                img.setImagesize(rs.getInt("img_len"));
                img.setUser_img(bytes);
                img.setFilefullpath(realPath);
                img.setFilepath("." + tmpdir + "/" + createFile.getName());

                setCurrentuserImage(img);
            }
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
     * 最新の日付の公開記事取得
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private ArrayList<Article> getArticles(User user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            ArrayList<Article> articles = new ArrayList<>();
            // ユーザ最新記事取得SQL
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_status = '01' and post_date=(SELECT MAX(post_date) FROM posts WHERE user_id=? AND post_status = '01')";

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
            return articles;
        } catch (Exception e) {
            throw e;
        }
    }

    private void getComments(Article art, ArrayList<Comment> comments) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
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

    /**
     * 記事取得(カテゴリ条件)
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private ArrayList<Article> getArticles(User user, String code) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {

            ArrayList<Article> articles = new ArrayList<>();

            // ユーザ記事取得SQL
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_category=? AND post_status='01' ORDER BY post_date DESC, post_id DESC";

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
                articles.add(article);
            }

            stmt.close();

            return articles;

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
        getArticles(getShowUser(), strFirstDay, strLastDay);

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
            String sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_date BETWEEN ? AND ? AND post_status='01'";

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
