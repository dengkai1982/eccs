package kaiyi.app.eccs.controller;

import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.entity.ProjectExtractGrant;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.service.*;
import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.commons.time.DateTimeUtil;
import kaiyi.puer.db.query.CompareQueryExpress;
import kaiyi.puer.db.query.CompareQueryExpress.*;
import kaiyi.puer.db.query.LikeQueryExpress;
import kaiyi.puer.db.query.LikeQueryExpress.MATCH;
import kaiyi.puer.db.query.LinkQueryExpress;
import kaiyi.puer.db.query.LinkQueryExpress.*;
import kaiyi.puer.db.query.OrderBy;
import kaiyi.puer.web.QueryParameterConvert;
import kaiyi.puer.web.QueryTransfor;
import kaiyi.puer.web.servlet.WebInteractive;
import kaiyi.puer.web.springmvc.IWebInteractive;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequestMapping(DataQueryController.rootPath)
public class DataQueryController extends ManagerController {
    public static final String rootPath="/query";

    @Resource
    private EmployeeService employeeService;
    @Resource
    private ProjectManagementService projectManagementService;
    @Resource
    private ParticipateEmployeeService participateEmployeeService;
    @Resource
    private ProjectAmountFlowService projectAmountFlowService;
    @Resource
    private ExtractGrantItemService extractGrantItemService;
    @Resource
    private ProjectExtractGrantService projectExtractGrantService;

    @RequestMapping("/extractGrantItem")
    public void extractGrantItem(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        excuteQueryToJson("id", interactive, extractGrantItemService, new QueryParameterConvert() {
            @Override
            public QueryTransfor getTransfor(String field, JavaDataTyper data) {
                switch(field){
                    case "projectManagementId":
                        ProjectManagement projectManagement=new ProjectManagement();
                        projectManagement.setId(data.stringValue());
                        ProjectExtractGrant grant=projectExtractGrantService.signleQuery("projectManagement",projectManagement);
                        if(grant!=null){
                            return new QueryTransfor(new CompareQueryExpress("extractGrant",
                                    Compare.EQUAL,grant),LINK.AND);
                        }
                        return null;
                    case "employeeId":
                        Employee employee=new Employee();
                        employee.setId(data.stringValue());
                        return new QueryTransfor(new CompareQueryExpress("employee",
                                Compare.EQUAL,employee),LINK.AND);
                }
                return null;
            }
            @Override
            public QueryTransfor[] getDefaultTransfors() {
                return new QueryTransfor[0];
            }
        },"extractGrantItem");
    }

    @RequestMapping("/projectAmountFlow")
    public void projectAmountFlow(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        excuteQueryToJson("id", interactive, projectAmountFlowService, new QueryParameterConvert() {
            @Override
            public QueryTransfor getTransfor(String field, JavaDataTyper data) {
                switch (field){
                    case "startTime":
                        return getStratDateQuery("receivablesDate",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "endTime":
                        return getEndDateQuery("receivablesDate",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "projectManagementId":
                        ProjectManagement projectManagement=new ProjectManagement();
                        projectManagement.setId(data.stringValue());
                        return new QueryTransfor(new CompareQueryExpress("projectManagement",
                                Compare.EQUAL,projectManagement),LINK.AND);
                    case "startAmount":
                        Currency startAmount=interactive.getCurrency("startAmount");
                        return new QueryTransfor(new CompareQueryExpress("amount",
                                Compare.GT_AND_EQUAL,startAmount.getNoDecimalPoint()),LINK.AND);
                    case "endAmount":
                        Currency endAmount=interactive.getCurrency("endAmount");
                        return new QueryTransfor(new CompareQueryExpress("amount",
                                Compare.LS_AND_EQUAL,endAmount.getNoDecimalPoint()),LINK.AND);
                }
                return null;
            }

            @Override
            public QueryTransfor[] getDefaultTransfors() {
                return new QueryTransfor[]{
                    new QueryTransfor(getEnableQueryExpress(),LINK.AND)
                };
            }
        },"projectAmountFlow");
    }

    @RequestMapping("/participateEmployee")
    public void participateEmployee(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        excuteQueryToJson("id", interactive, participateEmployeeService, new QueryParameterConvert() {
            @Override
            public QueryTransfor getTransfor(String field, JavaDataTyper data) {
                switch(field){
                    case "employeeId":
                        Employee employee=new Employee();
                        employee.setId(data.stringValue());
                        return new QueryTransfor(new CompareQueryExpress("employee",
                                Compare.EQUAL,employee),LINK.AND);
                    case "projectManagementId":
                        ProjectManagement projectManagement=new ProjectManagement();
                        projectManagement.setId(data.stringValue());
                        return new QueryTransfor(new CompareQueryExpress("projectManagement",
                                Compare.EQUAL,projectManagement),LINK.AND);
                }
                return null;
            }

            @Override
            public QueryTransfor[] getDefaultTransfors() {
                return new QueryTransfor[]{
                        new QueryTransfor(new CompareQueryExpress("bindStatus",Compare.EQUAL,Boolean.TRUE),LINK.AND)
                };
            }
        },"participateEmployee");
    }
    @RequestMapping("/projectManagement")
    public void projectManagement(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        excuteQueryToJson("id", interactive, projectManagementService, new QueryParameterConvert() {
            @Override
            public QueryTransfor getTransfor(String field, JavaDataTyper data) {
                switch(field){
                    case "projectName":
                        return new QueryTransfor(new LikeQueryExpress("projectName",data.stringValue(),MATCH.YES),LINK.AND);
                    case "contractNumber":
                        return new QueryTransfor(new CompareQueryExpress("contractNumber",Compare.EQUAL,data.stringValue()),LINK.AND);
                    case "projectType":
                        return new QueryTransfor(new CompareQueryExpress("projectType",Compare.EQUAL,data.stringValue()),LINK.AND);
                    case "startTime":
                        return getStratDateQuery("startTime",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "endTime":
                        return getEndDateQuery("endTime",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "gtContractAmount":
                        return new QueryTransfor(new CompareQueryExpress("contractAmount",Compare.GT_AND_EQUAL,
                                data.currencyValue().getNoDecimalPoint().intValue()),LINK.AND);
                    case "ltContractAmount":
                        return new QueryTransfor(new CompareQueryExpress("contractAmount",Compare.LS_AND_EQUAL,
                                data.currencyValue().getNoDecimalPoint().intValue()),LINK.AND);
                    case "gtTransferredAmount":
                        return new QueryTransfor(new CompareQueryExpress("transferredAmount",Compare.GT_AND_EQUAL,
                                data.currencyValue().getNoDecimalPoint().intValue()),LINK.AND);
                    case "ltTransferredAmount":
                        return new QueryTransfor(new CompareQueryExpress("transferredAmount",Compare.LS_AND_EQUAL,
                                data.currencyValue().getNoDecimalPoint().intValue()),LINK.AND);
                }
                return null;
            }

            @Override
            public QueryTransfor[] getDefaultTransfors() {
                return new QueryTransfor[]{
                    new QueryTransfor(getEnableQueryExpress(),LINK.AND)
                };
            }
        },"projectManagement");
    }

    @RequestMapping("/employee")
    public void employeeQuery(@IWebInteractive WebInteractive interactive, HttpServletResponse response) throws IOException {
        excuteQueryToJson("id", interactive, employeeService, new QueryParameterConvert() {
            @Override
            public QueryTransfor getTransfor(String field, JavaDataTyper data) {
                switch(field){
                    case "onTheJob":
                        return new QueryTransfor(new CompareQueryExpress("onTheJob",
                                Compare.EQUAL,data.booleanValue(false)), LinkQueryExpress.LINK.AND);
                    case "name":
                        return new QueryTransfor(new LikeQueryExpress("name",data.stringValue(),
                                MATCH.YES),LINK.AND);
                    case "startJobDate":
                        return getStratDateQuery("jobDate",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "endJobDate":
                        return getEndDateQuery("jobDate",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "startExitDate":
                        return getStratDateQuery("exitDate",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                    case "endExitDate":
                        return getEndDateQuery("exitDate",data,DateTimeUtil.yyyyMMdd,LINK.AND);
                }
                return null;
            }

            @Override
            public QueryTransfor[] getDefaultTransfors() {
                return new QueryTransfor[]{
                        new QueryTransfor(getEnableQueryExpress(),LINK.AND)
                };
            }
        },"employee");
    }

    @Override
    protected OrderBy getDefaultOrderBy(String prefix, String orderByFlag) {
        if(orderByFlag.equals("extractGrantItem")){
            return new OrderBy(prefix,"updateTime",OrderBy.TYPE.DESC);
        }
        return new OrderBy(prefix,"createTime",OrderBy.TYPE.DESC);
    }
}
