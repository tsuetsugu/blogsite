/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.commons.codec.binary.Base64;

/**
 *
 * @author lepra25-pc
 */
public class FileBase64 {

    /**
     * ファイルからBase64作成
     * @param file
     * @return
     * @throws IOException 
     */
    public static String encode(File file) throws IOException {

        int fileLen = (int) file.length();

        byte[] data = new byte[fileLen];
        FileInputStream fis = new FileInputStream(file);
        fis.read(data);
        String result = Base64.encodeBase64String(data);
        return result;
    }

    /**
     * byteからBase64作成
     * @param buf
     * @return
     * @throws IOException 
     */
    
    public static String encode(byte[] buf) throws IOException {

        String result = Base64.encodeBase64String(buf);
        return result;
    }
    
}
