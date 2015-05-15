/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionSupport;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import model.User;
import org.apache.log4j.Logger;

public class IndexAction extends AbstractDBAction {

    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);
    private String search;

    private ArrayList<User> users = new ArrayList<>();

    // 検索して一覧を取得
    public String show() throws Exception {
        
        //ユーザ一覧取得
        getUser();        
        setUsers(users);
        return SUCCESS;
    }

     public String getSearch() {
        return search;
    }

    public void setSearch(String serch) {
        this.search = serch;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }    
    
    
    private void getUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // ログインSQL
            String sql = "SELECT user_id, username FROM users WHERE username LIKE ?";
            
            Connection con = getConnection();
            
            //ワイルドカード
            String searchStr = "%" + search + "%";
            
            PreparedStatement stmt = con.prepareStatement(sql);
            // SQL実行
            stmt.setString(1, searchStr);
            ResultSet rs = stmt.executeQuery();
            
            User user = new User();
            
            while(rs.next()){
                user.setId(rs.getLong("user_id"));
                user.setUsername(rs.getString("username"));
                
                users.add(user);
            }

            stmt.close();
            con.close();
         } catch (Exception e) {
            
            throw e;
 
        }
    }

}
