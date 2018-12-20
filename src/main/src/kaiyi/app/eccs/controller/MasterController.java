package kaiyi.app.eccs.controller;

import kaiyi.app.eccs.entity.VisitorMenu;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.app.eccs.service.VisitorUserService;
import kaiyi.puer.commons.access.AccessControl;
import kaiyi.puer.db.orm.ServiceException;
import kaiyi.puer.json.creator.JsonMessageCreator;
import kaiyi.puer.web.servlet.WebInteractive;
import kaiyi.puer.web.springmvc.IWebInteractive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(MasterController.rootPath)
public class MasterController extends ManagerController {
    public static final String rootPath="/master";
    @Resource
    private VisitorUserService visitorUserService;
    //执行登录
    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public void login(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        JsonMessageCreator msg=getSuccessMessage();
        try{
            VisitorUser user=visitorUserService.login(
                    interactive.getStringParameter("username",""),
                    interactive.getStringParameter("password",""));
            addUserToSession(interactive,user);
            msg.setBody(interactive.generatorRequestUrl(EmployeeController.rootPath+"/employeeManager",null));
        }catch(ServiceException e){
            catchServiceException(msg,e);
        }
        interactive.writeUTF8Text(msg.build());
    }

    @RequestMapping("/exit")
    public void exit(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        removeUserBySession(interactive);
        interactive.writeUTF8Text(getSuccessMessage().build());
    }
}
