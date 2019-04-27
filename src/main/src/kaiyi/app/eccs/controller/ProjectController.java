package kaiyi.app.eccs.controller;

import kaiyi.app.eccs.entity.*;
import kaiyi.app.eccs.service.*;
import kaiyi.puer.commons.access.AccessControl;
import kaiyi.puer.commons.collection.StreamArray;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.commons.utils.PinyinUtils;
import kaiyi.puer.db.orm.ServiceException;
import kaiyi.puer.db.query.*;
import kaiyi.puer.db.query.CompareQueryExpress.*;
import kaiyi.puer.json.JsonCreator;
import kaiyi.puer.json.JsonParserException;
import kaiyi.puer.json.JsonValuePolicy;
import kaiyi.puer.json.creator.CollectionJsonCreator;
import kaiyi.puer.json.creator.JsonMessageCreator;
import kaiyi.puer.json.creator.ObjectJsonCreator;
import kaiyi.puer.json.parse.SimpleJsonParser;
import kaiyi.puer.web.elements.ChosenBuildHandler;
import kaiyi.puer.web.elements.ChosenElement;
import kaiyi.puer.web.servlet.WebInteractive;
import kaiyi.puer.web.springmvc.IWebInteractive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(ProjectController.rootPath)
@AccessControl(name = "项目", weight = 2f, detail = "工程项目管理",
        code = ProjectController.rootPath,defaultAuthor = true)
public class ProjectController extends ManagerController {
    public static final String rootPath="/project";
    @Resource
    private ProjectManagementService projectManagementService;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private ParticipateEmployeeService participateEmployeeService;
    @Resource
    private ProjectAmountFlowService projectAmountFlowService;
    @Resource
    private ProjectExtractGrantService projectExtractGrantService;
    @Resource
    private ExtractGrantItemService extractGrantItemService;
    @RequestMapping("/projectManager")
    @AccessControl(name = "工程项目管理", weight = 2.1f, detail = "管理工程项目", code = rootPath
            + "/projectManager", parent = rootPath,defaultAuthor = true)
    public String projectManager(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,projectManagementService,getEnableQueryExpress());
        return rootPath+"/projectManager";
    }

    @RequestMapping("/queryPage")
    public String projectQueryPage(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,projectManagementService,getEnableQueryExpress());
        setQueryBackPage(interactive);
        return rootPath+"/projectQueryPage";
    }

    private static final String ProjectManagerUrl=rootPath+"/projectManager";

    @RequestMapping("/projectManager/new")
    @AccessControl(name = "新建工程项目", weight = 2.11f, detail = "新建工程项目", code = rootPath
            + "/projectManager/new", parent = rootPath+"/projectManager")
    public String projectManagerNew(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectManagementService,(w,c)->{
            StreamCollection<Employee> employees=employeeService.getEntitys();
            StreamCollection<ChosenElement> employeeChosen=ChosenElement.buildForStream(employees, new ChosenBuildHandler<Employee>() {
                @Override
                public String getValue(Employee employee) {
                    return employee.getId();
                }

                @Override
                public String getHtml(Employee employee) {
                    return employee.getName();
                }

                @Override
                public boolean isSelect(Employee employee) {
                    return false;
                }

                @Override
                public String[] getSearchString(Employee employee) {
                    return new String[]{
                            employee.getName(),
                            PinyinUtils.getFirstStringPinYin(employee.getName()),
                            PinyinUtils.getStringPinYin(employee.getName())
                    };
                }
            });
            interactive.setRequestAttribute("employeeChosen",employeeChosen);
            w.setRequestAttribute("backUrl",ProjectManagerUrl);
            w.setRequestAttribute("commitUrl",ProjectManagerCommitUrl);
        });
        return rootPath+"/newOrEditProjectManager";
    }
    @RequestMapping("/projectManager/modify")
    @AccessControl(name = "编辑工程项目", weight = 2.12f, detail = "编辑工程项目", code = rootPath
            + "/projectManager/modify", parent = rootPath+"/projectManager")
    public String projectManagerModify(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectManagementService,(w,c)->{
            StreamCollection<Employee> employees=employeeService.getEntitys();
            StreamCollection<ParticipateEmployee> participateEmployees=participateEmployeeService.getEntitys(
                    new CompareQueryExpress("projectManagement",CompareQueryExpress.Compare.EQUAL,c)
            );
            StreamCollection<ChosenElement> employeeChosen=ChosenElement.buildForStream(employees, new ChosenBuildHandler<Employee>() {
                @Override
                public String getValue(Employee employee) {
                    return employee.getId();
                }

                @Override
                public String getHtml(Employee employee) {
                    return employee.getName();
                }

                @Override
                public boolean isSelect(Employee employee) {
                    return participateEmployees.find(f->{
                        return f.getEmployee().getId().equals(employee.getId())&&f.isBindStatus();
                    })!=null;
                }

                @Override
                public String[] getSearchString(Employee employee) {
                    return new String[]{
                            employee.getName(),
                            PinyinUtils.getFirstStringPinYin(employee.getName()),
                            PinyinUtils.getStringPinYin(employee.getName())
                    };
                }
            });
            interactive.setRequestAttribute("employeeChosen",employeeChosen);
            w.setRequestAttribute("backUrl",ProjectManagerUrl);
            w.setRequestAttribute("commitUrl",ProjectManagerCommitUrl);
        });
        return rootPath+"/newOrEditProjectManager";
    }
    private static final String ProjectManagerCommitUrl=rootPath+"/projectManager/commit";
    @RequestMapping(value="/projectManager/commit",method = RequestMethod.POST)
    public void projectManagerCommit(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
         JsonMessageCreator jmc=getSuccessMessage();
        if(interactive.validateRequestParameter(applicationRuntimeService.getValidateReference(),ProjectManagement.class, h->{
            jmc.setError(h);
        })){
            ProjectManagement projectManagement=exuterNewOrUpdate(interactive,projectManagementService,jmc);
            StreamArray<String> users=interactive.getStringStreamArray("employees",";");
            users.forEach(u->{
                participateEmployeeService.employeeBind(projectManagement,u);
            });
            interactive.writeUTF8Text(jmc.build());
        }
    }
    @RequestMapping(value="/projectManager/unbindEmployee",method = RequestMethod.POST)
    public void unbindEmployee(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String projectId=interactive.getStringParameter("projectId","");
        String employeeId=interactive.getStringParameter("employeeId","");
        ProjectManagement project=new ProjectManagement();
        project.setId(projectId);
        Employee employee=new Employee();
        employee.setId(employeeId);
        participateEmployeeService.employeeUnbind(project,employee);
        interactive.writeUTF8Text(getSuccessMessage().build());
    }

    @AccessControl(name = "项目详情", weight = 2.13f, detail = "工程项目详情", code = rootPath
            + "/projectManager/detail", parent = rootPath+"/projectManager")
    @RequestMapping("/projectManager/detail")
    public String projectManagerDetail(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectManagementService,(w,c)->{
            QueryExpress query=new CompareQueryExpress("projectManagement",Compare.EQUAL,c);
            QueryExpress flowQuery=new LinkQueryExpress(query, LinkQueryExpress.LINK.AND,
                    new CompareQueryExpress("enable",Compare.EQUAL,Boolean.TRUE));
            StreamCollection<ProjectAmountFlow> flows=projectAmountFlowService.getEntitys(flowQuery,new OrderBy(flowQuery.getPrefix(),
                    "createTime",OrderBy.TYPE.DESC));
            interactive.setRequestAttribute("flows",flows);
            query=new LinkQueryExpress(query, LinkQueryExpress.LINK.AND,new CompareQueryExpress("bindStatus",Compare.EQUAL,Boolean.TRUE));
            StreamCollection<ParticipateEmployee> participateEmployees=participateEmployeeService.getEntitys(query);
            String joinEmployees=participateEmployees.joinString(m->{
                return m.getEmployee().getName();
            },",");
            interactive.setRequestAttribute("joinEmployees",joinEmployees);
            w.setRequestAttribute("backUrl",ProjectManagerUrl);

        });
        return rootPath+"/projectDetail";
    }

    @RequestMapping("/participateEmployee")
    @AccessControl(name = "项目参与人员", weight = 2.2f, detail = "管理项目参与人员", code = rootPath
            + "/participateEmployee", parent = rootPath,defaultAuthor = true)
    public String participateEmployee(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,participateEmployeeService,new CompareQueryExpress("bindStatus",Compare.EQUAL,Boolean.TRUE));
        return rootPath+"/participateEmployee";
    }

    @RequestMapping(value="unbingUser",method = RequestMethod.POST)
    public void unbingUser(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String id=interactive.getStringParameter("id","");
        participateEmployeeService.unbindById(id);
        interactive.writeUTF8Text(getSuccessMessage().build());
    }

    @RequestMapping("/projectReceivables")
    @AccessControl(name = "工程收款管理", weight = 2.3f, detail = "工程收款管理", code = rootPath
            + "/projectReceivables", parent = rootPath,defaultAuthor = true)
    public String projectReceivables(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        generatorDataListPage(interactive,projectAmountFlowService,getEnableQueryExpress());
        return rootPath+"/projectReceivables";
    }


    private static final String ProjectReceivablesPage=rootPath+"/projectReceivables";

    private static final String ProjectReceivablesCommitUrl=rootPath+"/projectReceivables/commit";

    private static final String ProjectReceivablesGrantCommitUrl=rootPath+"/projectReceivables/extractGrant/commit";
    @RequestMapping("/projectReceivables/new")
    @AccessControl(name = "新增收款", weight = 2.31f, detail = "记录工程收款", code = rootPath
            + "/projectReceivables/new", parent = rootPath+"/projectReceivables",defaultAuthor = true)
    public String projectReceivablesNew(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectAmountFlowService,(w,c)->{
            w.setRequestAttribute("backUrl",ProjectReceivablesPage);
            w.setRequestAttribute("commitUrl",ProjectReceivablesCommitUrl);
        });
        interactive.setRequestAttribute("editor",false);
        return rootPath+"/receivablesEdit";
    }
    @RequestMapping("/projectReceivables/editor")
    @AccessControl(name = "修改收款", weight = 2.32f, detail = "修改收款记录", code = rootPath
            + "/projectReceivables/editor", parent = rootPath+"/projectReceivables",defaultAuthor = true)
    public String projectReceivablesEditor(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectAmountFlowService,(w,c)->{
            w.setRequestAttribute("backUrl",ProjectReceivablesPage);
            w.setRequestAttribute("commitUrl",ProjectReceivablesCommitUrl);
        });
        interactive.setRequestAttribute("editor",true);
        return rootPath+"/receivablesEdit";
    }

    //提交收款记录
    @RequestMapping(value="/projectReceivables/commit",method = RequestMethod.POST)
    public void projectReceivablesCommit(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String projectManagementId=interactive.getStringParameter("projectManagementId","");
        Currency amount=interactive.getCurrency("amount");
        String remark=interactive.getStringParameter("remark","");
        Date receivablesDate=interactive.getDateParameter("receivablesDate",new SimpleDateFormat("yyyy-MM-dd"));
        JsonMessageCreator jmc=getSuccessMessage();
        boolean editor=interactive.getBoolean("editor","true",false);
        String flowId=interactive.getStringParameter("flowId","");
        try {
            if(editor){
                projectAmountFlowService.modify(flowId,amount.getNoDecimalPoint().intValue(),getLoginedUser(interactive),remark,receivablesDate);
                projectManagementService.transferredAmountChange(projectManagementId);
            }else{
                projectManagementService.contractAdmission(projectManagementId,amount.getNoDecimalPoint().intValue(),
                        getLoginedUser(interactive),remark,receivablesDate);
            }
        } catch (ServiceException e) {
            catchServiceException(jmc,e);
        }
        interactive.writeUTF8Text(jmc.build());
    }
    //删除收款记录
    @RequestMapping(value="/projectReceivables/delete",method = RequestMethod.POST)
    public void projectReceivablesDelete(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String flowId=interactive.getStringParameter("flowId","");
        JsonMessageCreator jmc=getSuccessMessage();
        try {
            ProjectManagement pm=projectManagementService.deleteAdmission(flowId,getLoginedUser(interactive));
            projectManagementService.transferredAmountChange(pm.getId());
        } catch (ServiceException e) {
            catchServiceException(jmc,e);
        }
        interactive.writeUTF8Text(jmc.build());
    }
    //查询项目参与人员
    @RequestMapping("/queryProjectParticipateEmployee")
    public void  queryProjectParticipateEmployee(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String projectId=interactive.getStringParameter("projectId","");
        ProjectManagement projectManagement=new ProjectManagement();
        projectManagement.setId(projectId);
        StreamArray<String> excludeEmployee=interactive.getStringStreamArray("excludeEmployee",",");
        QueryExpress query=new CompareQueryExpress("projectManagement",Compare.EQUAL,projectManagement);
        if(excludeEmployee.assertNotEmpty()){
            Object[] objects=new Object[excludeEmployee.getArray().length];
            excludeEmployee.forEachByOrder((i,d)->{
                Employee employee=new Employee();
                employee.setId(d);
                objects[i]=employee;
            });
            query=new LinkQueryExpress(query, LinkQueryExpress.LINK.AND,
                    new ContainQueryExpress("employee",ContainQueryExpress.CONTAINER.NOT_IN,objects));
        }
        query=new LinkQueryExpress(query,LinkQueryExpress.LINK.AND,new CompareQueryExpress("bindStatus",Compare.EQUAL,Boolean.TRUE));
        StreamCollection<ParticipateEmployee> participateEmployees = participateEmployeeService.getEntitys(query);
        CollectionJsonCreator<ParticipateEmployee> jsonCreator=new CollectionJsonCreator<>(participateEmployees,
                new String[]{"employee"}, new JsonValuePolicy<ParticipateEmployee>() {
            @Override
            public JsonCreator getCreator(ParticipateEmployee entity, String field, Object fieldValue){
                if(field.equals("employee")){
                    return new ObjectJsonCreator<Employee>(entity.getEmployee(),new String[]{"id","name"});
                }
                return null;
            }
        });
        interactive.writeUTF8Text(jsonCreator.build());
    }

    @RequestMapping("/projectReceivables/extractGrant")
    @AccessControl(name = "提成发放", weight = 2.31f, detail = "发放工程提成款", code = rootPath
            + "/projectReceivables/extractGrant", parent = rootPath+"/projectReceivables",defaultAuthor = true)
    public String projectReceivablesExtractGrant(@IWebInteractive WebInteractive interactive, HttpServletResponse response){
        newOrEditPage(interactive,projectAmountFlowService,(w,c)->{
            QueryExpress query=new CompareQueryExpress("projectManagement",Compare.EQUAL,c.getProjectManagement());
            query=new LinkQueryExpress(query, LinkQueryExpress.LINK.AND,new CompareQueryExpress("bindStatus",Compare.EQUAL,Boolean.TRUE));
            StreamCollection<ParticipateEmployee> participateEmployees=participateEmployeeService.getEntitys(query);
            String joinEmployees=participateEmployees.joinString(m->{
                return m.getEmployee().getName();
            },",");
            interactive.setRequestAttribute("joinEmployees",joinEmployees);
            //如果已经创建提成项，则直接取出
            ProjectExtractGrant extractGrant=projectExtractGrantService.findByProjectAmountFlow(c.getId());
            interactive.setRequestAttribute("extractGrant",extractGrant);
            String excludeEmployee="";
            if(extractGrant!=null){
                QueryExpress grantItemQuery=new CompareQueryExpress("extractGrant",Compare.EQUAL,extractGrant);
                StreamCollection<ExtractGrantItem> grantItems=extractGrantItemService.getEntitys(grantItemQuery,new OrderBy(grantItemQuery.getPrefix(),
                        "employee"));
                interactive.setRequestAttribute("grantItems",grantItems);
                excludeEmployee=grantItems.joinString(m->{
                    return m.getEmployee().getId();
                },",");
            }
            interactive.setRequestAttribute("excludeEmployee",excludeEmployee);
            w.setRequestAttribute("backUrl",ProjectReceivablesPage);
            w.setRequestAttribute("commitUrl",ProjectReceivablesGrantCommitUrl);
        });
        return rootPath+"/extractGrant";
    }
    //项目提成发放
    @RequestMapping(value="/projectReceivables/extractGrant/commit",method = RequestMethod.POST)
    public void extractGrantCommit(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException, JsonParserException {
        String projectAmountFlowId=interactive.getStringParameter("projectAmountFlowId","");
        ProjectAmountFlow flow=new ProjectAmountFlow();
        flow.setId(projectAmountFlowId);
        ProjectExtractGrant grant=projectExtractGrantService.signleQuery("projectAmountFlow",flow);
        String grantItems=interactive.getHttpServletRequest().getParameter("grantItems");
        SimpleJsonParser jsonParser=new SimpleJsonParser(grantItems);
        Map<String,JavaDataTyper> jsonData=jsonParser.doParser();
        Set<ExtractGrantItem> items=new HashSet<>();
        JsonMessageCreator jmc=getSuccessMessage();
        List<Map<String,JavaDataTyper>> jsonList=jsonData.get("grantItem").listValue();
        for(Map<String,JavaDataTyper> jl:jsonList){
            ExtractGrantItem item=new ExtractGrantItem();
            item.setAmount(jl.get("amount").currencyValue().getNoDecimalPoint().intValue());
            item.setRate(jl.get("rate").integerValue(0));
            Employee employee=new Employee();
            employee.setId(jl.get("employeeId").stringValue());
            item.setEmployee(employee);
            items.add(item);
        }
        try{
            ProjectManagement pm;
            if(grant==null){
                pm=projectExtractGrantService.newProjectExtractGrant(projectAmountFlowId,items);
            }else{
                pm=projectExtractGrantService.modifyExtractGranItem(grant.getId(),items);
            }
            projectManagementService.transferredAmountChange(pm.getId());
        }catch(ServiceException e){
            catchServiceException(jmc,e);
        }
        interactive.writeUTF8Text(jmc.build());
    }
    @RequestMapping(value="/deleteExtractGrantItem",method=RequestMethod.POST)
    public void deleteExtractGrantItem(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        String itemId=interactive.getStringParameter("itemId","");
        String grantId=extractGrantItemService.deleteById(itemId);
        String projectManagementId=projectExtractGrantService.deleteExtractGrantItem(grantId);
        projectManagementService.transferredAmountChange(projectManagementId);
        interactive.writeUTF8Text(getSuccessMessage().build());
    }
}
