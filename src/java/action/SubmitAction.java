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

@Results({
    @Result(name="login_success", location="/myPage.jsp"),
    @Result(name="login_error", location="/login.jsp"),
    @Result(name="add_success", location="/result.jsp"),
    @Result(name ="input", location="/login.jsp")
})
public class SubmitAction extends AbstractDBAction{
	private String loginid;
	private String password;


        	/** Logger. */
	private static Logger logger = Logger.getLogger(IndexAction.class);
        

    /**
     * バリデーション
     */
    public void validate() {

        if ( loginid == null || loginid.length() == 0 ) {
            addActionError("ログインIDを入力してください");
        }
        if ( password==null || password.length() == 0 ) {
            addActionError("パスワードを入力してください");
        }
    }        
        
        /**
         * ログイン処理
         * @return
         * @throws Exception 
         */
        @Action("/login")
	public String login() throws Exception {
            
            //メッセージプロパティ取得
            Properties prop = loadUtf8Properties("/message.properties");
            
            User user = getLoginUser();
            
            logger.error("login");           
            if (user != null) {
		return "login_success";
            } else {
                addActionError(prop.getProperty("login.error"));
                    logger.error("login");
                    return "login_error";
            }
            
            //return SUCCESS;
	}     

        /**
         * 新規登録処理
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
                addActionError(MessageFormat.format(prop.getProperty("adduser.exist"),loginid));
		return "login_error";
            }
            
            //パスワードをハッシュ化
            
            // ユーザ登録処理
            int result = insertUser();
            
            //登録に失敗した場合
            if(result == 0){
               addActionError(prop.getProperty("adduser.error")); 
               return "add_success";
            //登録に成功した場合、メッセージを出力し結果画面へ遷移する   
            }else{
               addActionError(MessageFormat.format(prop.getProperty("adduser.success"),loginid));
               return "add_success"; 
            }
	}

	public String getLoginid() { return loginid; }
	public void setLoginid(String loginid) { this.loginid = loginid; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
        
        /**
         * ログインユーザ検索
         * @return
         * @throws SQLException
         * @throws ClassNotFoundException
         * @throws InstantiationException
         * @throws IllegalAccessException 
         */
        private User getLoginUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
		Statement statement = null;
		try {
                        // ログインSQL
                        String sql = "SELECT user_id, username FROM users WHERE login_id=? and password=?";
                    
                        PreparedStatement stmt = getConnection().prepareStatement(sql);
			// SQL実行
                        stmt.setString(1, loginid);
                        stmt.setString(2, password);
                        ResultSet rs = stmt.executeQuery();

			boolean br = rs.first();
			if (br == false) {
				return null;
			}
			User user = new User();
			user.setId(rs.getString("user_id"));
			user.setUserName(rs.getString("username"));

			return user;
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
			user.setId(rs.getString("user_id"));
			user.setUserName(rs.getString("username"));

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
        
}
