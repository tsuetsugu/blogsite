/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 *
 * @author lepra25-pc
 */
/**
 * ユーザ情報を保持します。
 */
public class User {
	
	/**
	 * IDを保持します。
	 */
	private String _id;
	
	/**
	 * ユーザ名を保持します。
	 */
	private String _username;

	
	
	/**
	 * 構築します。
	 */
	public User() {
		_id = null;
		_username = null;
	}
	
	/**
	 * IDを取得します。
	 * @return
	 */
	public String getId() {
		return _id;
	}
	
	/**
	 * IDを設定します。
	 * @param id
	 */
	public void setId(String id) {
		_id = id;
	}
	
	/**
	 * 名前を取得します。
	 * @return
	 */
	public String getUserName() {
		return _username;
	}
	
	/**
	 * 名前を設定します。
	 * @param userName
	 */
	public void setUserName(String userName) {
		_username = userName;
	}
	
}
