package kaiyi.app.eccs.controller.interceptor;

import kaiyi.app.eccs.controller.ManagerController;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.web.servlet.WebInteractive;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ChangeMenuInterceptor extends ManagerController implements HandlerInterceptor{

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        WebInteractive interactive=WebInteractive.build(request,response);
        if(StreamCollection.assertEmpty(getMenuForApplication(interactive))){
            addMenuToApplication(interactive.getHttpServletRequest());
        }
        perfectPage(interactive);
        request.setAttribute(WebInteractive.SAVE_NAME,interactive);
        return true;
    }

}
