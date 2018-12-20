package kaiyi.app.eccs.service.impl;

import kaiyi.app.eccs.InjectDao;
import kaiyi.app.eccs.entity.ProjectAmountFlow;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.app.eccs.service.ProjectAmountFlowService;
import org.springframework.stereotype.Service;

@Service("projectAmountFlowService")
public class ProjectAmountFlowServiceImpl extends InjectDao<ProjectAmountFlow> implements ProjectAmountFlowService {
    @Override
    public void addFlow(ProjectManagement projectManagement, int before, int amount, int after, VisitorUser operMan, String remark) {
        ProjectAmountFlow flow=new ProjectAmountFlow();
        flow.setAfterAmount(after);
        flow.setAmount(amount);
        flow.setBeforeAmount(before);
        flow.setOperMan(operMan);
        flow.setProjectManagement(projectManagement);
        flow.setRemark(remark);
        saveObject(flow);
    }
}
