package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.CurrencyUtils;
import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.ServiceExceptionDefine;
import kaiyi.app.eccs.entity.*;
import kaiyi.app.eccs.service.*;
import kaiyi.puer.commons.collection.StreamCollection;
import kaiyi.puer.commons.data.Currency;
import kaiyi.puer.commons.data.JavaDataTyper;
import kaiyi.puer.db.orm.ServiceException;
import kaiyi.puer.db.query.CompareQueryExpress;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.Objects;
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
    protected void objectBeforePersistHandler(ProjectManagement projectManagement) throws ServiceException {
        setCommisssion(projectManagement);
    }

    private void setCommisssion(ProjectManagement projectManagement){
        int contractAmount=projectManagement.getContractAmount();
        float proportion=projectManagement.getProportion();
        Currency contractAmountCurrency=Currency.parseForNoDecimalPoint(contractAmount);
        Currency commission=CurrencyUtils.computerPercentage(proportion,contractAmountCurrency);
        projectManagement.setCommissionAmount(commission.getNoDecimalPoint().intValue());
    }

    @Override
    protected void objectBeforeUpdateHandler(ProjectManagement projectManagement) throws ServiceException {
        setCommisssion(projectManagement);
    }

    @Override
    public void contractAdmission(String id, int amount,VisitorUser operMan,String remark,
                                  Date receivablesDate) throws ServiceException {
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
        projectAmountFlowService.addFlow(pm,amount,
                operMan,remark,receivablesDate);
        updateObject(pm);
    }



    @Override
    public ProjectManagement deleteAdmission(String flowId,VisitorUser operMan)throws ServiceException {
        if(projectExtractGrantService.findByProjectAmountFlow(flowId)!=null){
            throw ServiceExceptionDefine.extractGrantExist;
        }
        ProjectAmountFlow flow=projectAmountFlowService.findForPrimary(flowId);
        ProjectManagement management=flow.getProjectManagement();
        management.setTransferredAmount(management.getTransferredAmount()-flow.getAmount());
        projectAmountFlowService.deleteForId(flowId);
        return management;
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

    @Override
    public void transferredAmountChange(String entityId) {
        //projectManagement
        ProjectManagement projectManagement=findForPrimary(entityId);
        if (Objects.nonNull(projectManagement)) {
            Long amount=(Long)em.createQuery("select COALESCE(SUM(o.amount),0) from "+getEntityName(ProjectAmountFlow.class)
                    +" o where o.projectManagement=:projectManagement").setParameter("projectManagement",projectManagement)
                    .getSingleResult();
            projectManagement.setTransferredAmount(amount.intValue());
            Long commissionAmount=(Long)em.createQuery("select COALESCE(SUM(o.totalGrantAmount),0) from "+getEntityName(ProjectExtractGrant.class)
                    +" o where o.projectManagement=:projectManagement").setParameter("projectManagement",projectManagement)
                    .getSingleResult();
            projectManagement.setFinishCommission(commissionAmount.intValue());
            updateObject(projectManagement);
        }
    }
}
