/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import model.Item;

public class IndexAction extends ActionSupport {
    
    private String search;

    private ArrayList<Item> items = new ArrayList<Item>();

	// 検索して一覧を取得
    public String show() throws Exception {
        String str = "テストユーザ";
        for(int i = 0;i < 5;i++) {
                Item item = new Item();
                item.setId(Integer.toString(i));
                item.setName(str + Integer.toString(i));

                items.add(item);
            }
        setItems(items);
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
    
}
