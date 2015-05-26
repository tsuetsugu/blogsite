/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import java.io.File;
import com.opensymphony.xwork2.ActionSupport;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Image;
import model.User;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import static org.apache.struts2.ServletActionContext.getServletContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;


/**
 *
 * @author lepra25-pc
 */
public class UploadAction extends AbstractDBAction {

    private static Logger logger = Logger.getLogger(IndexAction.class);
    private File file;
    private String contentType;
    private String filename;

    public void setUpload(File file) {
        this.file = file;
    }

    public void setUploadContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setUploadFileName(String filename) {
        this.filename = filename;
    }

    public File getFile() {
        return file;
    }

    public String getContentType() {
        return contentType;
    }

    public String getFilename() {
        return filename;
    }


    public String execute() throws Exception {
        
    //BufferedImage readImage = null;
        
        User user = getCurrentUser();
        String realPath = ServletActionContext.getServletContext().getRealPath("/" + user.getId()); 
        
        
        
        File mdir = new File(realPath);
        mdir.mkdir();
        
        logger.error(realPath);
          
        try {
            FileInputStream fis = new FileInputStream(file.getPath());
            BufferedInputStream bis = new BufferedInputStream(fis);
            
            int size = (int)file.length();
            byte[] buf = new byte[size];
            
            bis.read(buf);
            
            Image img = new Image();
            img.setContentType(contentType);
            img.setImagesize(size);
            img.setUser_img(buf);
            img.setFilename(filename);
            img.setFilefullpath(realPath);
            
            File filePath = new File(mdir,filename);
            
            img.setFilepath("./" + user.getId() + "/" + filename);

            
            FileUtils.copyFile(file,filePath);
            img.setFile(filePath);
            setUploadImage(img);
            
           
            // エラーがあった場合は、スタックトレースを出力
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        //os.flush();    
        //...
        return "success";
    }
}
