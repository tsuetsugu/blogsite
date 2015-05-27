/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import static constants.Constant.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import model.Article;
import model.Comment;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;

@Results({
    @Result(name = "mypage", location = "/myPage.jsp"),
    @Result(name = "userpage", location = "/userPage.jsp"),})

/**
 *
 * @author lepra25-pc
 */
public class CalendarAction extends AbstractDBAction {

    /**
     * Logger.
     */
    private static Logger logger = Logger.getLogger(IndexAction.class);
    private ArrayList<Article> articles = new ArrayList<>();
    private ArrayList<Article> calarticles = new ArrayList<>();
    private Map<String, String> calMap = new HashMap<>();
    private ArrayList<Comment> comments = new ArrayList<>();
    private ArrayList<Comment> outcomments = new ArrayList<>();
    private int year;
    private int month;
    private int day;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public Map<String, String> getCalMap() {
        return calMap;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    /**
     * 月変更ボタン押下時の処理
     *
     * @return
     * @throws Exception
     */
    @Action("/chmonth")
    public String chmonth() throws Exception {

        boolean meflg = false;

        calMap.clear();
        articles.clear();
        comments.clear();
        outcomments.clear();

        HttpServletRequest request = ServletActionContext.getRequest();

        //年月の受け渡しの年跨ぎ対応
        if (month == 0) {
            year = year - 1;
            month = month + 1;
        } else if (month == 13) {
            year = year + 1;
            month = month - 1;
        }

        //年月から月末日を取得
        Calendar cal = Calendar.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");

        //初日作成
        cal.set(year, month - 1, 1);
        String strFirstDay = sdf.format(cal.getTime());

        int last = cal.getActualMaximum(Calendar.DATE);

        //月末作成
        cal.set(year, month - 1, last);
        String strLastDay = sdf.format(cal.getTime());

        //ログインユーザが空の場合は、選択しているユーザで検索
        if (getCurrentUser() == null) {

            getArticles(getShowUser(), strFirstDay, strLastDay, OPEN);
        } else {
            meflg = true;
            //日付による記事リストを作成
            getArticles(getCurrentUser(), strFirstDay, strLastDay, "");
        }
        long firstArt = 0;

        logger.error("記事数:" + calarticles.size());

        //記事がない場合
        if (calarticles.isEmpty()) {
            for (int i = 1; i <= last; i++) {

                calMap.put(String.valueOf(i), String.valueOf(0));
            }
        }
        for (int i = 1; i <= last; i++) {
            calMap.put(String.valueOf(i), String.valueOf(0));
            //日付に記事があるかを設定
            for (Article art : calarticles) {

                //最初の記事だけ対象
                if (firstArt == 0) {
                    firstArt = art.getPost_id();
                    getComments(art);
                }
                cal.setTime(art.getPost_date());

                if (i == cal.get(Calendar.DATE)) {
                    
                    calMap.put(String.valueOf(i), String.valueOf(i));

                }
            }
        }
        //セッションに格納
        setCalArticles(calarticles);
        setArticles(calarticles);
        setAllComments(comments);

        int count = 0;

        //次へボタンはデフォルト非表示
        setNextflg(0);
        setBackflg(0);
        setComNextIndex(0);
        setComBackIndex(0);

        for (Comment com : comments) {

            if (firstArt == com.getPost_id()) {
                count = count + 1;

                //次の表示を超えた場合
                if (count > BODER) {
                    //次へボタン表示
                    setNextflg(1);
                    setComNextIndex(count - 1);
                    break;
                }

                if (count <= BODER) {
                    outcomments.add(com);
                }
            }

        }

        //表示用のコメントをセッションに格納
        setShowComments(outcomments);

        setCalMap(calMap);

        request.setAttribute("calMap", calMap);

        if (meflg) {
            return "mypage";
        } else {
            return "userpage";
        }
    }

    /**
     * カレンダの日付をクリックした場合の処理
     *
     * @return
     * @throws Exception
     */
    @Action("/selectDay")
    public String selectDay() throws Exception {

        boolean meflg = false;

        if (getCurrentUser() != null) {
            meflg = true;
        }

        HttpServletRequest request = ServletActionContext.getRequest();

        ArrayList<Article> dayarticles = new ArrayList<>();
        ArrayList<Article> tmparticles = new ArrayList<>();
        ArrayList<Comment> tmpcomments = new ArrayList<>();

        comments.clear();

        Calendar cal = Calendar.getInstance();

        tmparticles = getCalArticles();
        tmpcomments = getAllComments();

        logger.error(tmpcomments.size());

        long firstArt = 0;

        for (Article art : tmparticles) {
            cal.setTime(art.getPost_date());

            if (cal.get(Calendar.DATE) == day) {
                dayarticles.add(art);

                if (firstArt == 0) {
                    firstArt = art.getPost_id();
                }

                getComments(art);
            }
        }

        setAllComments(comments);
        setArticles(dayarticles);
        int count = 0;

        //次へボタンはデフォルト非表示
        setNextflg(0);
        setBackflg(0);
        setComNextIndex(0);
        setComBackIndex(0);

        for (Comment com : comments) {

            if (firstArt == com.getPost_id()) {
                count = count + 1;

                //次の表示を超えた場合
                if (count > BODER) {
                    //次へボタン表示
                    setNextflg(1);
                    setComNextIndex(count - 1);
                    break;
                }

                if (count <= BODER) {
                    outcomments.add(com);
                }

            }

        }

        //表示用のコメントをセッションに格納
        setShowComments(outcomments);

        //画面に渡すパラメータ
        request.setAttribute("calMap", super.getCalMap());

        if (meflg) {
            return "mypage";
        } else {
            return "userpage";
        }
    }

    /**
     * 日付から記事取得
     *
     * @param user
     * @throws SQLException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    private void getArticles(User user, String firstday, String lastday, String status) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {

            PreparedStatement stmt = null;
            Connection con = getConnection();

            String sql = null;
            if (OPEN.equals(status)) {
                sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_date BETWEEN ? AND ? AND post_status=? ORDER BY post_date DESC,post_id DESC";
                stmt = con.prepareStatement(sql);
                stmt.setString(4, status);
            } else {
                sql = "SELECT post_id, post_date,post_category,post_title,post,post_status FROM posts WHERE user_id=? and post_date BETWEEN ? AND ? ORDER BY post_date DESC,post_id DESC";
                stmt = con.prepareStatement(sql);

            }

            stmt.setLong(1, user.getId());
            stmt.setString(2, firstday);
            stmt.setString(3, lastday);

            // ユーザ記事取得SQL
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Article article = new Article();
                article.setPost_id(rs.getLong("post_id"));
                article.setPost_date(rs.getDate("post_date"));
                article.setPost_category(rs.getString("post_category"));
                article.setPost_title(rs.getString("post_title"));
                article.setPost(rs.getString("post"));
                article.setPos_status(rs.getString("post_status"));
                calarticles.add(article);
            }

            stmt.close();
        } catch (Exception e) {

            throw e;

        }
    }

    private void getComments(Article art) throws SQLException, ClassNotFoundException, InstantiationException, IllegalAccessException {
        try {
            // 記事のコメント取得SQL
            String sql = "SELECT comment_id, post_id,username,create_date,comment FROM comments WHERE post_id=?";

            Connection con = getConnection();

            PreparedStatement stmt = con.prepareStatement(sql);
            logger.error(art.getPost_id());
            stmt.setLong(1, art.getPost_id());

            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Comment comment = new Comment();
                comment.setComment_id(rs.getLong("comment_id"));
                comment.setPost_id(rs.getLong("post_id"));
                comment.setUsername(rs.getString("username"));
                comment.setCreate_date(rs.getDate("create_date"));
                comment.setComment(rs.getString("comment"));
                comments.add(comment);
            }

            stmt.close();
        } catch (Exception e) {

            e.getStackTrace();

        }
    }

}
