/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;


import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author lepra25-pc
 */
@WebServlet("/imagefile")
public class ImageFileServlet extends HttpServlet {
private static final long serialVersionUID = 1L;

    public ImageFileServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //出力するファイル(あらかじめWebContent以下に配置しておく。)
        String dir = getServletContext().getRealPath("/");
        String fname = request.getParameter("name");

        int iData = 0;

        //ServletのOutputStream取得
        ServletOutputStream out = response.getOutputStream();

        //画像ファイルをBufferedInputStreamを使用して読み取る
        BufferedInputStream in = new BufferedInputStream(new FileInputStream(dir+fname));

        //画像を書き出す
        while((iData = in.read()) != -1){
            out.write(iData);
        }

        in.close();
        out.close();
    }    
}
