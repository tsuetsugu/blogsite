/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package action;

import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.dispatcher.multipart.MultiPartRequestWrapper;

/**
 *
 * @author lepra25-pc
 */
@Results({
  @Result(name="upload" , location="/profile.jsp"),
  @Result(name="success" , location="/profile.jsp"),
  @Result(name="input" , location="/profile.jsp")
})
public class FileUploadSampleAction extends ActionSupport {

        private File docment;
    private String docmentContentType;
    private String docmentFileName;
    
    private static final long serialVersionUID = 1L;
    private static Logger logger = Logger.getLogger(IndexAction.class);

    public String execute() {
        return ActionSupport.SUCCESS;
    }

    public String upload() throws Exception {
        
    MultiPartRequestWrapper multipartRequest = ((MultiPartRequestWrapper)ServletActionContext.getRequest());

    logger.error( docment.getAbsolutePath());

        return SUCCESS;
    }



    public void setDocment(File docment) {
        this.docment = docment;
    }

    public void setDocmentContentType(String docmentContentType) {
        this.docmentContentType = docmentContentType;
    }

    public void setDocmentFileName(String docmentFileName) {
        this.docmentFileName = docmentFileName;
    }

    public File getDocment() {
        return docment;
    }

    public String getDocmentContentType() {
        return docmentContentType;
    }

    public String getDocmentFileName() {
        return docmentFileName;
    }
    
    
    
}
