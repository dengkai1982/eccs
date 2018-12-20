package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.ServiceExceptionDefine;
import kaiyi.app.eccs.entity.Employee;
import kaiyi.app.eccs.entity.ProjectAmountFlow;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.app.eccs.service.*;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.db.orm.ServiceException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.function.BiFunction;

@Service("projectManagementService")
public class ProjectManagementServiceImpl extends InjectDao<ProjectManagement> implements ProjectManagementService {
    private static final long serialVersionUID = -5632890670627982356L;
    @Resource
    private ProjectAmountFlowService projectAmountFlowService;
    @Resource
    private EmployeeService employeeService;
    @Resource
    private ParticipateEmployeeService participateEmployeeService;
    @Resource
    private ProjectExtractGrantService projectExtractGrantService;
    @Override
    protected BiFunction<String, JavaDataTyper, Object> getNewOrUpdateConvert() {
        return (f,d)->{
            if(f.equals("contractAmount")||f.equals("transferredAmount")){
                return d.currencyValue().getNoDecimalPoint().intValue();
            }
            return null;
        };
    }

    @Override
    public void contractAdmission(String id, int amount,VisitorUser operMan,String remark) throws ServiceException {
        ProjectManagement pm=findForPrimary(id);
        if(pm==null){
            throw ServiceExceptionDefine.contractNotExist;
        }
        int contractAmount=pm.getContractAmount();
        int transferredAmount=pm.getTransferredAmount();
        if(amount<=0){
            throw ServiceExceptionDefine.admissionZeor;
        }
        if(amount>(contractAmount-transferredAmount)){
            throw ServiceExceptionDefine.admissionGTContract;
        }
        pm.setTransferredAmount(transferredAmount+amount);
        projectAmountFlowService.addFlow(pm,transferredAmount,amount,transferredAmount+amount,
                operMan,remark);
        updateObject(pm);
    }

    @Override
    public void deleteAdmission(String flowId,VisitorUser operMan)throws ServiceException {
        if(projectExtractGrantService.findByProjectAmountFlow(flowId)!=null){
            throw ServiceExceptionDefine.extractGrantExist;
        }
        ProjectAmountFlow flow=projectAmountFlowService.findForPrimary(flowId);
        ProjectManagement management=flow.getProjectManagement();
        management.setTransferredAmount(management.getTransferredAmount()-flow.getAmount());
        flow.setRemark(flow.getRemark()+",删除收款记录,操作人员:"+operMan.getRealName());
        updateObject(management);
        flow.setDeleted(true);
        flow.setEnable(false);
        em.merge(flow);
    }

    @Override
    public void employeeJoin(String projectId, String employeeId, boolean isJoin)throws ServiceException {
        Employee employee=employeeService.findForPrimary(employeeId);
        if(employee==null){
            throw ServiceExceptionDefine.employeeNotExist;
        }
        ProjectManagement pm=findForPrimary(projectId);
        if(pm==null){
            throw ServiceExceptionDefine.contractNotExist;
        }
        if(isJoin){
            participateEmployeeService.employeeBind(pm,employeeId);
        }else{
            participateEmployeeService.employeeUnbind(pm,employee);
        }
    }
}
