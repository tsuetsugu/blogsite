/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.File;
import org.apache.log4j.Logger;

/**
 *
 * @author lepra25-pc
 */
public class DeleteFile {

    private static Logger logger = Logger.getLogger(IndexAction.class);
    /**
     *
     * @param f
     */
    public void delFile(File f) {
        /*
         * ファイルまたはディレクトリが存在しない場合は何もしない
         */
        if (f.exists() == false) {
            logger.error("何もしない");
            return;
        }

        if (f.isFile()) {
            /*
             * ファイルの場合は削除する
             */
            logger.error("ファイル削除" + f.getName());
            f.delete();

        } else if (f.isDirectory()) {
            logger.error("ディレクトリ" + f.getName());
            /*
             * ディレクトリの場合は、すべてのファイルを削除する
             */

            /*
             * 対象ディレクトリ内のファイルおよびディレクトリの一覧を取得
             */
            File[] files = f.listFiles();

            /*
             * ファイルおよびディレクトリをすべて削除
             */
            for (int i = 0; i < files.length; i++) {
                /*
                 * 自身をコールし、再帰的に削除する
                 */
                logger.error("ファイル" + files[i].getName());
                delFile(files[i]);
            }
            /*
             * 自ディレクトリを削除する
             */
            logger.error("ディレクトリ削除" + f.getName()+f.getName());
            f.delete();
        }
    }
}
