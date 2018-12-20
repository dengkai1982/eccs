package kaiyi.app.eccs.controller;

import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.service.EmployeeService;
import kaiyi.app.eccs.service.ExtractGrantItemService;
import kaiyi.app.eccs.service.ProjectExtractGrantService;
import kaiyi.puer.commons.access.AccessControl;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.db.query.CompareQueryExpress;
import kaiyi.puer.json.creator.JsonMessageCreator;
import kaiyi.puer.web.elements.ChosenElement;
import kaiyi.puer.web.servlet.WebInteractive;
import kaiyi.puer.web.springmvc.IWebInteractive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

@Controller
@RequestMapping(EmployeeController.rootPath)
@AccessControl(name = "员工", weight = 1f, detail = "员工管理",
        code = EmployeeController.rootPath,defaultAuthor = true)
public class EmployeeController extends ManagerController {
    @Resource
    private EmployeeService employeeService;
    @Resource
    private ExtractGrantItemService extractGrantItemService;
    @Resource
    private ProjectExtractGrantService projectExtractGrantService;
    public static final String rootPath="/employee";

    @RequestMapping("/employeeManager")
    @AccessControl(name = "员工管理", weight = 1.1f, detail = "管理中的员工信息", code = rootPath
            + "/employeeManager", parent = rootPath,defaultAuthor = true)
    public String employeeManager(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,employeeService,getEnableQueryExpress());
        return rootPath+"/employeeManager";
    }

    @RequestMapping("/queryPage")
    public String employeeQueryPage(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,employeeService,getEnableQueryExpress());
        setQueryBackPage(interactive);
        return rootPath+"/employeeQueryPage";
    }

    private static final BiConsumer<WebInteractive,Employee> customer=(w,e)->{
        StreamCollection<ChosenElement> onTheJobChosen=new StreamCollection<>();
        if(e!=null){
            onTheJobChosen.add(new ChosenElement("在职","true",e.isOnTheJob()));
            onTheJobChosen.add(new ChosenElement("离职","false",!e.isOnTheJob()));
        }else{
            onTheJobChosen.add(new ChosenElement("在职","true"));
            onTheJobChosen.add(new ChosenElement("离职","false"));
        }
        w.setRequestAttribute("onTheJobChosen",onTheJobChosen);
        w.setRequestAttribute("backUrl",rootPath+"/employeeManager");
        w.setRequestAttribute("commitUrl",rootPath+"/employeeManager/commit");
    };

    @RequestMapping("/employeeManager/new")
    @AccessControl(name = "新增员工", weight = 1.11f, detail = "增加员工", code = rootPath
            + "/employeeManager/new", parent = rootPath+"/employeeManager")
    public String employeeNew(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,employeeService,customer);
        return rootPath+"/newOrEditEmployee";
    }
    @RequestMapping("/employeeManager/modify")
    @AccessControl(name = "修改员工", weight = 1.12f, detail = "修改员工信息", code = rootPath
            + "/employeeManager/modify", parent = rootPath+"/employeeManager")
    public String employeeModify(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,employeeService,customer);
        return rootPath+"/newOrEditEmployee";
    }

    @RequestMapping(value="/employeeManager/commit",method = RequestMethod.POST)
    public void employeeCommit(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        JsonMessageCreator jmc=getSuccessMessage();
        if(interactive.validateRequestParameter(applicationRuntimeService.getValidateReference(),Employee.class,h->{
            jmc.setError(h);
        })){
            exuterNewOrUpdate(interactive,employeeService,jmc);
            interactive.writeUTF8Text(jmc.build());
        }
    }
    @RequestMapping("/extractGrantManager")
    @AccessControl(name = "提成发放记录", weight = 1.2f, detail = "发放工程提成项目款", code = rootPath
            + "/extractGrantManager", parent = rootPath,defaultAuthor = true)
    public String extractGrantManager(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,extractGrantItemService,getIdentifyNotNullQueryExpress());
        return rootPath+"/extractGrantManager";
    }


    @RequestMapping("/extractGrantManager/new")
    @AccessControl(name = "新增工程款发放", weight = 1.21f, detail = "增加新的工程款发放记录", code = rootPath
            + "/extractGrantManager/new", parent = rootPath+"/extractGrantManager")
    public String extractGrantManagerNew(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectExtractGrantService,(w,c)->{
            w.setRequestAttribute("backUrl",rootPath+"/extractGrantManager");
            w.setRequestAttribute("commitUrl",rootPath+"/extractGrantManager/commit");
        });
        return rootPath+"/newOrEditExtractGrant";
    }
    @RequestMapping("/extractGrantManager/edit")
    @AccessControl(name = "修改工程款发放", weight = 1.22f, detail = "修改工程款发放记录", code = rootPath
            + "/extractGrantManager/edit", parent = rootPath+"/extractGrantManager")
    public String extractGrantManagerEdit(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectExtractGrantService,(w,c)->{
            w.setRequestAttribute("backUrl",rootPath+"/extractGrantManager");
            w.setRequestAttribute("commitUrl",rootPath+"/extractGrantManager/commit");
        });
        return rootPath+"/newOrEditExtractGrant";
    }
}
