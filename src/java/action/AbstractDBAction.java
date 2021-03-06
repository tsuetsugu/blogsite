package action;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author lepra25-pc
 */
/**
 * DBアクセスするアクションの抽象クラス。
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Map;

import org.apache.struts2.interceptor.SessionAware;

import com.opensymphony.xwork2.ActionSupport;
import static constants.Constant.*;
import java.util.ArrayList;
import model.Article;
import model.ArticleCategory;
import model.Comment;
import model.Image;
import model.MasterCode;
import model.User;

/**
 * DBアクセスするアクションの抽象クラス。
 */
public abstract class AbstractDBAction extends ActionSupport
        implements SessionAware {

    /**
     * シリアルバージョンUID。
     */
    private static final long serialVersionUID = 1L;


    /**
     * コネクションプール。
     */
    private Connection _pooledConnection;

    /**
     * エラーメッセージを保持します。
     */
    private String _errorMessage;

    /**
     * セッション情報を保持します。
     */
    private Map<String, Object> _session;

    /**
     * 構築します。
     */
    public AbstractDBAction() {
        _pooledConnection = null;
        _errorMessage = null;
        _session = null;
    }


    /**
     * セッション情報を保存します。
     * @param session
     */
    @Override
    public void setSession(Map<String, Object> session) {
        _session = session;
    }

    /**
     * 現在のログインユーザを取得します。
     *
     * @return
     */
    public User getCurrentUser() {
        return (User) _session.get(SESSION_CURRENT_USER);
    }

    /**
     * 現在のログインユーザを設定します。
     *
     * @param user
     */
    public void setCurrentUser(User user) {
        _session.put(SESSION_CURRENT_USER, user);
    }

    /**
     * 表示しているユーザを取得します。
     *
     * @return
     */
    public User getShowUser() {
        return (User) _session.get(SESSION_SHOW_USER);
    }

    /**
     * 表示しているユーザを設定します。
     *
     * @param user
     */
    public void setShowtUser(User user) {
        _session.put(SESSION_SHOW_USER, user);
    }


    /**
     * マスタコードを設定します。
     *
     * @param code_id
     * @param mscds
     *
     */
    public void setMasterCode(String code_id, ArrayList<MasterCode> mscds) {
        _session.put(code_id, mscds);
    }

    /**
     * マスタコードを取得します。
     *
     * @return
     */
    public ArrayList<MasterCode> getMasterCode(String code_id) {
        return (ArrayList<MasterCode>) _session.get(code_id);
    }

    /**
     * 記事(複数)を設定します。
     *
     * @param articles
     *
     */
    public void setArticles(ArrayList<Article> articles) {
        _session.put(SESSION_CURARTS, articles);
    }

    /**
     * 記事(複数)を取得します。
     *
     * @return
     */
    public ArrayList<Article> getArticles() {
        return (ArrayList<Article>) _session.get(SESSION_CURARTS);
    }

    /**
     * 記事(カテゴリ毎)を設定します。
     *
     * @param articles
     *
     */
    public void setSCatArticles(ArrayList<Article> articles) {
        _session.put("catArticles", articles);
    }

    /**
     * 記事(カテゴリ毎)を取得します。
     *
     * @return
     */
    public ArrayList<Article> getSCatArticles() {
        return (ArrayList<Article>) _session.get("catArticles");
    }

    /**
     * 表示コメントを設定します。
     *
     * @param comments
     *
     */
    public void setShowComments(ArrayList<Comment> comments) {
        _session.put("showComments", comments);
    }

    /**
     * 表示コメントを取得します。
     *
     * @return
     */
    public ArrayList<Comment> getShowComments() {
        return (ArrayList<Comment>) _session.get("showComments");
    }

    /**
     * 取得したコメントを設定します。
     *
     * @param comments
     *
     */
    public void setAllComments(ArrayList<Comment> comments) {
        _session.put("allComments", comments);
    }

    /**
     * 取得したコメントを取得します。
     *
     * @return
     */
    public ArrayList<Comment> getAllComments() {
        return (ArrayList<Comment>) _session.get("allComments");
    }

    /**
     * 記事設定します。
     *
     * @param article
     *
     */
    public void setArticle(Article article) {
        _session.put("currentArticle", article);
    }

    /**
     * 記事(複数)を取得します。
     *
     * @return
     */
    public Article getArticle() {
        return (Article) _session.get("currentArticle");
    }

    /**
     * 記事IDを設定します。
     *
     * @param post_id
     *
     */
    public void setPostId(long post_id) {
        _session.put("currentPostid", post_id);
    }

    /**
     * 記事IDを取得します。
     *
     * @return
     */
    public long getPostId() {
        return (long) _session.get("currentPostid");
    }

    /**
     * カテゴリごとの記事件数一覧
     *
     * @param artCats
     *
     */
    protected void setArtCats(ArrayList<ArticleCategory> artCats) {
        _session.put("artCategorys", artCats);
    }

    /**
     * カテゴリごとの記事件数一覧
     *
     * @return
     */
    public ArrayList<ArticleCategory> getArtCats() {
        return (ArrayList<ArticleCategory>) _session.get("artCategorys");
    }

    /**
     * 特定セッションの削除
     *
     * @param key
     */
    public void delSession(String key) {
        _session.remove(key);
    }

    /**
     * 現在の自分プロフィール画像
     *
     * @param image
     *
     */
    public void setCurrentmyImage(Image image) {
        _session.put("currentprofileimage", image);
    }

    /**
     * 現在の自分プロフィール画像
     *
     * @return
     */
    public Image getCurrentmyImage() {
        return (Image) _session.get("currentprofileimage");
    }

    /**
     * 現在のユーザプロフィール画像
     *
     * @param image
     *
     */
    public void setCurrentuserImage(Image image) {
        _session.put("currentuserprofileimage", image);
    }

    /**
     * 現在のユーザプロフィール画像
     *
     * @return
     */
    public Image getCurrentuserImage() {
        return (Image) _session.get("currentuserprofileimage");
    }

    /**
     * アップロードのプロフィール画像
     *
     * @param image
     *
     */
    public void setUploadImage(Image image) {
        _session.put("uploadprofileimage", image);
    }

    /**
     * アップロードのプロフィール画像
     *
     * @return
     */
    public Image getUploadImage() {
        return (Image) _session.get("uploadprofileimage");
    }

    public int getComBackIndex() {
        return (int) _session.get("backindex");
    }

    public void setComBackIndex(int index) {
        _session.put("backindex", index);
    }

    public int getComNextIndex() {
        return (int) _session.get("nextindex");
    }

    public void setComNextIndex(int index) {
        _session.put("nextindex", index);
    }

    /**
     * コメント次へ表示のためのフラグ
     *
     * @return
     */
    public int getNextflg() {
        return (int) _session.get("nextflg");
    }

    public void setNextflg(int flg) {
        _session.put("nextflg", flg);
    }

    /**
     * コメント前へ表示のためのフラグ
     *
     * @return
     */
    public int getBackflg() {
        return (int) _session.get("backflg");
    }

    public void setBackflg(int flg) {
        _session.put("backflg", flg);
    }

    /**
     * カレンダーの記事リスト
     *
     * @return
     */
    public ArrayList<Article> getCalArticles() {
        return (ArrayList<Article>) _session.get("calarticles");
    }

    public void setCalArticles(ArrayList<Article> calart) {
        _session.put("calarticles", calart);
    }

    /**
     * カレンダーの記事リスト
     *
     * @return
     */
    public Map<String, String> getCalMap() {
        return (Map<String, String>) _session.get("calmap");
    }

    public void setCalMap(Map<String, String> calmap) {
        _session.put("calmap", calmap);
    }

    /**
     * セッションクリア
     *
     * @return
     */
    public void delSession() {
        _session.clear();
    }

    /**
     * 接続オブジェクトを生成します。
     *
     * @return
     * @throws SQLException
     */
    protected Connection getConnection() throws SQLException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        // Connectionの準備
        if (_pooledConnection != null) {
            return _pooledConnection;
        }

        String url = DB_URL;

        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            // 下準備
            _pooledConnection = DriverManager.getConnection(url);
            return _pooledConnection;
        } catch (ClassNotFoundException e) {
            _pooledConnection = null;
            throw e;
        } catch (SQLException e) {
            _pooledConnection = null;
            throw e;
        }
    }

}
