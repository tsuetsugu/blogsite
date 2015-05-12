/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * 記事登録編集画面での処理
 * @author lepra25-pc
 */

@Results({
    @Result(name="cancel_article", location="/myPage.jsp"),
    @Result(name="commit_article", location="/myPage.jsp")
})

public class ArticleEditAction extends AbstractDBAction {

    /**
     * 更新ボタン押下時の処理
     * @return
     * @throws Exception 
     */
    public String execute() throws Exception{
        
        
        
        //記事登録の場合
        
        
        //記事編集の場合
        
        //更新処理
        return "commit_article";
    }
    
    /**
     * キャンセルボタン押下時の処理
     * @return
     * @throws Exception 
     */
    
    public String cancel() throws Exception{
        return "cancel_article";
    }
    
     
}
