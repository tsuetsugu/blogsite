/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import static constants.Constant.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Comment;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * コメント登録編集画面での処理
 *
 * @author lepra25-pc
 */
@Results({
    @Result(name = "cancel_comment", location = "/myPage.jsp"),
    @Result(name = "commit_comment", location = "/myPage.jsp"),
    @Result(name = "user_commit_comment", location = "/userPage.jsp"),
    @Result(name = "user_cancel_comment", location = "/userPage.jsp"),
    @Result(name = "input", location = "/comment.jsp"),})

public class CommentAction extends AbstractDBAction {

    //private long post_id;
    private long post_id;
    private String post_title;
    private String userName;
    private String comment;
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Comment> outcomments = new ArrayList<>();
    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public long getPost_id() {
        return post_id;
    }

    public void setPost_id(long post_id) {
        this.post_id = post_id;
    }

    public String getPost_title() {
        return post_title;
    }

    public void setPost_title(String post_title) {
        this.post_title = post_title;
    }

    public String doInit() {

        logger.error("post_id");

        setPostId(post_id);

        return "comment";
    }

    public String userAddComment() {

        logger.error("useraddcomment");

        setPostId(post_id);

        return "comment";
    }

    /**
     * 登録ボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    @Action("/addComment")
    public String addComment() throws Exception {

        //setPost_id(getPostId());
        //コメント登録
        insertComment(getPostId());

        //記事の最新コメントを再度抽出して置き換え
        getComments(getPostId());
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

        if (getShowUser() == null) {
            return "commit_comment";
        }

        //登録処理
        return "user_commit_comment";
    }

    /**
     * キャンセルボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    public String cancel() throws Exception {

        if (getShowUser() == null) {
            return "cancel_comment";

        }

        return "user_cancel_comment";
    }

    private int insertComment(long id) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        PreparedStatement stmt = null;
        try {

            String sql = "";
            // 新規登録SQL
            sql = "INSERT INTO comments (post_id,username,create_date,comment) VALUES(?,?,?,?)";

            stmt = getConnection().prepareStatement(sql);
            // SQL実行

            stmt.setLong(1, id);
            stmt.setString(2, userName);
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(3, sdf.format(today));
            stmt.setString(4, comment);

            int rs = stmt.executeUpdate();

            return rs;
        } catch (SQLException e) {
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
                stmt = null;
            }
        }
    }

    private void getComments(long id) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // 記事のコメント取得SQL
            String sql = "SELECT comment_id, post_id,username,create_date,comment FROM comments WHERE post_id=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setLong(1, id);

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
