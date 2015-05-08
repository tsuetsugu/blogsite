/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionSupport;

public class LoginAction extends ActionSupport {
	private String userid;
	private String password;
        private String errmsg;



	// name:test, password:test の場合のみログイン成功
	public String execute() throws Exception {
		if ("test".equals(getUserid()) && "test".equals(getPassword())) {
			return SUCCESS;
		} else {
                        addActionError("入力内容が不正です。");
			return ERROR;
		}
	}

	public String getUserid() { return userid; }
	public void setUserid(String userid) { this.userid = userid; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
               
}
