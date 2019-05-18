package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.CurrencyUtils;
import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.ProjectAmountFlow;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.app.eccs.service.ProjectAmountFlowService;
import kaiyi.puer.commons.data.Currency;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;

@Service("projectAmountFlowService")
public class ProjectAmountFlowServiceImpl extends InjectDao<ProjectAmountFlow> implements ProjectAmountFlowService {
    @Override
    public void addFlow(ProjectManagement projectManagement, int amount,
                        VisitorUser operMan, String remark, Date receivablesTime) {
        ProjectAmountFlow flow=new ProjectAmountFlow();
        flow.setAmount(amount);
        flow.setOperMan(operMan);
        flow.setProjectManagement(projectManagement);
        flow.setRemark(remark);
        flow.setReceivablesDate(receivablesTime);
        float proportion=projectManagement.getProportion();
        flow.setProportion((int)proportion);
        Currency commission=CurrencyUtils.computerPercentage(proportion,Currency.parseForNoDecimalPoint(amount));
        commission=commission.divide(projectManagement.getCommissionRate(),true);
        flow.setCommissionAmount(commission.getNoDecimalPoint().intValue());
        saveObject(flow);
    }

    @Override
    public void modify(String entityId, int amount, VisitorUser operMan, String remark, Date receivablesTime) {
        ProjectAmountFlow flow=findForPrimary(entityId);
        if(Objects.nonNull(flow)){
            flow.setAmount(amount);
            flow.setOperMan(operMan);
            flow.setRemark(remark);
            flow.setReceivablesDate(receivablesTime);
            Currency commission=CurrencyUtils.computerPercentage(flow.getProportion(),Currency.parseForNoDecimalPoint(amount));
            flow.setCommissionAmount(commission.getNoDecimalPoint().intValue());
        }
    }

    @Override
    public void deleteForId(String flowId) {
        em.remove(em.getReference(this.entityClass, flowId));
    }
}
