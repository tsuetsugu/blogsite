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
    @Result(name="cancel_comment", location="/myPage.jsp"),
    @Result(name="commit_comment", location="/myPage.jsp")
})

public class CommentAction extends AbstractDBAction {

    /**
     * 登録ボタン押下時の処理
     * @return
     * @throws Exception 
     */
    public String execute() throws Exception{
        
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
    
     
}
