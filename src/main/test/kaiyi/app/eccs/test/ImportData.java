package kaiyi.app.eccs.test;

import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.service.ProjectManagementService;
import kaiyi.app.eccs.service.VisitorUserService;
import kaiyi.puer.commons.bean.SpringSelector;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.db.orm.ServiceException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.HashMap;

public class ImportData {
    static ApplicationContext ctx;
    static SpringSelector sel;
    @Before
    public void before(){
        if(ctx==null){
            ctx=new ClassPathXmlApplicationContext("spring-context.xml");
            sel=new SpringSelector(ctx);
        }
    }

    @Test
    public void test() throws ServiceException {
        VisitorUserService vus=sel.getBean(VisitorUserService.class);
        vus.newObject(new HashMap<String,JavaDataTyper>(){{
            put("loginName",new JavaDataTyper("admin"));
            put("realName",new JavaDataTyper("admin"));
            put("password",new JavaDataTyper("111111"));
        }});
    }

    @Test
    public void transferredAmountChange() throws ServiceException {
        ProjectManagementService projectManagementService=sel.getBean(ProjectManagementService.class);
        projectManagementService.transferredAmountChange("15367309322550002");
    }
}
