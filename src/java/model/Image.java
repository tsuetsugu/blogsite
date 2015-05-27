/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author lepra25-pc
 */
public class Image implements Serializable {

    // Serial Version UID を付ける
    private static final long serialVersionUID = 1L;
    
    private String contentType;
    private int imagesize;
    private byte[] user_img;
    private String filename;
    private String filepath;
    private String filefullpath;
    private File file;

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public int getImagesize() {
        return imagesize;
    }

    public void setImagesize(int imagesize) {
        this.imagesize = imagesize;
    }

    public byte[] getUser_img() {
        return user_img;
    }

    public void setUser_img(byte[] user_img) {
        this.user_img = user_img;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
     * 相対パス
     * @return 
     */
     
    public String getFilepath() {
        return filepath;
    }

    /**
     * 相対パス
     * @param filepath 
     */
    
    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    /**
     * 絶対パス
     * @return 
     */
    public String getFilefullpath() {
        return filefullpath;
    }

    /**
     * 絶対パス設定
     * @param filefullpath 
     */
    public void setFilefullpath(String filefullpath) {
        this.filefullpath = filefullpath;
    }
 
    
    
}
