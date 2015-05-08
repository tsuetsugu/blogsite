/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionSupport;

public class indexAction extends ActionSupport {
	private String name;
	private String password;

	// 検索して一覧を取得
	public String execute() throws Exception {
		
			return SUCCESS;
	}

	public String getName() { return name; }
	public void setName(String name) { this.name = name; }

	public String getPassword() { return password; }
	public void setPassword(String password) { this.password = password; }
}
