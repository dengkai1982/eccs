package kaiyi.app.eccs.controller;

import kaiyi.app.eccs.entity.VisitorMenu;
import kaiyi.puer.commons.collection.Cascadeable;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.web.servlet.WebInteractive;
import kaiyi.puer.web.springmvc.IWebInteractive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping(CommonController.rootPath)
public class CommonController extends ManagerController{
    public static final String rootPath="/commons";
    @RequestMapping("/chooseFirstMenu")
    public void chooseFirstMenu(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String id=interactive.getStringParameter("id","");
        StreamCollection<VisitorMenu> menus=getMenuForApplication(interactive.getHttpServletRequest());
        VisitorMenu finder=(VisitorMenu)Cascadeable.findById(menus,id);
        if(finder==null||finder.getChildrenList().isEmpty()){
            interactive.sendRedict(interactive.getWebPage().getContextPath()+"/login.jsp");
        }else{
            List<? extends Cascadeable> cascadeList=finder.getChildrenList();
            for(Cascadeable cascade:cascadeList) {
                VisitorMenu menu=(VisitorMenu)cascade;
                if(menu.isShowable()) {
                    interactive.sendRedict(interactive.generatorRequestUrl(menu.getActionFlag(),null));
                    break;
                }
            }
        }
    }
}
