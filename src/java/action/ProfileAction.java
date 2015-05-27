/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import model.Image;
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
        
        setUploadImage(getCurrentmyImage());
        
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
            
            //都道府県コード取得
            mscds = getMasterCode("tdfcode");
            
           
            //出身地名を設定
            for (MasterCode mscd : mscds) {
                if (home.equals(mscd.getCode())) {
                    user.setHome(mscd.getCode_name());
                    break;
                }
            }
            
            Image img = new Image();
            img.setFile(getUploadImage().getFile());
            img.setFilename(getUploadImage().getFilename());
            img.setFilepath(getUploadImage().getFilepath());
            img.setImagesize(getUploadImage().getImagesize());
            img.setUser_img(getUploadImage().getUser_img());
            
            setCurrentmyImage(img);
            
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
    public String cancelporofile() throws Exception {
        delSession("uploadprofileimage");
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

    private int updateProfile(User user) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException,IOException{
        Statement statement = null;
        try {
            // ユーザ情報更新
            String sql = "UPDATE users SET username=?,todo_code=?, user_img=?,img_len=?,img_name=?,intro_myself=?, update_date=? WHERE user_id=?";

            PreparedStatement stmt = getConnection().prepareStatement(sql);
            
        //セッションから画像ファイル情報取得
            Image img = new Image();
            img = getUploadImage();
            
            
            // SQL実行
            stmt.setString(1, username);
            stmt.setString(2, home);
            stmt.setBinaryStream(3,new FileInputStream(img.getFile()),img.getImagesize());
            stmt.setInt(4,img.getImagesize());
            stmt.setString(5,img.getFilename());
            stmt.setString(6, myself);
            Date today = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            stmt.setString(7, sdf.format(today));
            stmt.setLong(8, user.getId());

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
