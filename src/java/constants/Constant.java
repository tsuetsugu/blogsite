/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package constants;

/**
 *
 * @author lepra25-pc
 */
public class Constant {
    private Constant(){
        
    }
    
    public static final String CATCD = "CATCD"; //カテゴリ検索用文字列
    public static final String SESSION_CATCD = "categorycode";
    public static final String STCD = "STCD";   //ステータス検索用文字列
    public static final String SESSION_STCD = "statuscode";
    public static final String TDFCD = "TDFCD"; //都道府県検索用文字列
    public static final String SESSION_TDFCD = "tdfcode";
    
    public static final int BODER = 5; //記事表示件数境界値
    public static final String OPEN = "01"; //記事ステータス「公開」
    
    public static final String SESSION_CURARTS = "currentArticles"; //表示する記事
    public static final String SESSION_SHOWUSER = "showuser"; //表示する記事
}
