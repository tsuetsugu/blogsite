/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.File;

/**
 *
 * @author lepra25-pc
 */
public class DeleteFile {

    /**
     *
     * @param f
     */
    public void delFile(File f) {
        /*
         * ファイルまたはディレクトリが存在しない場合は何もしない
         */
        if (f.exists() == false) {
            return;
        }

        if (f.isFile()) {
            /*
             * ファイルの場合は削除する
             */
            f.delete();

        } else if (f.isDirectory()) {

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
                delFile(files[i]);
            }
            /*
             * 自ディレクトリを削除する
             */
            f.delete();
        }
    }
}
