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
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.MasterCode;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * プロフィール画面での処理
 *
 * @author lepra25-pc
 */
@Results({
    @Result(name = "cancel_profile", location = "/myPage.jsp"),
    @Result(name = "commit_profile", location = "/myPage.jsp"),
    @Result(name = "success", location = "/profile.jsp")
})

public class ProfileAction extends AbstractDBAction {

    private ArrayList<MasterCode> mscds = new ArrayList<>();
    private static Logger logger = Logger.getLogger(IndexAction.class);
    private String username;
    private String home;
    private String myself;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getMyself() {
        return myself;
    }

    public void setMyself(String myself) {
        this.myself = myself;
    }

    @Action("/show")
    public String show() throws Exception {

        //mscds = getMasterCode("tdfcode");
        //セッションがない場合のみ実行
        //if(mscds.isEmpty()){
        setMscds(this.mscds);
        setMasterCode("tdfcode", mscds);
        //}

        return "success";
    }

    /**
     * 更新ボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    @Action("/update")
    public String update() throws Exception {

        //現在のユーザ取得
        User user = getCurrentUser();

        //更新処理
        int result = updateProfile(user);

        //更新に失敗した場合
        if (result == 0) {

        } else {
            //更新に成功した場合ユーザのセッション情報を書き換える
            user.setUsername(username);
            user.setTodo_code(home);
            user.setIntro_myself(myself);
            
            logger.error(home);
            
           
            //出身地名を設定
            for (MasterCode mscd : mscds) {
                logger.error(mscd.getCode() + ":" + home);
                if (home.equals(mscd.getCode())) {
                    logger.error(mscd.getCode() + ":" + home);
                    user.setHome(mscd.getCode_name());
                    break;
                }
            }

            setCurrentUser(user);
        }

        return "commit_profile";
    }

    /**
     * キャンセルボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    @Action("/cancel")
    public String cancel() throws Exception {
        return "cancel_profile";
    }

    /**
     * 画像アップロードボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    public String upload() throws Exception {
        return "upload_image";
    }

    public ArrayList<MasterCode> getMscds() {
        return mscds;
    }

    //出身地のリスト取得
    public void setMscds(ArrayList<MasterCode> mscds) throws Exception {
        gethome();

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
            con.close();
        } catch (Exception e) {

            throw e;

        }
    }

    private int updateProfile(User user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        Statement statement = null;
        try {
            // ユーザ情報更新
            String sql = "UPDATE users SET username=?,todo_code=?, intro_myself=?, update_date=? WHERE user_id=?";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            // SQL実行
            stmt.setString(1, username);
            stmt.setString(2, home);
            stmt.setString(3, myself);
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(4, sdf.format(today));
            stmt.setLong(5, user.getId());

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
