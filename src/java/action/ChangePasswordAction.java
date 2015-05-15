/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import static action.PropertiesWithUtf8.loadUtf8Properties;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.Properties;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 *
 * @author lepra25-pc
 */

@Results({
    @Result(name="change_success", location="/result.jsp"),
    @Result(name="error",location="/chPass.jsp"),
    @Result(name="cancel", location="/login.jsp")
})

public class ChangePasswordAction extends AbstractDBAction{
    private String loginid;
    private String password;
    
    /** Logger. */
    private static Logger logger = Logger.getLogger(IndexAction.class);
        
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
    
    /**
     * パスワード変更処理
     * @return
     * @throws Exception 
     */
    
    @Action("/changePass")
    public String execute() throws Exception {
        
        //メッセージプロパティ取得
        Properties prop = loadUtf8Properties("/message.properties");
        
        //ログインユーザの存在確認
        User user = getRegistUser();
        
        //存在しない場合はエラーメッセージを表示
        if(user == null){
           addActionError(MessageFormat.format(prop.getProperty("chpass.exist"),loginid));
           return "error";
        }
        
        //存在する場合は、パスワード変更処理
        int result = updatePassword(user);
        
        //変更が完了したら、結果画面へ遷移
        if(result == 0){
            addActionError(prop.getProperty("chpass.error"));
            return "error";
        }else{
            addActionError(prop.getProperty("chpass.success"));
            return "change_success";            
        }

    }
    
    /**
     * キャンセルボタン押下時の処理
     * @return
     * @throws Exception 
     */
    @Action("/cancel")
    public String cansel() throws Exception {
        return "cancel";
    }
    
        /**
         * 登録済みユーザ検索
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
        
    private int updatePassword(User user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Statement statement = null;
		try {
                        // パスワード初期化SQL
                        String sql = "UPDATE users SET password=? WHERE user_id=?";
                    
                        PreparedStatement stmt = getConnection().prepareStatement(sql);
			// SQL実行
                        stmt.setString(1, password);
                        stmt.setLong(2, user.getId());

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
}
