/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

/**
 * プロフィール画面での処理
 * @author lepra25-pc
 */

@Results({
    @Result(name="cancel_profile", location="/myPage.jsp"),
    @Result(name="commit_profile", location="/myPage.jsp")
})

public class ProfileAction extends AbstractDBAction {

    /**
     * 更新ボタン押下時の処理
     * @return
     * @throws Exception 
     */
    public String execute() throws Exception{
        
        //更新処理
        return "commit_profile";
    }
    
    /**
     * キャンセルボタン押下時の処理
     * @return
     * @throws Exception 
     */
    
    public String cancel() throws Exception{
        return "cancel_profile";
    }
    
    /**
     * 画像アップロードボタン押下時の処理
     * @return
     * @throws Exception 
     */
    public String upload() throws Exception{
        return "upload_image";
    }
    
}
