/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interceptor;

import action.IndexAction;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;
import static constants.Constant.*;
import javax.servlet.http.HttpSession;
import model.User;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

/**
 *
 * @author lepra25-pc
 */
public class LoginInterceptor extends AbstractInterceptor{
    public String intercept(ActionInvocation actioninvocation) throws Exception {
        
        HttpSession session = ServletActionContext.getRequest().getSession();
        
        User user = new User();
        User showuser = new User();
        
        user = (User) session.getAttribute(SESSION_CURRENT_USER);
        showuser = (User) session.getAttribute(SESSION_SHOW_USER);
        if(showuser == null && user == null){
            return "login";
        }

        return actioninvocation.invoke();
    }    
 
}
