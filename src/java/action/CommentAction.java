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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Article;
import model.Comment;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * コメント登録編集画面での処理
 * @author lepra25-pc
 */

@Results({
    @Result(name="cancel_comment", location="/myPage.jsp"),
    @Result(name="commit_comment", location="/myPage.jsp")
})

public class CommentAction extends AbstractDBAction {
    private long post_id;
    private String post_title;
    private String userName;
    private String comment;
    private ArrayList<Comment> comments = new ArrayList<>();
    

        /** Logger. */
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

    
    public String doInit(){
        setPostId(post_id);
              
        
        return "success";
    }
    
    
    
    /**
     * 登録ボタン押下時の処理
     * @return
     * @throws Exception 
     */
    @Action("/addComment")
    public String addComment() throws Exception{
        
        setPost_id(getPostId());
        
        //コメント登録
        insertComment(post_id);
        
        //セッションのコメントを編集
        comments = getComments();
        
        //記事の最新コメントを再度抽出して置き換え
        getComments(post_id);
        setComments(comments);
        
        //登録処理
        return "commit_comment";
    }
    
    /**
     * キャンセルボタン押下時の処理
     * @return
     * @throws Exception 
     */
    
    public String cancel() throws Exception{
        return "cancel_comment";
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
            stmt.setString(2,userName) ;
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
