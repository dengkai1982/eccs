package kaiyi.app.eccs.service;

import kaiyi.app.eccs.entity.ProjectAmountFlow;
import kaiyi.app.eccs.entity.ProjectManagement;
import kaiyi.app.eccs.entity.VisitorUser;
import kaiyi.puer.db.orm.DatabaseQuery;

public interface ProjectAmountFlowService extends DatabaseQuery<ProjectAmountFlow> {

    void addFlow(ProjectManagement projectManagement,int before,int amount,int after,VisitorUser operMan, String remark);

}
