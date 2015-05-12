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
import model.Item;
import model.User;
import org.apache.log4j.Logger;

public class IndexAction extends AbstractDBAction {

    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);
    private String search;

    private ArrayList<Item> items = new ArrayList<Item>();

    // 検索して一覧を取得
    public String show() throws Exception {
        String str = "テストユーザ";
        for (int i = 0; i < 5; i++) {
            Item item = new Item();
            item.setId(Integer.toString(i));
            item.setName(str + Integer.toString(i));

            items.add(item);
        }
        setItems(items);
        logger.error("[IndexAction]Errorログ");
        return SUCCESS;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String serch) {
        this.search = serch;
    }

    private void getUser() throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // ログインSQL
            String sql = "SELECT user_id, username FROM users WHERE username=?";
            
            Connection con = getConnection();
            
            PreparedStatement stmt = con.prepareStatement(sql);
            // SQL実行
            stmt.setString(1, search);
            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                
            }


            stmt.close();
            con.close();
         } catch (Exception e) {
            
            throw e;
 
        }
    }

}
