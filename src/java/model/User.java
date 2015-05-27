/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;

/**
 *
 * @author lepra25-pc
 */
/**
 * ユーザ情報を保持します。
 */
public class User implements Serializable {

    // Serial Version UID を付ける
    private static final long serialVersionUID = 1L;
    
    /**
     * IDを保持します。
     */
    private long _id;

    /**
     * ユーザ名を保持します。
     */
    private String _username;

    /**
     * 都道府県コードを保持
     */
    private String _todo_code;

    /**
     * 出身地名を保持
     */
    private String _home;
    
    /**
     * 自己紹介を保持
     */
    private String _intro_myself;

    /**
     * 構築します。
     */
    public User() {
        _id = 0;
        _username = null;
        _todo_code = null;
        _intro_myself = null;
    }

    /**
     * IDを取得します。
     *
     * @return
     */
    public long getId() {
        return _id;
    }

    /**
     * IDを設定します。
     *
     * @param id
     */
    public void setId(long id) {
        _id = id;
    }

    public String getUsername() {
        return _username;
    }

    public void setUsername(String _username) {
        this._username = _username;
    }

    
    public String getTodo_code() {
        return _todo_code;
    }

    public void setTodo_code(String _todo_code) {
        this._todo_code = _todo_code;
    }

    public String getIntro_myself() {
        return _intro_myself;
    }

    public void setIntro_myself(String _intro_myself) {
        this._intro_myself = _intro_myself;
    }

    public String getHome() {
        return _home;
    }

    public void setHome(String _home) {
        this._home = _home;
    }

    
    
}
