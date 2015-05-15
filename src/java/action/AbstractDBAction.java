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
import java.util.ArrayList;
import model.Article;
import model.Comment;
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
     * 現在のユーザを保持するセッション変数を示します。
     */
    protected static final String SESSION_CURRENT_USER = "currentUser";

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
     * エラーメッセージを取得します。
     *
     * @return
     */
    public String getErrorMessage() {
        return _errorMessage;
    }

    /**
     * セッション情報を保存します。
     */
    public void setSession(Map<String, Object> session) {
        _session = session;
    }

    /**
     * 現在のユーザを取得します。
     *
     * @return
     */
    public User getCurrentUser() {
        return (User) _session.get(SESSION_CURRENT_USER);
    }

    /**
     * 現在のユーザを設定します。
     *
     * @param user
     */
    protected void setCurrentUser(User user) {
        _session.put(SESSION_CURRENT_USER, user);
    }

    /**
     * エラーを表示します。
     *
     * @param errorMessage
     * @return
     */
    protected String showError(String errorMessage) {
        _errorMessage = errorMessage;

        return "error";
    }

    /**
     * マスタコードを設定します。
     *
     * @param code_id
     * @param mscds
     *
     */
    protected void setMasterCode(String code_id, ArrayList<MasterCode> mscds) {
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
     * @param code_id
     * @param mscds
     *
     */
    protected void setArticles(ArrayList<Article> articles) {
        _session.put("currentArticles", articles);
    }

    /**
     * 記事(複数)を取得します。
     *
     * @return
     */
    public ArrayList<Article> getArticles() {
        return (ArrayList<Article>) _session.get("currentArticles");
    }       
    
        /**
     * コメントを設定します。
     *
     * @param code_id
     * @param mscds
     *
     */
    protected void setComments(ArrayList<Comment> comments) {
        _session.put("currentComments", comments);
    }

    /**
     * コメントを取得します。
     *
     * @return
     */
    public ArrayList<Comment> getComments() {
        return (ArrayList<Comment>) _session.get("currentComments");
    }  
    
    /**
     * 記事(を設定します。
     *
     * @param code_id
     * @param mscds
     *
     */
    protected void setArticle(Article article) {
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
     * @param code_id
     * @param mscds
     *
     */
    protected void setPostId(long post_id) {
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

        String url = "jdbc:mysql://localhost:3306/blog?user=root&password=lepra&"
                + "useUnicode=true&characterEncoding=utf8&useServerPrepStmts=true";

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
